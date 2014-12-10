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
		NO_CURRENT_MODELING(1, "no current modeling"),
		AGENT_NOT_FOUND(2, "agent does not exist");

		private int		code;
		private String	message;

		Status(int code, String message) {
			this.code = code;
			this.message = message;
		}

		public int getCode() {
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