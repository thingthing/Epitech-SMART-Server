package eip.smart.server.servlet.modeling;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.model.Status;
import eip.smart.server.Server;
import eip.smart.server.servlet.JsonServlet;

/**
 * <b>The servlet ModelingCreate take a name as parameter and create a modeling with this name.</b>
 * @author Pierre Demessence
*/

@WebServlet(urlPatterns = { "/modeling_create" }, initParams = { @WebInitParam(name = "name", value = "") })
public class ModelingCreate extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, JsonGenerator json) throws ServletException, IOException {
		if (Server.getServer().getCurrentModeling() != null)
			this.status = Status.MODELING_ALREADY_CURRENT;
		else if (request.getParameter("name") == null || request.getParameter("name").equals(""))
			this.status = Status.MODELING_NO_NAME;
		else if (!Server.getServer().modelingCreate(request.getParameter("name")))
			this.status = Status.MODELING_DUPLICATE_NAME;
	}

}
