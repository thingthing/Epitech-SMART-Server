package eip.smart.server.net.http.servlet.modeling;

import com.fasterxml.jackson.core.JsonGenerator;
import eip.smart.cscommons.model.ServerStatus;
import eip.smart.server.Server;
import eip.smart.server.net.http.servlet.JsonServlet;
import eip.smart.server.util.exception.ModelingNotFoundException;
import eip.smart.server.util.exception.ModelingObsoleteException;
import eip.smart.server.util.exception.StatusException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <b>The servlet ModelingLoad take a name as parameter and set the corresponding modeling as current modeling.</b>
 *
 * @author Pierre Demessence
 */

@WebServlet(urlPatterns = { "/modeling_load" }, initParams = { @WebInitParam(name = "name", value = "") })
public class ModelingLoad extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	/**
	 * @throws StatusException
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, JsonGenerator json) throws ServletException, IOException, StatusException {
		if (Server.getServer().getModelingManager().getCurrentModeling() != null)
			throw new StatusException(ServerStatus.MODELING_ALREADY_CURRENT);
		String name = JsonServlet.getParameter(request, "name");
		try {
			Server.getServer().getModelingManager().modelingLoad(name);
		} catch (ModelingNotFoundException e) {
			throw new StatusException(ServerStatus.NOT_FOUND.addObjects("modeling", "name", name));
		} catch (ModelingObsoleteException e) {
			JsonServlet.LOGGER.warn("Obsolete Modeling", e);
			throw new StatusException(ServerStatus.MODELING_OBSOLETE.addObjects(name));
		}

	}
}
