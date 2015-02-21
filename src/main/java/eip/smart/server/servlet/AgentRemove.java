package eip.smart.server.servlet;

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

/**
 * Servlet implementation class GetAgentInfo
 */
@WebServlet(urlPatterns = { "/agent_remove" }, initParams = { @WebInitParam(name = "name", value = "") })
public class AgentRemove extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException {
		String name = req.getParameter("name");
		Agent agent = null;
		if (name != null) {
			ArrayList<Agent> agents = Server.getServer().getAgentsAvailable();
			for (Agent a : agents)
				if (name.equals(a.getName()))
					agent = a;
		}

		if (agent == null)
			this.status = Status.AGENT_NOT_FOUND;
		else if (Server.getServer().getCurrentModeling() == null)
			this.status = Status.MODELING_NO_CURRENT;
		else if (!Server.getServer().getCurrentModeling().getAgents().contains(agent))
			this.status = Status.AGENT_NOT_ADDED;
		else
			Server.getServer().getCurrentModeling().removeAgent(agent);
	}
}
