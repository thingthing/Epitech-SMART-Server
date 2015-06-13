package eip.smart.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import eip.smart.model.Agent;
import eip.smart.model.Modeling;
import eip.smart.model.proxy.SimpleModelingProxy;
import eip.smart.server.modeling.DefaultFileModelingManager;
import eip.smart.server.modeling.ModelingManager;
import eip.smart.server.modeling.ModelingTask;
import eip.smart.server.net.AgentServerHandler;
import eip.smart.server.net.IoAgentContainer;
import eip.smart.server.net.PacketCodecFactory;
import eip.smart.server.servlet.modeling.ModelingInfo;
import eip.smart.server.util.Configuration;

/**
 * Application Lifecycle Listener implementation class Server
 *
 */
@WebListener
public class Server implements ServletContextListener {

	/**
	 * The logger to log things.
	 */
	private final static Logger	LOGGER	= Logger.getLogger(ModelingInfo.class.getName());

	/**
	 * The static instance of the server.
	 */
	private static Server		server;

	/**
	 * @return The static server.
	 */
	public static Server getServer() {
		return (Server.server);
	}

	private NioSocketAcceptor	acceptor			= new NioSocketAcceptor();

	private Configuration		conf				= new Configuration("server");

	/**
	 * The current selected modeling.
	 */
	private Modeling			currentModeling		= null;

	/**
	 * The current thread task the threadPool is running.
	 */
	private ModelingTask		currentTask			= null;

	/**
	 * The ioAgentContainer to store Agents and bound TCP sessions.
	 */
	private IoAgentContainer	ioAgentContainer	= new IoAgentContainer();

	/**
	 * The Manager to manage Modelings and store it.
	 * Different implementations allow to different ways of storage.
	 */
	private ModelingManager		manager				= new DefaultFileModelingManager();

	/**
	 * Is the current modeling running.
	 */
	private boolean				running				= false;

	/**
	 * The threadPool allowing to run a Modeling.
	 */
	private ExecutorService		threadPool			= Executors.newSingleThreadExecutor();

	/**
	 * Called when the Server is destroyed (shutdown).
	 *
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		Server.LOGGER.log(Level.INFO, "Server stopping");
		if (this.currentModeling != null)
			Server.getServer().modelingStop();
		this.threadPool.shutdown();
		this.threadPool.shutdownNow();

		this.socketListenStop();
	}

	/**
	 * Called when the Server is created.
	 *
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		Configuration.setDefaultProperty("server", "TCP_PORT", "4200");

		Server.LOGGER.log(Level.INFO, "Server starting");
		Server.server = this;

		this.acceptor.setCloseOnDeactivation(true);
		this.acceptor.setReuseAddress(true);

		this.acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		this.acceptor.getFilterChain().addLast("protocol", new ProtocolCodecFilter(new PacketCodecFactory()));

		AgentServerHandler agentHandler = new AgentServerHandler();
		agentHandler.setIoAgentContainer(this.ioAgentContainer);
		this.acceptor.setHandler(agentHandler);

		this.acceptor.getSessionConfig().setReadBufferSize(2048);
		this.acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		try {
			this.socketListen();
		} catch (IllegalArgumentException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get all the connected agents.
	 *
	 * @return
	 */
	public ArrayList<Agent> getAgentsAvailable() {
		return (this.ioAgentContainer.getAgents());
	}

	public Configuration getConfiguration() {
		return (this.conf);
	}

	/**
	 * Get the current modeling.
	 *
	 * @return
	 */
	public Modeling getCurrentModeling() {
		return (this.currentModeling);
	}

	/**
	 * Get the IoAgentContainer which store the Agents and TCP sessions.
	 *
	 * @return
	 */
	public IoAgentContainer getIoAgentContainer() {
		return (this.ioAgentContainer);
	}

	/**
	 * Get the port.
	 */
	public int getPort() {
		return (this.conf.getPropertyInteger("TCP_PORT"));
	}

