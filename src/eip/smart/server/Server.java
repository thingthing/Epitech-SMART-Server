package eip.smart.server;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import eip.smart.model.Agent;
import eip.smart.model.Modeling;

/**
 * Application Lifecycle Listener implementation class Server
 *
 */
@WebListener
public class Server implements ServletContextListener {

	public static Server getServer() {
		return (Server.server);
	}

	private static Server			server;

	private Hashtable<String, File>	savedModelings	= new Hashtable<>();
	private Modeling				currentModeling;
	private boolean					running			= false;
	private ArrayList<Agent>		agentsAvaiable	= new ArrayList<>();

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
		this.running = false;
		Server.getServer().saveCurrentModeling();
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		Server.server = this;
		this.findSavedModelings();
	}

	public void createModeling(String name) {
		this.currentModeling = new Modeling(name);
	}

	private void findSavedModelings() {
		// @ TODO Récupérer tout les fichiers d'un dossier de sauvegarde et les charger dans la map.
	}

	public ArrayList<Agent> getAgentsAvaiable() {
		return (this.agentsAvaiable);
	}

	public Modeling getCurrentModeling() {
		return (this.currentModeling);
	}

	public boolean isRunning() {
		return (this.running);
	}

	public boolean loadModeling(String name) {
		// @ TODO Change la modelisation courante
		return (false);
	}

	public void saveCurrentModeling() {
		// @ TODO Sauvegarde la modelisation courante sur le disque.
	}

	public void startModeling() {
		while (this.running)
			try {
				// @ TODO La modelisation qui tourne.
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}

	public void stopModeling() {

	}
}
