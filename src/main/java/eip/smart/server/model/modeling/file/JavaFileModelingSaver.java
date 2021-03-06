package eip.smart.server.model.modeling.file;

import eip.smart.cscommons.model.modeling.Modeling;
import eip.smart.server.model.modeling.ModelingLogic;
import eip.smart.server.util.exception.ModelingNotFoundException;
import eip.smart.server.util.exception.ModelingObsoleteException;

import java.io.*;

/**
 * This class implements the standard way of storing a Modeling to a file, through Java serialization.
 */
public class JavaFileModelingSaver extends FileModelingSaver {

	@Override
	public Modeling load(String name) throws ModelingNotFoundException, ModelingObsoleteException {
		name = FileModelingSaver.addExtension(name, MODELING_EXTENSION);

		if (!this.exists(name))
			throw new ModelingNotFoundException(name);
		File file = new File(FileModelingSaver.getDir(), name);
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		Modeling modeling = null;
		FileModelingSaver.LOGGER.info("Loading modelisation at " + file.getAbsolutePath());

		try {
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);

			// modeling = (Modeling) ois.readObject();

			Modeling tmpModeling = (Modeling) ois.readObject();
			modeling = new ModelingLogic(tmpModeling);

			ois.close();
			fis.close();
		} catch (InvalidClassException | ClassNotFoundException | InvalidObjectException e) {
			throw new ModelingObsoleteException(e);
		} catch (IOException e) {
			FileModelingSaver.LOGGER.error("file not found", e);
		} finally {
			if (ois != null)
				try {
					ois.close();
				} catch (IOException e) {
					FileModelingSaver.LOGGER.error("Unable to close object stream", e);
				}
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					FileModelingSaver.LOGGER.error("Unable to close the file stream", e);
				}
		}
		return (modeling);
	}

	@Override
	public void save(Modeling modeling) {
		File file = new File(FileModelingSaver.getDir(), FileModelingSaver.addExtension(modeling.getName(), MODELING_EXTENSION));
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;

		try {
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			Modeling modelingParsed = new Modeling(modeling);
			oos.writeObject(modelingParsed);
		} catch (IOException e) {
			FileModelingSaver.LOGGER.error("Unable to save the file", e);
			return;
		} finally {
			if (oos != null)
				try {
					oos.close();
				} catch (IOException e) {
					FileModelingSaver.LOGGER.error("Unable to close the object stream", e);
				}
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e) {
					FileModelingSaver.LOGGER.error("Unable to close the file stream", e);
				}
		}
		FileModelingSaver.LOGGER.info("Successfully saved modelisation at " + file.getAbsolutePath());
	}
}
