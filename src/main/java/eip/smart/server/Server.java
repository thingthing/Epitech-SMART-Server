package eip.smart.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.LogManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import eip.smart.cscommons.configuration.Configuration;
import eip.smart.cscommons.model.modeling.Modeling;
import eip.smart.server.configuration.LocationManager;
import eip.smart.server.configuration.ServerDefaultConfiguration;
import eip.smart.server.model.agent.AgentLogic;
import eip.smart.server.model.modeling.ModelingLogic;
import eip.smart.server.model.modeling.ModelingTask;
import eip.smart.server.model.modeling.file.DefaultFileModelingManager;
import eip.smart.server.model.modeling.file.ModelingManager;
import eip.smart.server.net.tcp.IoAgentContainer;
import eip.smart.server.net.tcp.TCPHandler;
import eip.smart.server.net.tcp.TCPPacketCodecFactory;
import eip.smart.server.net.udp.UDPHandler;
import eip.smart.server.net.udp.UDPPacketCodecFactory;

/**
 * Application Lifecycle Listener implementation class Server
 *
 */
@WebListener
public class Server implements ServletContextListener {

	/**
	 * The logger to log things.
	 */
	private final static Logger	LOGGER	= LoggerFactory.getLogger(Server.class);

	/**
	 * The static instance of the server.
	 */
	private static Server		server;

	static {
		Configuration.CONFIG_DIR = LocationManager.LOCATION_CONFIG;
		Configuration.initDefaultValues(ServerDefaultConfiguration.values());
	}

	/**
	 * @return The static server.
	 */
	public static Server getServer() {
		return (Server.server);
	}

	private NioSocketAcceptor	acceptorTCP			= new NioSocketAcceptor();

	private IoAcceptor			acceptorUDP			= new NioDatagramAcceptor();
	private Configuration		conf				= new Configuration("server");
	/**
	 * The current selected modeling.
	 */
	private ModelingLogic		currentModeling		= null;

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
		Server.LOGGER.info("Server stopping");
		if (this.currentModeling != null)
			Server.getServer().modelingStop();
		this.threadPool.shutdown();
		this.threadPool.shutdownNow();

		this.socketTCPListenStop();
	}

	/**
	 * Called when the Server is created.
	 *
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			Server.server = this;
			Server.LOGGER.info("Server starting");

			// Convert Tomcat JUL log to SLF4J
			if (new Configuration("logging").getProperty("LOGGING_BRIDGE").equals("TRUE")) {
				LogManager.getLogManager().reset();
				SLF4JBridgeHandler.install();
				java.util.logging.Logger.getLogger("global").setLevel(Level.FINEST);
			}

			// Config of TCP Acceptor
			this.acceptorTCP.setCloseOnDeactivation(true);
			this.acceptorTCP.setReuseAddress(true);
			this.acceptorTCP.getFilterChain().addLast("logger", new LoggingFilter());
			this.acceptorTCP.getFilterChain().addLast("protocol", new ProtocolCodecFilter(new TCPPacketCodecFactory()));
			TCPHandler agentHandler = new TCPHandler();
			agentHandler.setIoAgentContainer(this.ioAgentContainer);
			this.acceptorTCP.setHandler(agentHandler);
			this.acceptorTCP.getSessionConfig().setReadBufferSize(2048);
			this.acceptorTCP.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 0);
			try {
				this.socketTCPListen();
			} catch (IllegalArgumentException | IOException e) {
				Server.LOGGER.error("Unable to open TCP socket", e);
			}

			// Config of UDP Acceptor
			this.acceptorUDP.setCloseOnDeactivation(true);
			((DatagramSessionConfig) this.acceptorUDP.getSessionConfig()).setReuseAddress(true);
			this.acceptorUDP.getFilterChain().addLast("logger", new LoggingFilter());
			this.acceptorUDP.getFilterChain().addLast("protocol", new ProtocolCodecFilter(new UDPPacketCodecFactory()));
			this.acceptorUDP.setHandler(new UDPHandler());
			this.acceptorUDP.getSessionConfig().setReadBufferSize(2048);
			this.acceptorUDP.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 0);
			try {
				this.socketUDPListen();
			} catch (IOException e) {
				Server.LOGGER.error("Unable to open UDP socket", e);
			}

		} catch (Exception e) {
			Server.LOGGER.error("Uncatched exception", e);
		}
	}

	public AgentLogic getAgentByName(String name) {
		if (name != null)
			for (AgentLogic a : this.getAgentsAvailable())
				if (name.equals(a.getName()))
					return (a);
		return null;
	}

	/**
	 * Get all the connected agents.
	 *
	 * @return
	 */
	public List<AgentLogic> getAgentsAvailable() {
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
	public ModelingLogic getCurrentModeling() {
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
	 * Get all the connected TCP sessions.
	 *
	 * @return
	 */
	public List<IoSession> getSessions() {
		return (this.ioAgentContainer.getSessions());
	}

	/**
	 * Return whereas the TCP Acceptor is active and can handle TCP connections;
	 *
	 * @return
	 */
	public boolean isAcceptorActive() {
		return (this.acceptorTCP.isActive());
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
		this.currentModeling = new ModelingLogic(name);
		this.modelingSave();
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
	public List<Modeling> modelingList() {
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
		this.currentModeling = new ModelingLogic(modeling);
		return (true);
	}

	/**
	 * Pause the current modeling.
	 */
	public void modelingPause() {
		this.currentTask.pause();
	}

	/**
	 * Resume the current modeling.
	 */
	public void modelingResume() {
		this.currentTask.resume();
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
		this.currentModeling = null;
	}

	/**
	 * Open the TCP acceptor so it will handle new TCP connections.
	 *
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public void socketTCPListen() throws IOException, IllegalArgumentException {
		this.acceptorTCP.bind(new InetSocketAddress(this.conf.getPropertyInteger("TCP_PORT")));
		Server.LOGGER.info("TCP Server open on port " + this.conf.getPropertyInteger("TCP_PORT"));
	}

	/**
	 * Stop the TCP acceptor so it will not longer handle TCP connections.
	 */
	public void socketTCPListenStop() {
		for (IoSession session : this.acceptorTCP.getManagedSessions().values())
			session.close(true);
		this.acceptorTCP.unbind();
	}

	/**
	 * Open the UDP acceptor so it will handle new UDP connections.
	 *
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public void socketUDPListen() throws IOException, IllegalArgumentException {
		this.acceptorUDP.bind(new InetSocketAddress(this.conf.getPropertyInteger("UDP_PORT")));
		Server.LOGGER.info("UDP Server open on port " + this.conf.getPropertyInteger("UDP_PORT"));
	}

	/**
	 * Stop the UDP acceptor so it will not longer handle UDP connections.
	 */
	public void socketUDPListenStop() {
		for (IoSession session : this.acceptorUDP.getManagedSessions().values())
			session.close(true);
		this.acceptorUDP.unbind();
	}
}
