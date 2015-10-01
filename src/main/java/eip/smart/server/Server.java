package eip.smart.server;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.LogManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.mina.core.session.IoSession;
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

/**
 * Application Lifecycle Listener implementation class Server
 *
 */
@WebListener
public class Server implements ServletContextListener {

	/**
	 * The logger to log things.
	 */
	final static Logger		LOGGER	= LoggerFactory.getLogger(Server.class);

	/**
	 * The static instance of the server.
	 */
	private static Server	server;

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

	Configuration				conf				= new Configuration("server");

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

	private ServerSocketManager	socketManager		= new ServerSocketManager();

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

		this.socketManager.socketTCPListenStop();
		this.socketManager.socketUDPListenStop();
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

			this.socketManager.init();
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

	public Configuration getConf() {
		return this.conf;
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

	public ServerSocketManager getSocketManager() {
		return this.socketManager;
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
}
