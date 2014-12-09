package eip.smart.server;

import java.util.ArrayList;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import eip.smart.model.Agent;

/**
 * Application Lifecycle Listener implementation class Server
 *
 */
@WebListener
public class Server implements ServletContextListener {

	public static Server getServer() {
		return (Server.server);
	}

	private static Server		server;

	private ArrayList<Agent>	agentsAvaiable	= new ArrayList<>();

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
	public void contextDestroyed(ServletContextEvent arg0) {}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		Server.server = this;
	}

	public ArrayList<Agent> getAgentsAvaiable() {
		return (this.agentsAvaiable);
	}

}
