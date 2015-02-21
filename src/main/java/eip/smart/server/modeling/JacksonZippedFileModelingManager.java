package eip.smart.server.modeling;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eip.smart.model.Modeling;
import eip.smart.model.proxy.FileModelingProxy;
import eip.smart.model.proxy.SimpleModelingProxy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class JacksonZippedFileModelingManager extends ModelingManager {

    protected int compressionLevel = -1;

    private static String addExtension(String name) {
        String res = name;
        if (!res.matches(".*\\.modeling$"))
            res += JacksonZippedFileModelingManager.EXTENSION;
        return (res);
    }

    private final static Logger	LOGGER		= Logger.getLogger(Modeling.class.getName());
    private static File			DIR			= new File(new File(System.getProperty("catalina.base")).getAbsolutePath(), "modelings");

    private static String		EXTENSION	= ".modeling";

    public JacksonZippedFileModelingManager() {
        JacksonZippedFileModelingManager.DIR.mkdirs();
    }

    @Override
    public boolean delete(String name) {
        name = JacksonZippedFileModelingManager.addExtension(name);
        if (!this.exists(name))
            return (false);
        File file = new File(JacksonZippedFileModelingManager.DIR, name);
        file.delete();
        return (true);
    }

    @Override
    public boolean exists(String name) {
        name = JacksonZippedFileModelingManager.addExtension(name);
        File file = new File(JacksonZippedFileModelingManager.DIR, name);
        return (file.exists());
    }

    @Override
    public ArrayList<SimpleModelingProxy> list() {
        ArrayList<SimpleModelingProxy> modelings = new ArrayList<>();
        for (File file : JacksonZippedFileModelingManager.DIR.listFiles()) {
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
        name = JacksonZippedFileModelingManager.addExtension(name);
        Modeling modeling = null;
        if (!this.exists(name))
            return (null);
        File file = new File(JacksonZippedFileModelingManager.DIR, name);
        FileInputStream fis = null;
        ZipInputStream zis = null;
        LOGGER.log(Level.INFO, "Loading modelisation at " + file.getAbsolutePath());

        try {
            ObjectMapper mapper = new ObjectMapper();
            fis = new FileInputStream(file);
            zis = new ZipInputStream(fis);
            SimpleModelingProxy tmp = mapper.readValue(zis, SimpleModelingProxy.class);
            modeling = new Modeling();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zis != null) {
                try {
                    zis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return (modeling);

    }

    @Override
    public void save(Modeling modeling) {
        File file = new File(JacksonZippedFileModelingManager.DIR, JacksonZippedFileModelingManager.addExtension(modeling.getName()));
        FileOutputStream fos = null;
        ZipOutputStream zos = null;

        try {
            ObjectMapper mapper = new ObjectMapper();
            fos = new FileOutputStream(file);
            zos = new ZipOutputStream(fos);
            if (compressionLevel <= 9 && compressionLevel >= 0) {
                zos.setLevel(compressionLevel);
            }
            FileModelingProxy modelingParsed = new FileModelingProxy(modeling);
            mapper.writeValue(zos, modelingParsed);
            zos.finish();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        LOGGER.log(Level.INFO, "Saved modelisation at " + file.getAbsolutePath());
    }

    public int getCompressionLevel() {
        return compressionLevel;
    }

    public void setCompressionLevel(int compressionLevel) {
        this.compressionLevel = compressionLevel;
    }
}
