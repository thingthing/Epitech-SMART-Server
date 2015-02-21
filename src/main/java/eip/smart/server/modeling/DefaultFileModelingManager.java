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
import eip.smart.model.Modeling;
import eip.smart.model.proxy.SimpleModelingProxy;


import eip.smart.model.proxy.FileModelingProxy;

public class DefaultFileModelingManager extends ModelingManager {

	private static String addExtension(String name) {
		String res = name;
		if (!res.matches(".*\\.modeling$"))
			res += DefaultFileModelingManager.EXTENSION;
		return (res);
	}

	private final static Logger	LOGGER		= Logger.getLogger(Modeling.class.getName());
	private static File			DIR			= new File(new File(System.getProperty("catalina.base")).getAbsolutePath(), "modelings");

	private static String		EXTENSION	= ".modeling";

	public DefaultFileModelingManager() {
		DefaultFileModelingManager.DIR.mkdirs();
	}

	@Override
	public boolean delete(String name) {
		name = DefaultFileModelingManager.addExtension(name);
		if (!this.exists(name))
			return (false);
		File file = new File(DefaultFileModelingManager.DIR, name);
		file.delete();
		return (true);
	}

	@Override
	public boolean exists(String name) {
		name = DefaultFileModelingManager.addExtension(name);
		File file = new File(DefaultFileModelingManager.DIR, name);
		return (file.exists());
	}

	@Override
	public ArrayList<SimpleModelingProxy> list() {
		ArrayList<SimpleModelingProxy> modelings = new ArrayList<>();
		for (File file : DefaultFileModelingManager.DIR.listFiles()) {
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
		name = DefaultFileModelingManager.addExtension(name);
		
		if (!this.exists(name))
			return (null);
		File file = new File(DefaultFileModelingManager.DIR, name);
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
			DefaultFileModelingManager.LOGGER.log(Level.WARNING, "Saved modelisation (" + name + ") is obsolete and will be ignored.");
			return (null);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			if (ois != null)
				ois.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (fis != null)
				fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (modeling);
	}

	@Override
	public void save(Modeling modeling) {
		File file = new File(DefaultFileModelingManager.DIR, DefaultFileModelingManager.addExtension(modeling.getName()));
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
