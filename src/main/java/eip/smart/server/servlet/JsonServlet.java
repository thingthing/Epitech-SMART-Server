package eip.smart.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class JsonServlet extends HttpServlet {

	public enum Status {
		SIMULATION(-2, "SIMULATION"),
		TODO(-1, "TODO"),
		OK(0, "ok"),
		MODELING_ALREADY_CURRENT("a modeling is already loaded"),
		MODELING_NO_CURRENT("no current modeling"),
		MODELING_DUPLICATE_NAME("modeling with given name already exist"),
		MODELING_NOT_FOUND("modeling with given name does not exist"),
		MODELING_ALREADY_RUNNING("current modeling is already running"),
		MODELING_NOT_RUNNING("current modeling is not running"),
		MODELING_ALREADY_PAUSED("current modeling is already paused"),
		MODELING_NOT_PAUSED("current modeling is not paused"),
		AGENT_NOT_FOUND("agent with given id does not exist"),
		MODELING_NO_NAME("modeling must have a name");

		private int		code;
		private String	message;

		Status(int code, String message) {
			this.code = code;
			this.message = message;
		}

		Status(String message) {
			this(Integer.MIN_VALUE, message);
		}

		public int getCode() {
			if (this.code == Integer.MIN_VALUE)
				return (this.ordinal());
			return (this.code);
		}

		public String getMessage() {
			return (this.message);
		}
	}

	protected Status		status	= Status.OK;

	protected ObjectMapper	mapper	= new ObjectMapper();

	@SuppressWarnings("resource")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			JsonGenerator json = new JsonFactory().createGenerator(resp.getWriter());
			json.writeStartObject();
			json.writeFieldName("data");
			json.writeStartObject();

			this.status = Status.OK;
			this.doGet(req, resp, json);

			json.writeEndObject();
			json.writeNumberField("status", this.status.getCode());
			json.writeStringField("message", this.status.getMessage());
			json.writeEndObject();
			json.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected abstract void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException;
}