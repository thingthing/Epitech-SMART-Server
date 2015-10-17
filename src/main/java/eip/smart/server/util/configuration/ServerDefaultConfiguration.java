package eip.smart.server.util.configuration;

import eip.smart.cscommons.configuration.DefaultConfiguration;

public enum ServerDefaultConfiguration implements DefaultConfiguration {
	AGENTS_MAX_POS("50", "server"),
	DEV_MAX_TICK("300", "server"),
	LOCATION_MODELING(LocationManager.LOCATION_MODELINGS, "location"),
	LOGGING_BRIDGE("FALSE", "logging"),
	LOGGING_SLACK("FALSE", "logging"),
	LOOP_DELAY("1000", "server"),
	MODE("DEVELOPPEMENT", "server"),
	TCP_PORT("4200", "server"),
	UDP_PORT("4300", "server");

	private String	defaut_value;
	private String	file;

	private ServerDefaultConfiguration(String default_value, String file) {
		this.defaut_value = default_value;
		this.file = file;
	}

	@Override
	public String getFile() {
		return (this.file);
	}

	@Override
	public String getKey() {
		return (this.name());
	}

	@Override
	public String getValue() {
		return (this.defaut_value);
	}
}
