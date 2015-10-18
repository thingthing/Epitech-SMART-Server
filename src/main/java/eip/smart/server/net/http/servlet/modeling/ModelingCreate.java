package eip.smart.server.net.http.servlet.modeling;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.cscommons.model.ServerStatus;
import eip.smart.server.Server;
import eip.smart.server.net.http.servlet.JsonServlet;
import eip.smart.server.util.exception.ModelingAlreadyExistsException;
import eip.smart.server.util.exception.StatusException;

/**
 * <b>The servlet ModelingCreate take a name as parameter and create a modeling with this name.</b>
 *
 * @author Pierre Demessence
 */

@WebServlet(urlPatterns = { "/modeling_create" }, initParams = { @WebInitParam(name = "name", value = "") })
public class ModelingCreate extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, JsonGenerator json) throws ServletException, IOException, StatusException {
		String name = JsonServlet.getParameter(request, "name");
		try {
			Server.getServer().getModelingManager().modelingCreate(name);
		} catch (ModelingAlreadyExistsException e) {
			throw new StatusException(ServerStatus.DUPLICATE.addObjects("modeling", "name", name));
		}
	}
}
