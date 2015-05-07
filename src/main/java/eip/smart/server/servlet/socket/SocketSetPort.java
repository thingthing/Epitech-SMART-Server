package eip.smart.server.servlet.socket;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.model.Status;
import eip.smart.server.servlet.JsonServlet;

/**
 * <b>The servlet SocketSetPort take as parameter a number and set it as the new socket's port.</b>
 *
 * @author Pierre Demessence
 */
@WebServlet(urlPatterns = { "/socket_set_port" }, initParams = { @WebInitParam(name = "port", value = "") })
public class SocketSetPort extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException {

		this.status = Status.ERR_REMOVED;

		/*
		int port = -1;

		if (Server.getServer().isAcceptorActive())
			this.status = Status.SOCKET_ALREADY_RUNNING;
		try {
			port = Integer.parseInt(req.getParameter("port"));
		} catch (NumberFormatException e) {}
		if (port <= 0)
			this.status = Status.PORT_NO_GIVEN;
		else if (!AvailablePortFinder.available(port))
			this.status = Status.PORT_ALREADY_USED;
		else
			Server.getServer().setPort(port);
			*/
	}
}
