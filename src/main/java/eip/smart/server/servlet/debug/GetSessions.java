package eip.smart.server.servlet.debug;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.mina.core.session.IoSession;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.server.Server;
import eip.smart.server.net.tcp.IoSessionProxy;
import eip.smart.server.servlet.JsonServlet;

/**
 * Servlet implementation class GetAgentsAvailable
 */
@WebServlet("/get_sessions")
public class GetSessions extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, JsonGenerator json) throws ServletException, IOException {
		ArrayList<IoSessionProxy> sessions = new ArrayList<>();
		for (IoSession session : Server.getServer().getSessions()) {
			sessions.add(new IoSessionProxy(session));
			if (Server.getServer().getIoAgentContainer().getBySession(session).getAgent() != null)
				sessions.get(sessions.size() - 1).setConnectedAgent(Server.getServer().getIoAgentContainer().getBySession(session).getAgent().getName());
		}

		json.writeFieldName("sessions");
		this.mapper.writeValue(json, sessions);
	}
}
