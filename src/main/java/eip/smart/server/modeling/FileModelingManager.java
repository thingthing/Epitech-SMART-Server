package eip.smart.server.modeling;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import eip.smart.model.Modeling;
import eip.smart.model.proxy.SimpleModelingProxy;
import eip.smart.server.util.Configuration;
import eip.smart.server.util.DefaultConfiguration;

/**
 * This class is the base class for the file based modeling managers, it handles the name and creation of the files,
 * leaving its subclass implements the save and load management.
 */
public abstract class FileModelingManager implements ModelingManager {

	protected final static Logger	LOGGER		= Logger.getLogger(Modeling.class.getName());

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
