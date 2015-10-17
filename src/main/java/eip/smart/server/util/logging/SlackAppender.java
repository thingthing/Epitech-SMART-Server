package eip.smart.server.util.logging;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class SlackAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

	@SuppressWarnings({ "hiding" })
	class SlackAttachment {

		class Field implements JsonSerializable {
			public String	title;
			public String	value;

			public Field(String title, String value) {
				this.title = title;
				this.value = value;
			}

			@Override
			public void serialize(JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
				jgen.writeStartObject();
				jgen.writeObjectField("title", this.title);
				jgen.writeObjectField("value", this.value);
				jgen.writeObjectField("short", true);
				jgen.writeEndObject();
			}

			@Override
			public void serializeWithType(JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonProcessingException {}
		}

		public String			author_icon	= "http://blogs.thesitedoctor.co.uk/tim/images/616c06524d21_ABF1/error2.png";
		public String			author_link	= "http://54.148.17.11:8080";
		public String			author_name;
		public String			color		= "#FF0000";
		public String			fallback;
		public ArrayList<Field>	fields		= new ArrayList<>();
		public String[]			mrkdwn_in	= { "text" };
		public String			text;
		public String			title;
		public String			title_link	= "http://54.148.17.11:8080";

		public SlackAttachment(final ILoggingEvent evt) {
			this.fields.add(new Field("Class", evt.getCallerData()[0].getClassName()));
			this.fields.add(new Field("Method", evt.getCallerData()[0].getMethodName() + ":" + evt.getCallerData()[0].getLineNumber()));
			this.fields.add(new Field("Date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS").format(new Date(evt.getTimeStamp()))));
			this.fields.add(new Field("Thread", evt.getThreadName()));
			this.author_name = "Critical Server Error";
			this.fallback = this.author_name + "\n" + evt.getFormattedMessage();
			this.text = "*" + evt.getFormattedMessage() + "*\n";
			if (evt.getThrowableProxy() != null)
				this.text += "```" + this.getExceptionStackTrace(evt.getThrowableProxy()) + "```";
		}

		public String getExceptionStackTrace(IThrowableProxy ex) {
			String str = ex.getClassName() + ": " + ex.getMessage() + "\n";
			for (int i = 0; i < ex.getStackTraceElementProxyArray().length - ex.getCommonFrames(); i++) {
				StackTraceElementProxy trace = ex.getStackTraceElementProxyArray()[i];
				StackTraceElement ste = trace.getStackTraceElement();
				str += "\tat " + ste.getClassName() + "." + ste.getMethodName() + "(" + ste.getFileName() + ":" + ste.getLineNumber() + ") [" + trace.getClassPackagingData().getCodeLocation() + ":" + trace.getClassPackagingData().getVersion() + "]\n";
			}
			if (ex.getCommonFrames() != 0)
				str += "\t... " + ex.getCommonFrames() + " common frames omitted\n";
			if (ex.getCause() != null)
				str += "Caused by:" + this.getExceptionStackTrace(ex.getCause());
			return (str);
		}
	}

	private final static String	API_URL	= "https://slack.com/api/chat.postMessage";
	private String				channel;
	private String				token;
	private String				username;

	@Override
	protected void append(final ILoggingEvent evt) {
		try {
			final URL url = new URL(SlackAppender.API_URL);

			final StringWriter w = new StringWriter();
			w.append("token=").append(this.token).append("&");
			if (this.username != null)
				w.append("username=").append(URLEncoder.encode(this.username, "UTF-8")).append("&");
			w.append("icon_emoji=:computer:&");
			w.append("attachments=[");
			new ObjectMapper().writeValue(w, new SlackAttachment(evt));
			w.append("]&");
			if (this.channel != null)
				w.append("channel=").append(URLEncoder.encode(this.channel, "UTF-8")).append("&");

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

	public String getToken() {
		return this.token;
	}

	public Object getUsername() {
		return this.username;
	}

	public void setChannel(final String channel) {
		this.channel = channel;
	}

	public void setToken(final String token) {
		this.token = token;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
