package eip.smart.server.servlet.configuration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.model.Status;
import eip.smart.server.servlet.JsonServlet;
import eip.smart.server.util.Configuration;

/**
 * <b>The servlet ListStatus return the list of the status that can be returned.</b>
 *
 * @author Pierre Demessence
 */
@WebServlet("/conf_set")
public class ConfSet extends JsonServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException {
		String name = req.getParameter("name");
		String key = req.getParameter("key");
		String value = req.getParameter("value");
		if (name == null || name.isEmpty())
			this.status = Status.MISSING_PARAMETER.addObjects("name");
		else if (key == null || key.isEmpty())
			this.status = Status.MISSING_PARAMETER.addObjects("key");
		else if (value == null || value.isEmpty())
			this.status = Status.MISSING_PARAMETER.addObjects("value");
		else if (!Configuration.confExists(name))
			this.status = Status.NOT_FOUND.addObjects("configuration", "name", name);
		else {
			Configuration conf = new Configuration(name);
			conf.setProperty(key, value);
		}
	}

}
