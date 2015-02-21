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

public class JacksonFileModelingManager extends ModelingManager {

    private static String addExtension(String name) {
        String res = name;
        if (!res.matches(".*\\.modeling$"))
            res += JacksonFileModelingManager.EXTENSION;
        return (res);
    }

    private final static Logger	LOGGER		= Logger.getLogger(Modeling.class.getName());
    private static File			DIR			= new File(new File(System.getProperty("catalina.base")).getAbsolutePath(), "modelings");

    private static String		EXTENSION	= ".modeling";

    public JacksonFileModelingManager() {
        JacksonFileModelingManager.DIR.mkdirs();
    }

    @Override
    public boolean delete(String name) {
        name = JacksonFileModelingManager.addExtension(name);
        if (!this.exists(name))
            return (false);
        File file = new File(JacksonFileModelingManager.DIR, name);
        file.delete();
        return (true);
    }

    @Override
    public boolean exists(String name) {
        name = JacksonFileModelingManager.addExtension(name);
        File file = new File(JacksonFileModelingManager.DIR, name);
        return (file.exists());
    }

    @Override
    public ArrayList<SimpleModelingProxy> list() {
        ArrayList<SimpleModelingProxy> modelings = new ArrayList<>();
        for (File file : JacksonFileModelingManager.DIR.listFiles()) {
            Modeling modeling = this.load(file.getName());
            if (modeling != null)
            {
                SimpleModelingProxy smp = new SimpleModelingProxy(modeling);
                modelings.add(smp);
            }
            else
            {
                SimpleModelingProxy smpObsolete = new SimpleModelingProxy();
                smpObsolete.setName(file.getName());
                smpObsolete.setObsolete(true);
                modelings.add(smpObsolete);
            }
        }
        return (modelings);
    }

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
