package eip.smart.server.modeling;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import eip.smart.model.Modeling;
import eip.smart.model.proxy.SimpleModelingProxy;

public abstract class FileModelingManager implements ModelingManager {

    protected static String addExtension(String name) {
        String res = name;
        if (!res.matches(".*\\.modeling$"))
            res += FileModelingManager.EXTENSION;
        return (res);
    }

    protected final static Logger	LOGGER		= Logger.getLogger(Modeling.class.getName());
    public static final File        DEFAULT_DIR = getBaseDirectory();

    private static File getBaseDirectory() {
        try {
            return new File(new File(System.getProperty("catalina.base")).getAbsolutePath(), "modelings");
        } catch (NullPointerException e) { // We are not in Tomcat context, default save folder is current folder
            return new File(".");
        }
    }

    public static final String		EXTENSION	= ".modeling";

    public FileModelingManager() {
        FileModelingManager.DEFAULT_DIR.mkdirs();
    }

    @Override
    public boolean delete(String name) {
        name = FileModelingManager.addExtension(name);
        if (!this.exists(name))
            return (false);
        File file = new File(FileModelingManager.DEFAULT_DIR, name);
        file.delete();
        return (true);
    }

    @Override
    public boolean exists(String name) {
        name = FileModelingManager.addExtension(name);
        File file = new File(FileModelingManager.DEFAULT_DIR, name);
        return (file.exists());
    }

    @Override
    public ArrayList<SimpleModelingProxy> list() {
        ArrayList<SimpleModelingProxy> modelings = new ArrayList<>();
        for (File file : FileModelingManager.DEFAULT_DIR.listFiles()) {
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
