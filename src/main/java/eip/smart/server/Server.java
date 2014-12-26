package eip.smart.server;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import eip.smart.model.Agent;
import eip.smart.model.Modeling;
import eip.smart.model.proxy.SimpleModelingProxy;
import eip.smart.server.servlet.ModelingInfo;

/**
 * Application Lifecycle Listener implementation class Server
 *
 */
@WebListener
public class Server implements ServletContextListener {

	public static Server getServer() {
		return (Server.server);
	}

	private final static Logger	LOGGER			= Logger.getLogger(ModelingInfo.class.getName());
	private static Server		server;

	private ModelingManager		manager			= new FileModelingManager();

	private ArrayList<Agent>	agentsAvaiable	= new ArrayList<>();
	private ExecutorService		threadPool		= Executors.newSingleThreadExecutor();

	private Modeling			currentModeling	= null;
	private ModelingTask		currentTask		= null;
	private boolean				running			= false;

	public Server() {
		this.agentsAvaiable.add(new Agent());
		this.agentsAvaiable.add(new Agent());
		this.agentsAvaiable.add(new Agent());
		this.agentsAvaiable.add(new Agent());
	}

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
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		Server.LOGGER.log(Level.INFO, "Server starting");
		Server.server = this;
	}

	public ArrayList<Agent> getAgentsAvaiable() {
		return (this.agentsAvaiable);
	}

	public Modeling getCurrentModeling() {
		return (this.currentModeling);
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
}
