package eip.smart.server.model.modeling.file;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eip.smart.cscommons.model.JSONViews;
import eip.smart.cscommons.model.modeling.Modeling;

/**
 * This class implements storage of a Modeling through JSON serialization, using Jackson implementation of JSON.
 */
public class JacksonFileModelingManager extends FileModelingManager {

	@Override
	public Modeling load(String name) {
		name = FileModelingManager.addExtension(name);
		Modeling modeling = null;
		if (!this.exists(name))
			return (null);
		File file = new File(FileModelingManager.getDir(), name);
		FileModelingManager.LOGGER.info("Loading modelisation at " + file.getAbsolutePath());

		try {
			ObjectMapper mapper = new ObjectMapper();
			Modeling tmp = mapper.readerWithView(JSONViews.DISK.class).forType(Modeling.class).readValue(file);
			modeling = new Modeling();
		} catch (JsonMappingException e) {
			FileModelingManager.LOGGER.error("file mapping error", e);
		} catch (JsonParseException e) {
			FileModelingManager.LOGGER.error("non-well-formed content of the file", e);
		} catch (IOException e) {
			FileModelingManager.LOGGER.error("unable to load the file", e);
		}
		return (modeling);

	}

	@Override
	public void save(Modeling modeling) {
		File file = new File(FileModelingManager.getDir(), FileModelingManager.addExtension(modeling.getName()));

		try {
			ObjectMapper mapper = new ObjectMapper();
			Modeling tmp = new Modeling(modeling);
			mapper.writerWithView(JSONViews.DISK.class).writeValue(file, tmp);
		} catch (JsonMappingException e) {
			FileModelingManager.LOGGER.error("file mapping error", e);
			return;
		} catch (JsonGenerationException e) {
			FileModelingManager.LOGGER.error("error with the json generation", e);
			return;
		} catch (IOException e) {
			FileModelingManager.LOGGER.error("error saving the modeling", e);
			return;
		}
		FileModelingManager.LOGGER.info("Successfully saved modelisation at " + file.getAbsolutePath());
	}
}
