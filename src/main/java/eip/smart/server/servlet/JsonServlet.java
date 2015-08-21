package eip.smart.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import eip.smart.model.Status;
import eip.smart.server.Server;
import eip.smart.server.exception.StatusException;

public abstract class JsonServlet extends HttpServlet {
	private final static Logger	LOGGER	= LoggerFactory.getLogger(Server.class);

	protected static String getParameter(HttpServletRequest req, String name) throws StatusException {
		String param = req.getParameter(name);
		if (param == null || param.isEmpty())
			throw new StatusException(Status.MISSING_PARAMETER.addObjects(name));
		return (param);
	}

	protected ObjectMapper	mapper	= new ObjectMapper();

	protected Status		status	= Status.OK;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JsonServlet.LOGGER.info("GET request from {} on {}", req.getRemoteAddr(), req.getServletPath());
		try {
			JsonGenerator json = new JsonFactory().createGenerator(resp.getWriter());
			json.writeStartObject();

			json.writeFieldName("data");
			json.writeStartObject();
			this.status = Status.OK;
			try {
				this.doGet(req, resp, json);
			} catch (StatusException e) {
				this.status = e.getStatus();
			}
			json.writeEndObject();

			json.writeFieldName("status");
			json.writeStartObject();
			json.writeNumberField("code", this.status.getCode());
			json.writeStringField("message", this.status.getMessage());
			json.writeEndObject();

			json.writeEndObject();
			json.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JsonServlet.LOGGER.info("GET response from {} to {} with status {}", req.getServletPath(), req.getRemoteAddr(), this.status);
	}

	protected abstract void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException, StatusException;
}