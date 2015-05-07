package eip.smart.server.servlet.socket;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.server.Server;
import eip.smart.server.servlet.JsonServlet;

/**
 * <b>The servlet SocketGetPort return the port of the Socket.</b>
 * @author Pierre Demessence
*/

@WebServlet(urlPatterns = { "/socket_get_port" })
public class SocketGetPort extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException {
		json.writeFieldName("port");
		this.mapper.writeValue(json, Server.getServer().getPort());
	}
}
