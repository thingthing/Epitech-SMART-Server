package eip.smart.server.modeling;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eip.smart.model.Modeling;
import eip.smart.model.proxy.SimpleModelingProxy;


import eip.smart.model.proxy.FileModelingProxy;

public class JacksonFileModelingManager extends FileModelingManager {

    @Override
    public Modeling load(String name) {
        name = JacksonFileModelingManager.addExtension(name);
        Modeling modeling = null;
        if (!this.exists(name))
            return (null);
        File file = new File(JacksonFileModelingManager.DIR, name);
        LOGGER.log(Level.INFO, "Loading modelisation at " + file.getAbsolutePath());

        try {
            ObjectMapper mapper = new ObjectMapper();
            SimpleModelingProxy tmp = mapper.readValue(file, SimpleModelingProxy.class);
            modeling = new Modeling();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (modeling);

    }

    @Override
    public void save(Modeling modeling) {
        File file = new File(JacksonFileModelingManager.DIR, JacksonFileModelingManager.addExtension(modeling.getName()));

        try {
            ObjectMapper mapper = new ObjectMapper();
            FileModelingProxy tmp = new FileModelingProxy(modeling);
            mapper.writeValue(file, tmp);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            return ;
        } catch (JsonGenerationException e) {
            e.printStackTrace();
            return ;
        } catch (IOException e) {
            e.printStackTrace();
            return ;
        }
        LOGGER.log(Level.INFO, "Successfully saved modelisation at " + file.getAbsolutePath());
    }
}
