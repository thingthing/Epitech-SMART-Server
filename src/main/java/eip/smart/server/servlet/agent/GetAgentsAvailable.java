package eip.smart.server.servlet.agent;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.model.Agent;
import eip.smart.model.proxy.SimpleAgentProxy;
import eip.smart.server.Server;
import eip.smart.server.servlet.JsonServlet;

/**
 * <b>The servlet GetAgentsAvailable return the list of the agents connected to the server and not already attributed to a modeling.</b>
 * @author Pierre Demessence
*/

@WebServlet("/get_agents_available")
public class GetAgentsAvailable extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, JsonGenerator json) throws ServletException, IOException {
		ArrayList<SimpleAgentProxy> agents = new ArrayList<>();
		for (Agent agent : Server.getServer().getAgentsAvailable())
			agents.add(agent.getProxy());

		json.writeFieldName("agents");
		this.mapper.writeValue(json, agents);
	}
}
