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

public class NewFileModelingManager extends ModelingManager {

	private static String addExtension(String name) {
		String res = name;
		if (!res.matches(".*\\.modeling$"))
			res += NewFileModelingManager.EXTENSION;
		return (res);
	}

	private final static Logger	LOGGER		= Logger.getLogger(Modeling.class.getName());
	private static File			DIR			= new File(new File(System.getProperty("catalina.base")).getAbsolutePath(), "modelings");

	private static String		EXTENSION	= ".modeling";

	public NewFileModelingManager() {
		NewFileModelingManager.DIR.mkdirs();
	}

	@Override
	public boolean delete(String name) {
		name = NewFileModelingManager.addExtension(name);
		if (!this.exists(name))
			return (false);
		File file = new File(NewFileModelingManager.DIR, name);
		file.delete();
		return (true);
	}

	@Override
	public boolean exists(String name) {
		name = NewFileModelingManager.addExtension(name);
		File file = new File(NewFileModelingManager.DIR, name);
		return (file.exists());
	}

	@Override
	public ArrayList<SimpleModelingProxy> list() {
		ArrayList<SimpleModelingProxy> modelings = new ArrayList<>();
		for (File file : NewFileModelingManager.DIR.listFiles()) {
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
		name = NewFileModelingManager.addExtension(name);
		
		if (!this.exists(name))
			return (null);
		File file = new File(NewFileModelingManager.DIR, name);
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		Modeling modeling = null;

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
			NewFileModelingManager.LOGGER.log(Level.WARNING, "Saved modelisation (" + name + ") is obsolete and will be ignored.");
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
		File file = new File(NewFileModelingManager.DIR, NewFileModelingManager.addExtension(modeling.getName()));
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		
		try {
			
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			FileModelingProxy modelingParsed = new FileModelingProxy(modeling);
			//oos.writeObject(modeling);
			oos.writeObject(modelingParsed);
			
			oos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (oos != null)
				oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (fos != null)
				fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
