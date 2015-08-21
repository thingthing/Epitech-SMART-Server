package eip.smart.server.servlet.socket;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.cscommons.model.ServerStatus;
import eip.smart.server.Server;
import eip.smart.server.exception.StatusException;
import eip.smart.server.servlet.JsonServlet;

/**
 * Restart the TCP and UDP socket.
 *
 * @author Pierre Demessence
 */

@WebServlet(urlPatterns = { "/socket_restart" })
public class SocketRestart extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException, StatusException {
		try {
			Server.getServer().socketTCPListenStop();
			Server.getServer().socketUDPListenStop();
			Server.getServer().socketTCPListen();
			Server.getServer().socketUDPListen();
		} catch (IOException e) {
			throw new StatusException(ServerStatus.ERR_UNKNOWN.addObjects(e.getMessage()));
		} catch (IllegalArgumentException e) {
			throw new StatusException(ServerStatus.SOCKET_ERROR.addObjects("port out of range or unavaiable."));
		}

	}
}
