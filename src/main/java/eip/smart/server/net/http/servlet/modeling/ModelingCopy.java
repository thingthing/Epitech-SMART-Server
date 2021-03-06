package eip.smart.server.net.http.servlet.modeling;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.cscommons.model.ServerStatus;
import eip.smart.server.Server;
import eip.smart.server.net.http.servlet.JsonServlet;
import eip.smart.server.util.exception.ModelingAlreadyExistsException;
import eip.smart.server.util.exception.ModelingNotFoundException;
import eip.smart.server.util.exception.StatusException;

/**
 * <b>The servlet ModelingDelete take a name as parameter and delete the modeling with this name.</b>
 *
 * @author Pierre Demessence
 */

@WebServlet(urlPatterns = { "/modeling_copy" }, initParams = { @WebInitParam(name = "name", value = ""), @WebInitParam(name = "copy", value = "") })
public class ModelingCopy extends JsonServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @throws StatusException
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, JsonGenerator json) throws ServletException, IOException, StatusException {
		String name = JsonServlet.getParameter(request, "name");
		String copy = JsonServlet.getParameter(request, "copy");
		try {
			Server.getServer().getModelingManager().getModelingSaver().copy(name, copy);
		} catch (ModelingAlreadyExistsException e) {
			throw new StatusException(ServerStatus.DUPLICATE.addObjects("modeling", "name", e.getName()));
		} catch (ModelingNotFoundException e) {
			throw new StatusException(ServerStatus.NOT_FOUND.addObjects("modeling", "name", e.getName()));
		}
	}
}
