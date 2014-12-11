package eip.smart.server.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.model.Agent;
import eip.smart.model.Modeling;
import eip.smart.model.proxy.SimpleAgentProxy;
import eip.smart.server.Server;

/**
 * Servlet implementation class GetAgents
 */
@WebServlet(urlPatterns = { "/get_agents" })
public class GetAgents extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, JsonGenerator json) throws ServletException, IOException {
		ArrayList<SimpleAgentProxy> agents = new ArrayList<>();
		Modeling currentModeling = Server.getServer().getCurrentModeling();
		if (currentModeling != null) {
			for (Agent agent : currentModeling.getAgents())
				agents.add(agent.getProxy());
			json.writeFieldName("agents");
			this.mapper.writeValue(json, agents);
		} else
			this.status = Status.MODELING_NO_CURRENT;
	}
}
