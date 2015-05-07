package eip.smart.server.servlet.agent;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.model.Agent;
import eip.smart.model.Status;
import eip.smart.server.Server;
import eip.smart.server.exception.StatusException;
import eip.smart.server.servlet.JsonServlet;

/**
 * <b>The servlet AgentDelete take an agent's name as parameter and delete the corresponding Agent from the available agents' list of the server.</b>
 *
 * @author Pierre Demessence
 */

@WebServlet(urlPatterns = { "/agent_delete" }, initParams = { @WebInitParam(name = "name", value = "") })
public class AgentDelete extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException, StatusException {
		String name = req.getParameter("name");
		Agent agent = null;
		if (name != null) {
			ArrayList<Agent> agents = Server.getServer().getAgentsAvailable();
			for (Agent a : agents)
				if (name.equals(a.getName()))
					agent = a;
		}

		if (agent == null)
			throw new StatusException(Status.NOT_FOUND.addObjects("agent", "name", name));
		Server.getServer().getIoAgentContainer().getByAgent(agent).removeAgent();
	}
}
