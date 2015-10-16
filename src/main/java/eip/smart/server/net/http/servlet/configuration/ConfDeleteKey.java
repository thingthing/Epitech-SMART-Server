package eip.smart.server.net.http.servlet.configuration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.cscommons.configuration.Configuration;
import eip.smart.cscommons.model.ServerStatus;
import eip.smart.server.net.http.servlet.JsonServlet;
import eip.smart.server.util.exception.StatusException;

/**
 * @author Vincent Buresi
 */
@WebServlet("/conf_delete_key")
public class ConfDeleteKey extends JsonServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException, StatusException {
        String name = JsonServlet.getParameter(req, "name");
        String key = JsonServlet.getParameter(req, "key");
        if (!Configuration.confExists(name))
            throw new StatusException(ServerStatus.NOT_FOUND.addObjects("configuration", "name", name));
        Configuration conf = new Configuration(name);
        conf.removeKey(key);
    }
}
