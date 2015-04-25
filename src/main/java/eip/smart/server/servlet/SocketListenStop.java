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
 * <b>The servlet SocketListenStop close the port and stop "listening" at it.</b>
 * @author Pierre Demessence
*/

@WebServlet(urlPatterns = { "/socket_listen_stop" })
public class SocketListenStop extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException {

		if (!Server.getServer().isAcceptorActive())
			this.status = Status.SOCKET_NOT_RUNNING;
		else
			Server.getServer().socketListenStop();

	}
}
