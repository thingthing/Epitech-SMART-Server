package eip.smart.server.net.http.servlet.debug;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.server.Server;
import eip.smart.server.model.agent.AgentLogic;
import eip.smart.server.net.http.servlet.JsonServlet;
import eip.smart.server.util.exception.StatusException;

/**
 * Servlet implementation class GetAgentInfo
 */
@WebServlet(urlPatterns = { "/create_test_agents" }, initParams = { @WebInitParam(name = "nb", value = "") })
public class CreateTestAgents extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException {
		int nb = 0;

		try {
			nb = Integer.parseInt(JsonServlet.getParameter(req, "nb"));
		} catch (StatusException e) {}
		nb = (nb < 0 ? 0 : nb);
		nb = (nb > 10 ? 10 : nb);

		for (int i = 1; i <= nb; i++)
			if (Server.getServer().getAgentManager().getIoAgentContainer().getByAgentName("TestAgent#" + i) != null)
				++nb;
			else
				Server.getServer().getAgentManager().getIoAgentContainer().addAgent(new AgentLogic("TestAgent#" + i));
	}
}