	/**
	 * Get all the connected TCP sessions.
	 *
	 * @return
	 */
	public ArrayList<IoSession> getSessions() {
		return (this.ioAgentContainer.getSessions());
	}

	/**
	 * Return whereas the TCP Acceptor is active and can handle TCP connections;
	 *
	 * @return
	 */
	public boolean isAcceptorActive() {
		return (this.acceptor.isActive());
	}

	/**
	 * Return true if the current modeling is paused.
	 *
	 * @return
	 */
	public boolean isPaused() {
		return (this.running && this.currentTask.isPaused());
	}

	/**
	 * Return true if the current modeling is running.
	 *
	 * @return
	 */
	public boolean isRunning() {
		return (this.running);
	}

	/**
	 * Crate a new modeling with a given name.
	 *
	 * @param name
	 *            the name of the modeling to create.
	 * @return true if successful, false otherwise (modeling already exist).
	 */
	public boolean modelingCreate(String name) {
		if (this.manager.exists(name))
			return (false);
		this.currentModeling = new Modeling(name);
		Server.LOGGER.log(Level.INFO, "New modeling (" + this.currentModeling.getName() + ") created.");
		return (true);
	}

	/**
	 * Delete the modeling with a given name.
	 *
	 * @param name
	 *            the name of the modeling to delete.
	 * @return always true.
	 */
	public boolean modelingDelete(String name) {
		return (this.manager.delete(name));
	}

	/**
	 * Get the list of all the existing modelings.
	 *
	 * @return
	 */
	public ArrayList<SimpleModelingProxy> modelingList() {
		return (this.manager.list());
	}

	/**
	 * Load a given modeling and make it current.
	 *
	 * @param name
	 *            the name of the modeling to load.
	 * @return true if successful, false otherwise (another modeling is already the current modeling).
	 */
	public boolean modelingLoad(String name) {
		Modeling modeling = this.manager.load(name);
		if (modeling == null)
			return (false);
		this.currentModeling = modeling;
		return (true);
	}

	/**
	 * Pause the current modeling.
	 */
	public void modelingPause() {
		this.currentTask.pause();
		Server.LOGGER.log(Level.INFO, "Modeling (" + this.currentModeling.getName() + ") paused.");
	}

	/**
	 * Resume the current modeling.
	 */
	public void modelingResume() {
		this.currentTask.resume();
		Server.LOGGER.log(Level.INFO, "Modeling (" + this.currentModeling.getName() + ") resumed.");
	}

	/**
	 * Save the current modeling.
	 */
	public void modelingSave() {
		this.manager.save(this.currentModeling);
	}

	/**
	 * Start the current modeling.
	 */
	public void modelingStart() {
		Server.LOGGER.log(Level.INFO, "Modeling (" + this.currentModeling.getName() + ") started.");
		this.running = true;
		this.currentTask = new ModelingTask(this.currentModeling);
		this.threadPool.execute(this.currentTask);
	}

	/**
	 * Stop and save the current modeling.
	 * The modeling is then no longer the current modeling.
	 */
	public void modelingStop() {
		if (this.running) {
			this.currentTask.stop();
			this.running = false;
			this.currentTask = null;
		}
		this.modelingSave();
		Server.LOGGER.log(Level.INFO, "Modeling (" + this.currentModeling.getName() + ") stopped.");
		this.currentModeling = null;
	}

	/**
	 * Open the TCP acceptor so it will handle new TCP connections.
	 *
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public void socketListen() throws IOException, IllegalArgumentException {
		this.acceptor.bind(new InetSocketAddress(this.getPort()));
		Server.LOGGER.log(Level.INFO, "TCP Server open on port " + this.getPort());
	}

	/**
	 * Stop the TCP acceptor so it will not longer handle TCP connections.
	 */
	public void socketListenStop() {
		for (IoSession session : this.acceptor.getManagedSessions().values())
			session.close(true);
		this.acceptor.unbind();
	}
}
