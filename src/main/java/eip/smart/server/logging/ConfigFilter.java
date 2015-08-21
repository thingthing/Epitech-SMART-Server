package eip.smart.server.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import eip.smart.cscommons.configuration.Configuration;

public class ConfigFilter extends Filter<ILoggingEvent> {

	private String	file;
	private String	key;
	private String	value;

	@Override
	public FilterReply decide(ILoggingEvent event) {
		if (!this.isStarted())
			return FilterReply.NEUTRAL;
		if (this.file == null || this.key == null || this.value == null)
			return FilterReply.DENY;
		if (new Configuration(this.file).getProperty(this.key).equals(this.value))
			return FilterReply.NEUTRAL;
		return FilterReply.DENY;
	}

	public String getFile() {
		return this.file;
	}

	public String getKey() {
		return this.key;
	}

	public String getValue() {
		return this.value;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
