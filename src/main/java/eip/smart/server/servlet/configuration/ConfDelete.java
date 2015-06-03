package eip.smart.server.servlet.configuration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.model.Status;
import eip.smart.server.exception.StatusException;
import eip.smart.server.servlet.JsonServlet;
import eip.smart.server.util.Configuration;

/**
 * @author Vincent Buresi
 */
@WebServlet("/conf_delete")
public class ConfDelete extends JsonServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException, StatusException {
		String name = JsonServlet.getParameter(req, "name");
		if (!Configuration.confExists(name))
			throw new StatusException(Status.NOT_FOUND.addObjects("configuration", "name", name));
		Configuration.confDelete(name);
	}
}