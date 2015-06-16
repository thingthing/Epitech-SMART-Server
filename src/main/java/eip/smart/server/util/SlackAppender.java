package eip.smart.server.util;

import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.LayoutBase;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

public class SlackAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

	private final static String		API_URL			= "https://slack.com/api/chat.postMessage";

	private String					token;

	private String					channel;

	private Layout<ILoggingEvent>	layout;

	private Layout<ILoggingEvent>	defaultLayout	= new LayoutBase<ILoggingEvent>() {

														@Override
														public String doLayout(ILoggingEvent event) {
															StringBuffer sbuf = new StringBuffer(128);
															sbuf.append("-- ");
															sbuf.append("[");
															sbuf.append(event.getLevel());
															sbuf.append("]");
															sbuf.append(event.getLoggerName());
															sbuf.append(" - ");
															sbuf.append(event.getFormattedMessage().replaceAll("\n", "\n\t"));
															return sbuf.toString();
														}

													};

	@Override
	protected void append(final ILoggingEvent evt) {
		try {
			final URL url = new URL(SlackAppender.API_URL);

			final StringWriter w = new StringWriter();
			w.append("token=").append(this.token).append("&");
			if (this.channel != null)
				w.append("channel=").append(URLEncoder.encode(this.channel, "UTF-8")).append("&");
			if (this.layout != null)
				w.append("text=").append(URLEncoder.encode(this.layout.doLayout(evt), "UTF-8"));
			else
				w.append("text=").append(URLEncoder.encode(this.defaultLayout.doLayout(evt), "UTF-8"));

			final byte[] bytes = w.toString().getBytes("UTF-8");

			final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setFixedLengthStreamingMode(bytes.length);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			final OutputStream os = conn.getOutputStream();
			os.write(bytes);

			os.flush();
			os.close();

		} catch (Exception ex) {
			ex.printStackTrace();
			this.addError("Error to post log to Slack.com (" + this.channel + "): " + evt, ex);
		}
	}

	public String getChannel() {
		return this.channel;
	}

	public Layout<ILoggingEvent> getLayout() {
		return this.layout;
	}

	public String getToken() {
		return this.token;
	}

	public void setChannel(final String channel) {
		this.channel = channel;
	}

	public void setLayout(final Layout<ILoggingEvent> layout) {
		this.layout = layout;
	}

	public void setToken(final String token) {
		this.token = token;
	}

}
