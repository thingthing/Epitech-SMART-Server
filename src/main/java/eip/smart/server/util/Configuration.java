package eip.smart.server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
	public static boolean exists(String name) {
		return (new File(Configuration.CONFIG_DIR, name + Configuration.CONFIG_EXTENSION).exists());
	}

	/**
	 * Set a default property value for a specific configuration.
	 *
	 * @param name
	 * @param key
	 * @param value
	 */
	public static void setDefaultProperty(String name, String key, String value) {
		if (Configuration.defaultProperties.get(name) == null)
			Configuration.defaultProperties.put(name, new Properties());
		Configuration.defaultProperties.get(name).setProperty(key, value);
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
		this.properties = new Properties(Configuration.defaultProperties.get(name));
		this.configFile = new File(Configuration.CONFIG_DIR, name + Configuration.CONFIG_EXTENSION);
		new File(Configuration.CONFIG_DIR).mkdirs();
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

		this.save();
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Hashtable#containsKey(java.lang.Object)
	 */
	public boolean exists(Object key) {
		return (this.properties.containsKey(key));
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
		return (this.properties.getProperty(key));
	}

	/**
	 * Print the current configuration to standard output.
	 */
	public void printDebug() {
		System.out.println("#ConfigurationDebugBegin#");
		if (Configuration.defaultProperties.get(this.name) != null)
			for (Entry<Object, Object> entry : Configuration.defaultProperties.get(this.name).entrySet())
				if (!this.exists(entry.getKey()))
					System.out.format("[%s] : %s (default value)\n", entry.getKey(), entry.getValue());
		for (Entry<Object, Object> entry : this.properties.entrySet())
			System.out.format("[%s] : %s\n", entry.getKey(), entry.getValue());
		System.out.println("#ConfigurationDebugEnd#");
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
}
