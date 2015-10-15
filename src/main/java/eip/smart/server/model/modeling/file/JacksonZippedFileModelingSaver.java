package eip.smart.server.model.modeling.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eip.smart.cscommons.model.JSONViews;
import eip.smart.cscommons.model.modeling.Modeling;

/**
 * This implementation of the FileModelingManager uses a compression method to reduce size taken by files.
 * Uses JSON serialization with Jackson implementation.
 */
public abstract class JacksonZippedFileModelingSaver extends FileModelingSaver {

	protected int	compressionLevel	= -1;

	public int getCompressionLevel() {
		return this.compressionLevel;
	}

	@Override
	public Modeling load(String name) {
		name = FileModelingSaver.addExtension(name);
		Modeling modeling = null;
		if (!this.exists(name))
			return (null);
		File file = new File(FileModelingSaver.getDir(), name);
		FileInputStream fis = null;
		ZipInputStream zis = null;
		FileModelingSaver.LOGGER.info("Loading modelisation at " + file.getAbsolutePath());

		try {
			ObjectMapper mapper = new ObjectMapper();
			fis = new FileInputStream(file);
			zis = new ZipInputStream(fis);
			Modeling tmp = mapper.readerWithView(JSONViews.DISK.class).forType(Modeling.class).readValue(zis);
			modeling = new Modeling();
		} catch (JsonMappingException e) {
			FileModelingSaver.LOGGER.error("file mapping error", e);
		} catch (JsonParseException e) {
			FileModelingSaver.LOGGER.error("non-well-formed content of the file", e);
		} catch (IOException e) {
			FileModelingSaver.LOGGER.error("unable to load the file", e);
		} finally {
			if (zis != null)
				try {
					zis.close();
				} catch (IOException e) {
					FileModelingSaver.LOGGER.error("Unable to close the zip stream", e);
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
		File file = new File(FileModelingSaver.getDir(), FileModelingSaver.addExtension(modeling.getName()));
		FileOutputStream fos = null;
		ZipOutputStream zos = null;

		try {
			ObjectMapper mapper = new ObjectMapper();
			fos = new FileOutputStream(file);
			zos = new ZipOutputStream(fos);
			if (this.compressionLevel <= 9 && this.compressionLevel >= 0)
				zos.setLevel(this.compressionLevel);
			Modeling modelingParsed = new Modeling(modeling);
			mapper.writerWithView(JSONViews.DISK.class).writeValue(zos, modelingParsed);
			zos.finish();
		} catch (JsonMappingException e) {
			FileModelingSaver.LOGGER.error("file mapping error", e);
		} catch (JsonGenerationException e) {
			FileModelingSaver.LOGGER.error("non-well-formed content of the file", e);
		} catch (IOException e) {
			FileModelingSaver.LOGGER.error("unable to save the file", e);
		} finally {
			if (zos != null)
				try {
					zos.close();
				} catch (IOException e) {
					FileModelingSaver.LOGGER.error("Unable to close the zip stream", e);
				}
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e) {
					FileModelingSaver.LOGGER.error("Unable to close the file stream", e);
				}
		}
		FileModelingSaver.LOGGER.info("Saved modelisation at " + file.getAbsolutePath());
	}

	public void setCompressionLevel(int compressionLevel) {
		this.compressionLevel = compressionLevel;
	}
}
