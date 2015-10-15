package eip.smart.server.model.modeling.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import eip.smart.cscommons.model.modeling.Modeling;
import eip.smart.server.model.modeling.ModelingLogic;

/**
 * This implementation of the FileModelingManager uses a compression method to reduce size taken by files.
 * Uses Java serialization.
 */
public abstract class DefaultZippedFileModelingManager extends FileModelingManager {

	protected int	compressionLevel	= -1;

	public int getCompressionLevel() {
		return this.compressionLevel;
	}

	@Override
	public Modeling load(String name) {
		name = FileModelingManager.addExtension(name);

		if (!this.exists(name))
			return (null);
		File file = new File(FileModelingManager.getDir(), name);
		FileInputStream fis = null;
		ZipInputStream zis = null;
		ObjectInputStream ois = null;
		Modeling modeling = null;

		try {
			fis = new FileInputStream(file);
			zis = new ZipInputStream(fis);
			ois = new ObjectInputStream(zis);

			// modeling = (Modeling) ois.readObject();

			Modeling tmpModeling = (Modeling) ois.readObject();
			modeling = new ModelingLogic(tmpModeling);

			ois.close();
			fis.close();
		} catch (InvalidClassException e) {
			FileModelingManager.LOGGER.warn("Saved modelisation (" + name + ") is obsolete and will be ignored.");
			FileModelingManager.LOGGER.info("Saved modelisation (" + name + ") is obsolete and will be ignored.");
			return (null);
		} catch (IOException | ClassNotFoundException e) {
			FileModelingManager.LOGGER.error("Unable to find the file", e);
		} finally {
			if (ois != null)
				try {
					ois.close();
				} catch (IOException e) {
					FileModelingManager.LOGGER.error("Unable to close the object stream", e);
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
		ZipOutputStream zos = null;

		try {

			fos = new FileOutputStream(file);
			zos = new ZipOutputStream(fos);
			if (this.compressionLevel >= 0 && this.compressionLevel <= 9)
				zos.setLevel(this.compressionLevel);
			oos = new ObjectOutputStream(fos);
			Modeling modelingParsed = new Modeling(modeling);
			oos.writeObject(modelingParsed);
			zos.finish();
		} catch (IOException e) {
			FileModelingManager.LOGGER.error("Unable to save the file", e);
		} finally {
			if (oos != null)
				try {
					oos.close();
				} catch (IOException e) {
					FileModelingManager.LOGGER.error("Unable to close the object stream", e);
				}
			if (zos != null)
				try {
					zos.close();
				} catch (IOException e) {
					FileModelingManager.LOGGER.error("Unable to close the zip stream", e);
				}
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e) {
					FileModelingManager.LOGGER.error("Unable to close the file stream", e);
				}
		}
	}

	public void setCompressionLevel(int compressionLevel) {
		this.compressionLevel = compressionLevel;
	}
}
