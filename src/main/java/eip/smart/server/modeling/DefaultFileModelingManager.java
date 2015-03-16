package eip.smart.server.modeling;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;

import eip.smart.model.Modeling;


import eip.smart.model.proxy.FileModelingProxy;

public class DefaultFileModelingManager extends FileModelingManager {

	@Override
	public Modeling load(String name) {
		name = DefaultFileModelingManager.addExtension(name);
		
		if (!this.exists(name))
			return (null);
		File file = new File(DefaultFileModelingManager.DEFAULT_DIR, name);
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		Modeling modeling = null;
        LOGGER.log(Level.INFO, "Loading modelisation at " + file.getAbsolutePath());

		try {
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			
			//modeling = (Modeling) ois.readObject();
			
			FileModelingProxy tmpModeling = (FileModelingProxy) ois.readObject();
			modeling = new Modeling(tmpModeling);
			
			ois.close();
			fis.close();
		} catch (InvalidClassException e) {
			e.printStackTrace();
			LOGGER.log(Level.WARNING, "Saved modelisation (" + name + ") is obsolete and will be ignored.");
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
		File file = new File(DefaultFileModelingManager.DEFAULT_DIR, DefaultFileModelingManager.addExtension(modeling.getName()));
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		
		try {
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			FileModelingProxy modelingParsed = new FileModelingProxy(modeling);
			oos.writeObject(modelingParsed);
		} catch (IOException e) {
			e.printStackTrace();
            return ;
		} finally {
            if (oos != null) {
                try {
                    oos.close();
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
        LOGGER.log(Level.INFO, "Successfully saved modelisation at " + file.getAbsolutePath());
	}
}
