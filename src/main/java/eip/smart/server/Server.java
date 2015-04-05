package eip.smart.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import eip.smart.model.Agent;
import eip.smart.model.Modeling;
import eip.smart.model.proxy.SimpleModelingProxy;
import eip.smart.server.modeling.DefaultFileModelingManager;
import eip.smart.server.modeling.ModelingManager;
import eip.smart.server.modeling.ModelingTask;
import eip.smart.server.net.AgentServerHandler;
import eip.smart.server.net.BroadcastUDPHandler;
import eip.smart.server.net.IoAgentContainer;
import eip.smart.server.servlet.ModelingInfo;

/**
 * Application Lifecycle Listener implementation class Server
 *
 */
@WebListener
public class Server implements ServletContextListener {

	private final static Logger	LOGGER	= Logger.getLogger(ModelingInfo.class.getName());

	private static Server		server;

	public static Server getServer() {
		return (Server.server);
	}

	private IoAcceptor			acceptorTCP			= new NioSocketAcceptor();
	private IoAcceptor			acceptorUDP			= new NioDatagramAcceptor();

	private Modeling			currentModeling		= null;
	private ModelingTask		currentTask			= null;

	private IoAgentContainer	ioAgentContainer	= new IoAgentContainer();
	private ModelingManager		manager				= new DefaultFileModelingManager();

	private int					portTCP				= 4200;
	private int					portUDP				= 4300;

	private boolean				running				= false;
	private ExecutorService		threadPool			= Executors.newSingleThreadExecutor();

	public Server() {}

	/**
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
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		Server.LOGGER.log(Level.INFO, "Server starting");
		Server.server = this;

		this.acceptorTCP.getFilterChain().addLast("logger", new LoggingFilter());
		this.acceptorTCP.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));

		AgentServerHandler agentHandler = new AgentServerHandler();
		agentHandler.setIoAgentContainer(this.ioAgentContainer);
		this.acceptorTCP.setHandler(agentHandler);

		this.acceptorTCP.getSessionConfig().setReadBufferSize(2048);
		this.acceptorTCP.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);

		this.acceptorUDP.getFilterChain().addLast("logger", new LoggingFilter());
		this.acceptorUDP.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
		((DatagramSessionConfig) this.acceptorUDP.getSessionConfig()).setReuseAddress(true);
		this.acceptorUDP.setHandler(new BroadcastUDPHandler());
		try {
			this.acceptorUDP.bind(new InetSocketAddress(this.portUDP));
			Server.LOGGER.info(String.format("UDP Port is %d", this.portUDP));
		} catch (IOException e) {
			Server.LOGGER.severe(String.format("Failed to bind UDP port:\n\t%s", e));
		}
	}

	public ArrayList<Agent> getAgentsAvailable() {
		return (this.ioAgentContainer.getAgents());
	}

	public Modeling getCurrentModeling() {
		return (this.currentModeling);
	}

	public IoAgentContainer getIoAgentContainer() {
		return (this.ioAgentContainer);
	}

	public int getPort() {
		return (this.portTCP);
	}

	public ArrayList<IoSession> getSessions() {
		return (this.ioAgentContainer.getSessions());
	}

	public boolean isAcceptorActive() {
		return (this.acceptorTCP.isActive());
	}

	public boolean isPaused() {
		return (this.running && this.currentTask.isPaused());
	}

	public boolean isRunning() {
		return (this.running);
	}

	public boolean modelingCreate(String name) {
		if (this.manager.exists(name))
			return (false);
		this.currentModeling = new Modeling(name);
		Server.LOGGER.log(Level.INFO, "New modeling (" + this.currentModeling.getName() + ") created.");
		return (true);
	}

	public boolean modelingDelete(String name) {
		return (this.manager.delete(name));
	}

	public ArrayList<SimpleModelingProxy> modelingList() {
		return (this.manager.list());
	}

	public boolean modelingLoad(String name) {
		Modeling modeling = this.manager.load(name);
		if (modeling == null)
			return (false);
		this.currentModeling = modeling;
		return (true);
	}

	public void modelingPause() {
		this.currentTask.pause();
		Server.LOGGER.log(Level.INFO, "Modeling (" + this.currentModeling.getName() + ") paused.");
	}

	public void modelingResume() {
		this.currentTask.resume();
		Server.LOGGER.log(Level.INFO, "Modeling (" + this.currentModeling.getName() + ") resumed.");
	}

	public void modelingSave() {
		this.manager.save(this.currentModeling);
	}

	public void modelingStart() {
		Server.LOGGER.log(Level.INFO, "Modeling (" + this.currentModeling.getName() + ") started.");
		this.running = true;
		this.currentTask = new ModelingTask(this.currentModeling);
		this.threadPool.execute(this.currentTask);
	}

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

	public void setPort(int port) {
		this.portTCP = port;
	}

	public void socketListen() throws IOException, IllegalArgumentException {
		this.acceptorTCP.bind(new InetSocketAddress(this.portTCP));
	}

	public void socketListenStop() {
		this.acceptorTCP.setCloseOnDeactivation(true);
		for (IoSession session : this.acceptorTCP.getManagedSessions().values())
			session.close(true);
		this.acceptorTCP.unbind();
		this.acceptorTCP.dispose(false);
	}
}
