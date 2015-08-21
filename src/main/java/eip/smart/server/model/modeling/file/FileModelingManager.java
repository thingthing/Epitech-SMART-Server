package eip.smart.server.model.modeling.file;

import java.io.File;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eip.smart.cscommons.configuration.Configuration;
import eip.smart.cscommons.model.modeling.Modeling;
import eip.smart.server.configuration.ServerDefaultConfiguration;
import eip.smart.server.model.modeling.ModelingLogic;

/**
 * This class is the base class for the file based modeling managers, it handles the name and creation of the files,
 * leaving its subclass implements the save and load management.
 */
public abstract class FileModelingManager implements ModelingManager {

	public static final String	EXTENSION	= ".modeling";

	protected static Logger		LOGGER		= LoggerFactory.getLogger(FileModelingManager.class);

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

	public static File getDir() {
		File dir = new File(new Configuration("location").getProperty(ServerDefaultConfiguration.LOCATION_MODELING.name()));
		dir.mkdirs();
		return (dir);
	}

	public FileModelingManager() {}

	@Override
	public boolean delete(String name) {
		name = FileModelingManager.addExtension(name);
		if (!this.exists(name))
			return (false);
		File file = new File(FileModelingManager.getDir(), name);
		file.delete();
		return (true);
	}

	@Override
	public boolean exists(String name) {
		name = FileModelingManager.addExtension(name);
		File file = new File(FileModelingManager.getDir(), name);
		return (file.exists());
	}

	@Override
	public ArrayList<Modeling> list() {
		ArrayList<Modeling> modelings = new ArrayList<>();
		for (File file : FileModelingManager.getDir().listFiles()) {
			Modeling modeling = this.load(file.getName());
			if (modeling != null) {
				Modeling smp = new ModelingLogic(modeling);
				modelings.add(smp);
			} else {
				ModelingLogic smpObsolete = new ModelingLogic();
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
