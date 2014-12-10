package eip.smart.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.server.Server;

/**
 * Servlet implementation class ModelingStop
 */
@WebServlet("/modeling_stop")
public class ModelingStop extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException {
		if (Server.getServer().getCurrentModeling() == null)
			this.status = Status.MODELING_NO_CURRENT;
		else
			Server.getServer().modelingStop();
	}

}
