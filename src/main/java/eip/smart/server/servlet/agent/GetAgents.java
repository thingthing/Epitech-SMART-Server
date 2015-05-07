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
import eip.smart.model.Modeling;
import eip.smart.model.Status;
import eip.smart.model.proxy.SimpleAgentProxy;
import eip.smart.server.Server;
import eip.smart.server.exception.StatusException;
import eip.smart.server.servlet.JsonServlet;

/**
 * <b>The servlet GetAgents return the list of the agents attributed to the current modeling.</b>
 *
 * @author Pierre Demessence
 */

@WebServlet(urlPatterns = { "/get_agents" })
public class GetAgents extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	/**
	 * @throws StatusException
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, JsonGenerator json) throws ServletException, IOException, StatusException {
		ArrayList<SimpleAgentProxy> agents = new ArrayList<>();
		Modeling currentModeling = Server.getServer().getCurrentModeling();
		if (currentModeling == null)
			throw new StatusException(Status.MODELING_NO_CURRENT);
		for (Agent agent : currentModeling.getAgents())
			agents.add(agent.getProxy());
		json.writeFieldName("agents");
		this.mapper.writeValue(json, agents);
	}
}
