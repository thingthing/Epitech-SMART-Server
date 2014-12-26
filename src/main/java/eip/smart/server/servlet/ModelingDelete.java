package eip.smart.server.servlet;

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

/**
 * Servlet implementation class ModelingLoad
 */
@WebServlet(urlPatterns = { "/modeling_delete" }, initParams = { @WebInitParam(name = "name", value = "") })
public class ModelingDelete extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, JsonGenerator json) throws ServletException, IOException {
		if (request.getParameter("name") == null || request.getParameter("name").equals(""))
			this.status = Status.MODELING_NO_NAME;
		else if (!Server.getServer().modelingDelete(request.getParameter("name")))
			this.status = Status.MODELING_NOT_FOUND;
	}
}
