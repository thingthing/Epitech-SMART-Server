package eip.smart.server.model.modeling.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import eip.smart.cscommons.model.modeling.Modeling;
import eip.smart.server.model.modeling.ModelingLogic;

/**
 * This class implements the standard way of storing a Modeling to a file, through Java serialization.
 */
public class DefaultFileModelingManager extends FileModelingManager {

	@Override
	public Modeling load(String name) {
		name = FileModelingManager.addExtension(name);

		if (!this.exists(name))
			return (null);
		File file = new File(FileModelingManager.getDir(), name);
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		Modeling modeling = null;
		FileModelingManager.LOGGER.info("Loading modelisation at " + file.getAbsolutePath());

		try {
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);

			// modeling = (Modeling) ois.readObject();

			Modeling tmpModeling = (Modeling) ois.readObject();
			modeling = new ModelingLogic(tmpModeling);

			ois.close();
			fis.close();
		} catch (InvalidClassException | ClassNotFoundException e) {
			FileModelingManager.LOGGER.warn("Saved modelisation (" + name + ") is obsolete and will be ignored.");
			return (null);
		} catch (IOException e) {
			FileModelingManager.LOGGER.error("file not found", e);
		} finally {
			if (ois != null)
				try {
					ois.close();
				} catch (IOException e) {
					FileModelingManager.LOGGER.error("Unable to close object stream", e);
				}
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					FileModelingManager.LOGGER.error("Unable to close the file stream", e);
				}
		}
		return (modeling);
	}

	@Override
	public void save(Modeling modeling) {
		File file = new File(FileModelingManager.getDir(), FileModelingManager.addExtension(modeling.getName()));
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;

		try {
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			Modeling modelingParsed = new Modeling(modeling);
			oos.writeObject(modelingParsed);
		} catch (IOException e) {
			FileModelingManager.LOGGER.error("Unable to save the file", e);
			return;
		} finally {
			if (oos != null)
				try {
					oos.close();
				} catch (IOException e) {
					FileModelingManager.LOGGER.error("Unable to close the object stream", e);
				}
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e) {
					FileModelingManager.LOGGER.error("Unable to close the file stream", e);
				}
		}
		FileModelingManager.LOGGER.info("Successfully saved modelisation at " + file.getAbsolutePath());
	}
}
