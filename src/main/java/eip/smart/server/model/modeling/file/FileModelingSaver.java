package eip.smart.server.model.modeling.file;

import eip.smart.cscommons.configuration.Configuration;
import eip.smart.cscommons.model.modeling.Modeling;
import eip.smart.server.model.modeling.ModelingLogic;
import eip.smart.server.util.configuration.ServerDefaultConfiguration;
import eip.smart.server.util.exception.ModelingAlreadyExistsException;
import eip.smart.server.util.exception.ModelingNotFoundException;
import eip.smart.server.util.exception.ModelingObsoleteException;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;

/**
 * This class is the base class for the file based modeling managers, it handles the name and creation of the files,
 * leaving its subclass implements the save and load management.
 */
public abstract class FileModelingSaver implements ModelingSaver {

	public static final String MODELING_EXTENSION = "modeling";
	public static final String MAPPING_EXTENSION = "mapping";

	protected static Logger		LOGGER		= LoggerFactory.getLogger(FileModelingSaver.class);

	/**
	 * Adds a .modeling extension to a file name if not yet present.
	 *
	 * @param name
	 * @return New name with .modeling extension
	 */
	protected static String addExtension(String name, String extension) {
		String res = name;
		if (!res.matches(".*\\." + extension + "$"))
			res += "." + extension;
		return (res);
	}

	public static File getDir() {
		File dir = new File(new Configuration("location").getProperty(ServerDefaultConfiguration.LOCATION_MODELING.name()));
		dir.mkdirs();
		return (dir);
	}

	public FileModelingSaver() {}

	@Override
	public void clear() {
		for (Modeling m : this.list())
			try {
				this.delete(m.getName());
			} catch (ModelingNotFoundException e) {
				FileModelingSaver.LOGGER.error("Should never happen", e);
			}
	}

	@Override
	public void copy(String name, String copy) throws ModelingNotFoundException, ModelingAlreadyExistsException {
		if (!this.exists(name))
			throw new ModelingNotFoundException(name);
		if (this.exists(copy))
			throw new ModelingAlreadyExistsException(copy);
		ModelingLogic m;
		try {
			m = new ModelingLogic(this.load(name));
			m.setName(copy);
			this.save(m);
		} catch (ModelingObsoleteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(String name) throws ModelingNotFoundException {
		if (!this.exists(name))
			throw new ModelingNotFoundException(name);
		getModelingFile(name).delete();
		getMappingFile(name).delete();
	}

	@Override
	public boolean exists(String name) {
		return (getModelingFile(name).exists());
	}

	@Override
	public ArrayList<Modeling> list() {
		ArrayList<Modeling> modelings = new ArrayList<>();
		for (File file : FileModelingSaver.getDir().listFiles()) {
			if (!FilenameUtils.getExtension(file.getName()).equals(MODELING_EXTENSION))
				continue;
			try {
				Modeling modeling = this.preview(file.getName());
				if (modeling == null)
					throw new ModelingNotFoundException(file.getName());
				modelings.add(modeling);
			} catch (ModelingNotFoundException e) {
				FileModelingSaver.LOGGER.error("Should never happen", e);
			} catch (ModelingObsoleteException e) {
				ModelingLogic obsModeling = new ModelingLogic();
				obsModeling.setName(FilenameUtils.removeExtension(file.getName()));
				obsModeling.setObsolete(true);
				modelings.add(obsModeling);
			}
		}
		return (modelings);
	}

	public Modeling preview(String name) throws ModelingNotFoundException, ModelingObsoleteException {
		return (this.load(name));
	}

	@Override
	public abstract Modeling load(String name) throws ModelingNotFoundException, ModelingObsoleteException;

	protected static File getModelingFile(String name) {
		return (new File(FileModelingSaver.getDir(), FileModelingSaver.addExtension(name, MODELING_EXTENSION)));
	}

	protected static File getMappingFile(String name) {
		return (new File(FileModelingSaver.getDir(), FileModelingSaver.addExtension(name, MAPPING_EXTENSION)));
	}

	@Override
	public abstract void save(Modeling modeling);
}
