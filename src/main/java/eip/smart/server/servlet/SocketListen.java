package eip.smart.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.model.Status;

/**
 * <b>The servlet SocketListen open the port and start "listening" at it.</b>
 *
 * @author Pierre Demessence
 */

@WebServlet(urlPatterns = { "/socket_listen" })
public class SocketListen extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException {

		this.status = Status.ERR_REMOVED;
		/*
		if (Server.getServer().isAcceptorActive())
			this.status = Status.SOCKET_ALREADY_RUNNING;
		else
			try {
				Server.getServer().socketListen();
			} catch (IOException e) {
				this.status = Status.ERR_UNKNOWN;
			} catch (IllegalArgumentException e) {
				this.status = Status.PORT_BAD;
			}
		*/
	}
}
