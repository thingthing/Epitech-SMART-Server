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
 * <b>The servlet ListStatus return the list of the status that can be returned.</b>
 *
 * @author Pierre Demessence
 */
@WebServlet("/conf_set")
public class ConfSet extends JsonServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException, StatusException {
        boolean remove = false;
		String name = JsonServlet.getParameter(req, "name");
		String key = JsonServlet.getParameter(req, "key");
        String value = null;
        try {
            value = JsonServlet.getParameter(req, "value");
        } catch (StatusException e) {
            if (e.getStatus() == Status.MISSING_PARAMETER) {
                remove = true;
            } else {
                throw e;
            }
        }
		if (!Configuration.confExists(name))
			throw new StatusException(Status.NOT_FOUND.addObjects("configuration", "name", name));
		Configuration conf = new Configuration(name);
		if (remove) {
            conf.removeKey(key);
        } else {
            conf.setProperty(key, value);
        }
	}

}
