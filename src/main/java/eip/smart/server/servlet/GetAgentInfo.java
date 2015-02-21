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
@WebServlet(urlPatterns = { "/get_agent_info" }, initParams = { @WebInitParam(name = "name", value = "") })
public class GetAgentInfo extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException {
		String name = "";
		name = req.getParameter("name");
		ArrayList<Agent> agents = Server.getServer().getAgentsAvailable();
		Agent agent = null;
		for (Agent a : agents)
			if (name.equals(a.getName()))
				agent = a;

		if (agent == null)
			this.status = Status.AGENT_NOT_FOUND;
		else {
			json.writeFieldName("agent");
			// @ TODO Create a Proxy here ?
			this.mapper.writeValue(json, agent);
		}
	}
}
