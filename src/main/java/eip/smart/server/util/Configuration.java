package eip.smart.server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 * Configuration class to store and load properties.
 *
 * @author Pierre Demessence
 *
 */
public class Configuration {
	/**
	 * Folder where config files will be stored.
	 */
	public static final String					CONFIG_DIR			= "config";

	/**
	 * Extension for config files.
	 */
	private static final String					CONFIG_EXTENSION	= ".xml";

	/**
	 * Default properties. One for each name.
	 */
	private static HashMap<String, Properties>	defaultProperties	= new HashMap<>();

	/**
	 * Checks if a configuration exists.
	 *
	 * @param name
	 *            The name of the configuration to check.
	 * @return true if the configuration file exists.
	 */
	public static boolean confExists(String name) {
		return (Configuration.defaultProperties.containsKey(name) || new File(Configuration.CONFIG_DIR, name + Configuration.CONFIG_EXTENSION).exists());
	}

    /**
     * Try to remove a configuration file.
     * NB: This method can (but should never) throw SecurityException, if this happens check the SecurityManager configuration.
     *
     * @param name
     *            The name of the configuration file to remove
     * @return true if the configuration file have been successfully removed
     */
    public static boolean confDelete(String name) {
        return (new File(CONFIG_DIR, name + CONFIG_EXTENSION).delete());
    }

	/**
	 * Get a list of all the configuration names.
	 *
	 * @return
	 */
	public static ArrayList<String> getConfigurations() {
		ArrayList<String> names = new ArrayList<>();
		for (String name : new File(Configuration.CONFIG_DIR).list())
			names.add(name.replaceAll(Configuration.CONFIG_EXTENSION, ""));
		return (names);
	}

	private static Properties getDefaultProperties(String name) {
		if (Configuration.defaultProperties.get(name) == null)
			Configuration.defaultProperties.put(name, new Properties());
		return (Configuration.defaultProperties.get(name));
	}

	/**
	 * Set a default property value for a specific configuration.
	 *
	 * @param name
	 * @param key
	 * @param value
	 */
	public static void setDefaultProperty(String name, String key, String value) {
		Configuration.getDefaultProperties(name).setProperty(key, value);
	}

	private File		configFile;

	private String		name;

	private Properties	properties;

	/**
	 * Create a new configuration with a name. If a configuration with this name already exist, it will be loaded.
	 * Otherwise, the default configuration will be loaded.
	 *
	 * @param name
	 *            The name of the configuration.
	 */
	public Configuration(String name) {
		this.name = name;
		this.properties = new Properties(Configuration.getDefaultProperties(name));
		this.configFile = new File(Configuration.CONFIG_DIR, name + Configuration.CONFIG_EXTENSION);
		new File(Configuration.CONFIG_DIR).mkdirs();
		this.load();
		this.save();
	}

	/**
	 * Print the current configuration to standard output.
	 */
	public void dump() {
		System.out.println("#ConfigurationDebugBegin#");
		if (Configuration.defaultProperties.get(this.name) != null)
			for (Entry<Object, Object> entry : Configuration.defaultProperties.get(this.name).entrySet())
				if (!this.keyExists(entry.getKey()))
					System.out.format("[%s] : %s (default value)\n", entry.getKey(), entry.getValue());
		for (Entry<Object, Object> entry : this.properties.entrySet())
			System.out.format("[%s] : %s\n", entry.getKey(), entry.getValue());
		System.out.println("#ConfigurationDebugEnd#");
	}

	/**
	 * @return
	 * @see java.util.Properties#stringPropertyNames()
	 */
	public Set<String> getKeys() {
		return this.properties.stringPropertyNames();
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Properties#getProperty(java.lang.String)
	 */
	public String getProperty(String key) {
		this.load();
		return (this.properties.getProperty(key));
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Properties#getProperty(java.lang.String)
	 */
	public int getPropertyInteger(String key) {
		return (Integer.parseInt(this.getProperty(key)));
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Hashtable#containsKey(java.lang.Object)
	 */
	public boolean keyExists(Object key) {
		return (Configuration.getDefaultProperties(this.name).containsKey(key) || this.properties.containsKey(key));
	}

	private void load() {
		if (this.configFile.exists()) {
			FileInputStream in = null;
			try {
				in = new FileInputStream(this.configFile);
				this.properties.loadFromXML(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	private void save() {
		try {
			FileOutputStream out = new FileOutputStream(this.configFile, false);
			this.properties.storeToXML(out, null);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set a new property and save to file.
	 *
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Properties#setProperty(java.lang.String, java.lang.String)
	 */
	public void setProperty(String key, String value) {
		this.properties.setProperty(key, value);
		this.save();
	}

    /**
     * Deletes a property and save to file.
     *
     * @param key
     * @return
     * @see java.util.Properties#remove(java.lang.Object)
     */
    public void removeKey(String key) {
        this.properties.remove(key);
        this.save();
    }
}
