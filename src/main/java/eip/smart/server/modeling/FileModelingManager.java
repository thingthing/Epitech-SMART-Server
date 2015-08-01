package eip.smart.server.modeling;



import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.LogManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import eip.smart.model.Modeling;
import eip.smart.model.agent.Agent;
import eip.smart.model.proxy.SimpleModelingProxy;
import eip.smart.server.modeling.DefaultFileModelingManager;
import eip.smart.server.modeling.ModelingManager;
import eip.smart.server.modeling.ModelingTask;
import eip.smart.server.net.tcp.AgentServerHandler;
import eip.smart.server.net.tcp.IoAgentContainer;
import eip.smart.server.net.tcp.TCPPacketCodecFactory;
import eip.smart.server.util.Configuration;

import java.io.File;

import eip.smart.server.Server;
import eip.smart.server.util.DefaultConfiguration;

/**
 * This class is the base class for the file based modeling managers, it handles the name and creation of the files,
 * leaving its subclass implements the save and load management.
 */
public abstract class FileModelingManager implements ModelingManager {

	//protected final static Logger	LOGGER		= Logger.getLogger(Modeling.class.getName());
	protected static Logger	LOGGER	= LoggerFactory.getLogger(FileModelingManager.class);


	public static File				DEFAULT_DIR	= null;

	public static final String		EXTENSION	= ".modeling";

	/**
	 * Adds a .modeling extension to a file name if not yet present.
	 *
	 * @param name
	 * @return New name with .modeling extension
	 */
	protected static String addExtension(String name) {
		String res = name;
		if (!res.matches(".*\\.modeling$"))
			res += FileModelingManager.EXTENSION;
		return (res);
	}

	public FileModelingManager() {
		if (FileModelingManager.DEFAULT_DIR == null)
			FileModelingManager.DEFAULT_DIR = new File(new Configuration("location").getProperty(DefaultConfiguration.LOCATION_MODELING.name()));
		FileModelingManager.DEFAULT_DIR.mkdirs();
	}

	@Override
	public boolean delete(String name) {
		name = FileModelingManager.addExtension(name);
		if (!this.exists(name))
			return (false);
		File file = new File(FileModelingManager.DEFAULT_DIR, name);
		file.delete();
		return (true);
	}

	@Override
	public boolean exists(String name) {
		name = FileModelingManager.addExtension(name);
		File file = new File(FileModelingManager.DEFAULT_DIR, name);
		return (file.exists());
	}

	@Override
	public ArrayList<SimpleModelingProxy> list() {
		ArrayList<SimpleModelingProxy> modelings = new ArrayList<>();
		for (File file : FileModelingManager.DEFAULT_DIR.listFiles()) {
			Modeling modeling = this.load(file.getName());
			if (modeling != null) {
				SimpleModelingProxy smp = new SimpleModelingProxy(modeling);
				modelings.add(smp);
			} else {
				SimpleModelingProxy smpObsolete = new SimpleModelingProxy();
				smpObsolete.setName(file.getName());
				smpObsolete.setObsolete(true);
				modelings.add(smpObsolete);
			}
		}
		return (modelings);
	}

	@Override
	public abstract Modeling load(String name);

	@Override
	public abstract void save(Modeling modeling);
}
