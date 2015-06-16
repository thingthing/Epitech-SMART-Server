package eip.smart.server.util;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class SlackFilter extends Filter<ILoggingEvent> {

	@Override
	public FilterReply decide(ILoggingEvent event) {
		if (new Configuration("server").getProperty("LOGGING_SLACK").equals("TRUE"))
			return FilterReply.ACCEPT;
		return FilterReply.DENY;
	}

}
