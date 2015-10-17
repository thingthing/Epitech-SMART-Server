package eip.smart.server;

import java.util.logging.Level;
import java.util.logging.LogManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import eip.smart.cscommons.configuration.Configuration;
import eip.smart.server.util.configuration.LocationManager;
import eip.smart.server.util.configuration.ServerDefaultConfiguration;

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

	private ServerAgentManager		agentManager	= new ServerAgentManager();
	private Configuration			configuration	= new Configuration("server");
	private ServerModelingManager	modelingManager	= new ServerModelingManager();
	private ServerSocketManager		socketManager	= new ServerSocketManager();

	/**
	 * Called when the Server is destroyed (shutdown).
	 *
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		Server.LOGGER.info("Server stopping");
		if (this.modelingManager.getCurrentModeling() != null)
			Server.getServer().modelingManager.modelingStop();
		this.modelingManager.stop();
		this.agentManager.stop();

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

	public ServerAgentManager getAgentManager() {
		return (this.agentManager);
	}

	public Configuration getConfiguration() {
		return (this.configuration);
	}

	public ServerModelingManager getModelingManager() {
		return (this.modelingManager);
	}

	public ServerSocketManager getSocketManager() {
		return (this.socketManager);
	}

	@SuppressWarnings("static-method")
	public void stop() {
		System.exit(0);
	}

}
