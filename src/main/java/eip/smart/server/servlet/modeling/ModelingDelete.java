package eip.smart.server.servlet.modeling;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.model.Status;
import eip.smart.server.Server;
import eip.smart.server.exception.StatusException;
import eip.smart.server.servlet.JsonServlet;

/**
 * <b>The servlet ModelingDelete take a name as parameter and delete the modeling with this name.</b>
 *
 * @author Pierre Demessence
 */

@WebServlet(urlPatterns = { "/modeling_delete" }, initParams = { @WebInitParam(name = "name", value = "") })
public class ModelingDelete extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	/**
	 * @throws StatusException
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, JsonGenerator json) throws ServletException, IOException, StatusException {
		if (request.getParameter("name") == null || request.getParameter("name").equals(""))
			throw new StatusException(Status.MISSING_PARAMETER.addObjects("name"));
		else if (!Server.getServer().modelingDelete(request.getParameter("name")))
			throw new StatusException(Status.NOT_FOUND.addObjects("modeling", "name", request.getParameter("name")));
	}
}
