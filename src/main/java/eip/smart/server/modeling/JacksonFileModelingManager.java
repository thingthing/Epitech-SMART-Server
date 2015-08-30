package eip.smart.server.modeling;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eip.smart.model.Modeling;
import eip.smart.model.proxy.FileModelingProxy;
import eip.smart.model.proxy.SimpleModelingProxy;

/**
 * This class implements storage of a Modeling through JSON serialization, using Jackson implementation of JSON.
 */
public class JacksonFileModelingManager extends FileModelingManager {

    @Override
    public Modeling load(String name) {
        name = JacksonFileModelingManager.addExtension(name);
        Modeling modeling = null;
        if (!this.exists(name))
            return (null);
        File file = new File(JacksonFileModelingManager.DEFAULT_DIR, name);
        LOGGER.info("Loading modelisation at " + file.getAbsolutePath());

        try {
            ObjectMapper mapper = new ObjectMapper();
            SimpleModelingProxy tmp = mapper.readValue(file, SimpleModelingProxy.class);
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
        File file = new File(JacksonFileModelingManager.DEFAULT_DIR, JacksonFileModelingManager.addExtension(modeling.getName()));

        try {
            ObjectMapper mapper = new ObjectMapper();
            FileModelingProxy tmp = new FileModelingProxy(modeling);
            mapper.writeValue(file, tmp);
        } catch (JsonMappingException e) {
			FileModelingManager.LOGGER.error("file mapping error", e);
            return ;
        } catch (JsonGenerationException e) {
			FileModelingManager.LOGGER.error("error with the json generation", e);
            return ;
        } catch (IOException e) {
			FileModelingManager.LOGGER.error("error saving the modeling", e);
            return ;
        }
        LOGGER.info("Successfully saved modelisation at " + file.getAbsolutePath());
    }
}