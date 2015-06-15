package eip.smart.server.servlet.debug;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.model.agent.Agent;
import eip.smart.server.Server;
import eip.smart.server.servlet.JsonServlet;

/**
 * Servlet implementation class GetAgentInfo
 */
@WebServlet(urlPatterns = { "/delete_test_agents" })
public class DeleteTestAgents extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException {
		for (Agent a : Server.getServer().getAgentsAvailable())
			if (a.getName().startsWith("TestAgent#"))
				Server.getServer().getIoAgentContainer().getByAgent(a).removeAgent();
	}
}
