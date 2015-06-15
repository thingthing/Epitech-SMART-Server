package eip.smart.server.servlet.debug;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.model.agent.Agent;
import eip.smart.server.Server;
import eip.smart.server.exception.StatusException;
import eip.smart.server.servlet.JsonServlet;

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
			if (Server.getServer().getIoAgentContainer().getByAgentName("TestAgent#" + i) != null)
				++nb;
			else
				Server.getServer().getIoAgentContainer().addAgent(new Agent("TestAgent#" + i));
	}
}
