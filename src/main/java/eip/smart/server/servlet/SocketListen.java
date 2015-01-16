package eip.smart.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.model.Status;
import eip.smart.server.Server;

/**
 * Servlet implementation class SocketListen
 */
@WebServlet(urlPatterns = { "/socket_listen" })
public class SocketListen extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException {

		if (Server.getServer().isAcceptorActive())
			this.status = Status.SOCKET_ALREADY_RUNNING;
		else
			try {
				Server.getServer().socketListen();
			} catch (IOException e) {
				this.status = Status.UNKNOWN;
			} catch (IllegalArgumentException e) {
				this.status = Status.PORT_BAD;
			}

	}
}
