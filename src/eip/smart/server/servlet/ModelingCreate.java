package eip.smart.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.server.Server;

/**
 * Servlet implementation class ModelingCreate
 */
@WebServlet(urlPatterns = { "/modeling_create" }, initParams = { @WebInitParam(name = "name", value = "") })
public class ModelingCreate extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException {
		if (Server.getServer().getCurrentModeling() != null)
			this.status = Status.MODELING_ALREADY_CURRENT;
		else if (!Server.getServer().modelingCreate(req.getParameter("name")))
			this.status = Status.MODELING_DUPLICATE_NAME;
	}

}
