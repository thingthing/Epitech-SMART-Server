package eip.smart.server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import eip.smart.util.Pair;

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
	public static final String					CONFIG_DIR			= LocationManager.LOCATION_CONFIG;

	/**
	 * Extension for config files.
	 */
	private static final String					CONFIG_EXTENSION	= ".xml";

	/**
	 * Default properties. One for each name.
	 */
	private static HashMap<String, Properties>	defaultProperties	= new HashMap<>();

	/**
	 * Try to remove a configuration file.
	 * NB: This method can (but should never) throw SecurityException, if this happens check the SecurityManager configuration.
	 *
	 * @param name
	 *            The name of the configuration file to remove
	 * @return true if the configuration file have been successfully removed
	 */
	public static boolean confDelete(String name) {
		return (new File(Configuration.CONFIG_DIR, name + Configuration.CONFIG_EXTENSION).delete());
	}

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
	 * Get a list of all the configuration names.
	 *
	 * @return
	 */
	public static HashSet<String> getConfigurations() {
		HashSet<String> names = new HashSet<>();
		names.addAll(Configuration.defaultProperties.keySet());
		for (String name : new File(Configuration.CONFIG_DIR).list())
			names.add(name.replaceAll(Configuration.CONFIG_EXTENSION, ""));
		return (names);
	}

	private static Properties getDefaultProperties(String name) {
		if (Configuration.defaultProperties.get(name) == null)
			Configuration.defaultProperties.put(name, new Properties());
		return (Configuration.defaultProperties.get(name));
	}

	public static void initDefaultValues() {
		for (DefaultConfiguration defaultConf : DefaultConfiguration.values())
			Configuration.setDefaultProperty(defaultConf.getFile(), defaultConf.getKey(), defaultConf.getValue());
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
	 * @return
	 * @see java.util.Properties#stringPropertyNames()
	 */
	public Set<String> getKeys() {
		return this.properties.stringPropertyNames();
	}

	/**
	 * Print the current configuration to standard output.
	 */
	public ArrayList<Pair<String, String>> getProperties() {
		ArrayList<Pair<String, String>> list = new ArrayList<>();
		for (String key : this.getKeys())
			list.add(new Pair<>(key, this.getProperty(key)));
		return (list);
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
