package eip.smart.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import eip.smart.model.Status;
import eip.smart.server.exception.StatusException;

public abstract class JsonServlet extends HttpServlet {

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
	}

	protected abstract void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException, StatusException;
}