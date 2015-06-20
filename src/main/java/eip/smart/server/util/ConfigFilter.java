package eip.smart.server.util;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class ConfigFilter extends Filter<ILoggingEvent> {

	private String	key;
	private String	value;

	@Override
	public FilterReply decide(ILoggingEvent event) {
		if (!this.isStarted())
			return FilterReply.NEUTRAL;
		if (this.key == null || this.value == null)
			return FilterReply.DENY;
		if (new Configuration("server").getProperty(this.key).equals(this.value))
			return FilterReply.NEUTRAL;
		return FilterReply.DENY;
	}

	public String getKey() {
		return this.key;
	}

	public String getValue() {
		return this.value;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
