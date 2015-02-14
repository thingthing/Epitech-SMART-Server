package eip.smart.server.modeling;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eip.smart.model.Modeling;
import eip.smart.model.proxy.SimpleModelingProxy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FileModelingManager extends ModelingManager {

    private static File         DIR			= new File(new File(System.getProperty("catalina.base")).getAbsolutePath(), "modelings");
    private static String		EXTENSION	= ".modeling";
    private final static Logger LOGGER		= Logger.getLogger(Modeling.class.getName());

    public FileModelingManager() {
        FileModelingManager.DIR.mkdirs();
    }

    private static String addExtension(String name) {
        String res = name;
        if (!res.matches(".*\\.modeling$"))
            res += FileModelingManager.EXTENSION;
        return (res);
    }


    @Override
    public boolean delete(String name) {
        name = FileModelingManager.addExtension(name);
        if (!this.exists(name))
            return (false);
        File file = new File(FileModelingManager.DIR, name);
        file.delete();
        return (true);
    }

    @Override
    public boolean exists(String name) {
        name = FileModelingManager.addExtension(name);
        File file = new File(FileModelingManager.DIR, name);
        return (file.exists());
    }

    @Override
    public ArrayList<SimpleModelingProxy> list() {
        ArrayList<SimpleModelingProxy> modelings = new ArrayList<>();
        for (File file : FileModelingManager.DIR.listFiles()) {
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
        name = FileModelingManager.addExtension(name);
        Modeling modeling = null;
        if (!this.exists(name))
            return (null);
        File file = new File(FileModelingManager.DIR, name);
        FileModelingManager.LOGGER.log(Level.INFO, "Loading modelisation at " + file.getAbsolutePath());
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
        File file = new File(FileModelingManager.DIR, FileModelingManager.addExtension(modeling.getName()));
        try {
            ObjectMapper mapper = new ObjectMapper();
            SimpleModelingProxy tmp = new SimpleModelingProxy(modeling);
            mapper.writeValue(file, tmp);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileModelingManager.LOGGER.log(Level.INFO, "Saved modelisation at " + file.getAbsolutePath());
    }
}
