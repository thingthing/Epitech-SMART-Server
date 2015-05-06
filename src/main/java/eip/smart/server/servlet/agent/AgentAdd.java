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
import eip.smart.server.servlet.JsonServlet;

/**
 * <b>The servlet AgentAdd take an agent's name as parameter and add the corresponding Agent from the current Modeling.</b>
 * 
 * @author Pierre Demessence
 */

@WebServlet(urlPatterns = { "/agent_add" }, initParams = { @WebInitParam(name = "name", value = "") })
public class AgentAdd extends JsonServlet {
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
			this.status = Status.NOT_FOUND.addObject("agent").addObject("name").addObject(name);
		else if (Server.getServer().getCurrentModeling() == null)
			this.status = Status.MODELING_NO_CURRENT;
		else if (Server.getServer().getCurrentModeling().getAgents().contains(agent))
			this.status = Status.AGENT_ALREADY_ADDED;
		else
			Server.getServer().getCurrentModeling().addAgent(agent);
	}
}
