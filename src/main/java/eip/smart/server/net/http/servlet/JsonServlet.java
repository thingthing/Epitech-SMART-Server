package eip.smart.server.net.http.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import eip.smart.cscommons.model.ServerStatus;
import eip.smart.server.Server;
import eip.smart.server.util.exception.StatusException;

public abstract class JsonServlet extends HttpServlet {
	private final static Logger	LOGGER	= LoggerFactory.getLogger(Server.class);

	protected static String getParameter(HttpServletRequest req, String name) throws StatusException {
		String param = req.getParameter(name);
		if (!JsonServlet.hasParameter(req, name))
			throw new StatusException(ServerStatus.MISSING_PARAMETER.addObjects(name));
		return (param);
	}

	protected static boolean hasParameter(HttpServletRequest req, String name) {
		String param = req.getParameter(name);
		if (param == null || param.isEmpty())
			return (false);
		return (true);
	}

	protected ObjectMapper	mapper	= new ObjectMapper();
	protected ServerStatus	status	= ServerStatus.OK;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
		this.mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		this.mapper.configure(MapperFeature.AUTO_DETECT_GETTERS, false);
		this.mapper.configure(MapperFeature.AUTO_DETECT_IS_GETTERS, false);
		this.mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		JsonServlet.LOGGER.trace("GET request from {} on {}", req.getRemoteAddr(), req.getServletPath());
		try {
			JsonGenerator json = new JsonFactory().createGenerator(resp.getWriter());
			json.setCodec(this.mapper);
			json.writeStartObject();

			json.writeFieldName("data");
			json.writeStartObject();
			this.status = ServerStatus.OK;
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
		JsonServlet.LOGGER.trace("GET response from {} to {} with status {}", req.getServletPath(), req.getRemoteAddr(), this.status);
	}

	protected abstract void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException, StatusException;
}