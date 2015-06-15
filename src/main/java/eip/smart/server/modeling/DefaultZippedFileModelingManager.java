package eip.smart.server.modeling;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import eip.smart.model.Modeling;
import eip.smart.model.proxy.FileModelingProxy;

/**
 * This implementation of the FileModelingManager uses a compression method to reduce size taken by files.
 * Uses Java serialization.
 */
public class DefaultZippedFileModelingManager extends FileModelingManager {

    protected int compressionLevel = -1;

    @Override
    public Modeling load(String name) {
        name = DefaultZippedFileModelingManager.addExtension(name);

        if (!this.exists(name))
            return (null);
        File file = new File(DefaultZippedFileModelingManager.DEFAULT_DIR, name);
        FileInputStream fis = null;
        ZipInputStream zis = null;
        ObjectInputStream ois = null;
        Modeling modeling = null;

        try {
            fis = new FileInputStream(file);
            zis = new ZipInputStream(fis);
            ois = new ObjectInputStream(zis);

            //modeling = (Modeling) ois.readObject();

            FileModelingProxy tmpModeling = (FileModelingProxy) ois.readObject();
            modeling = new Modeling(tmpModeling);

            ois.close();
            fis.close();
        } catch (InvalidClassException e) {
            e.printStackTrace();
            DefaultZippedFileModelingManager.LOGGER.log(Level.WARNING, "Saved modelisation (" + name + ") is obsolete and will be ignored.");
            return (null);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
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
        File file = new File(DefaultZippedFileModelingManager.DEFAULT_DIR, DefaultZippedFileModelingManager.addExtension(modeling.getName()));
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        ZipOutputStream zos = null;

        try {

            fos = new FileOutputStream(file);
            zos = new ZipOutputStream(fos);
            if (compressionLevel >= 0 && compressionLevel <= 9)
                zos.setLevel(compressionLevel);
            oos = new ObjectOutputStream(fos);
            FileModelingProxy modelingParsed = new FileModelingProxy(modeling);
            oos.writeObject(modelingParsed);
            zos.finish();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
    }

    public int getCompressionLevel() {
        return compressionLevel;
    }

    public void setCompressionLevel(int compressionLevel) {
        this.compressionLevel = compressionLevel;
    }
}
