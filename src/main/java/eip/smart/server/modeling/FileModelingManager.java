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
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import eip.smart.model.Modeling;
import eip.smart.model.proxy.SimpleModelingProxy;


import eip.smart.model.proxy.FileModelingProxy;

public abstract class FileModelingManager implements ModelingManager {

    protected static String addExtension(String name) {
        String res = name;
        if (!res.matches(".*\\.modeling$"))
            res += FileModelingManager.EXTENSION;
        return (res);
    }

    protected final static Logger	LOGGER		= Logger.getLogger(Modeling.class.getName());
    protected static File			DIR			= new File(new File(System.getProperty("catalina.base")).getAbsolutePath(), "modelings");

    private static String		EXTENSION	= ".modeling";

    public FileModelingManager() {
        FileModelingManager.DIR.mkdirs();
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

    public abstract Modeling load(String name);
    public abstract void save(Modeling modeling);
}
