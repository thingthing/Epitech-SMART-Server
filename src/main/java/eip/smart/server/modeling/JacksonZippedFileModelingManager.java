package eip.smart.server.modeling;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eip.smart.model.Modeling;
import eip.smart.model.proxy.FileModelingProxy;
import eip.smart.model.proxy.SimpleModelingProxy;

/**
 * This implementation of the FileModelingManager uses a compression method to reduce size taken by files.
 * Uses JSON serialization with Jackson implementation.
 */
public class JacksonZippedFileModelingManager extends FileModelingManager {

    protected int compressionLevel = -1;

    @Override
    public Modeling load(String name) {
        name = JacksonZippedFileModelingManager.addExtension(name);
        Modeling modeling = null;
        if (!this.exists(name))
            return (null);
        File file = new File(JacksonZippedFileModelingManager.DEFAULT_DIR, name);
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
        File file = new File(JacksonZippedFileModelingManager.DEFAULT_DIR, JacksonZippedFileModelingManager.addExtension(modeling.getName()));
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
