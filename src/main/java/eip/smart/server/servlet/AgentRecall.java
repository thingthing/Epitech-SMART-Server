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
@WebServlet(urlPatterns = { "/agent_recall" }, initParams = { @WebInitParam(name = "id", value = "") })
public class AgentRecall extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException {
		int id = -1;
		try {
			id = Integer.parseInt(req.getParameter("id"));
		} catch (NumberFormatException e) {}
		ArrayList<Agent> agents = Server.getServer().getAgentsAvailable();
		Agent agent = null;
		for (Agent a : agents)
			if (a.getID() == id)
				agent = a;

		if (agent == null)
			this.status = Status.AGENT_NOT_FOUND;
		else if (Server.getServer().getCurrentModeling() == null)
			this.status = Status.MODELING_NO_CURRENT;
		else {
			this.status = Status.TODO;
			agent.recall();
		}
	}
}