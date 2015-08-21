package eip.smart.server.util;

public enum DefaultConfiguration {
	LOGGING_SLACK("FALSE", "logging"),
	LOGGING_BRIDGE("FALSE", "logging"),
	LOCATION_MODELING(LocationManager.LOCATION_MODELINGS, "location"),
	TCP_PORT("4200", "server"),
	UDP_PORT("4300", "server");

	private String	defaut_value;
	private String	file;

	private DefaultConfiguration(String default_value, String file) {
		this.defaut_value = default_value;
		this.file = file;
	}

	public String getFile() {
		return (this.file);
	}

	public String getKey() {
		return (this.name());
	}

	public String getValue() {
		return (this.defaut_value);
	}
}
