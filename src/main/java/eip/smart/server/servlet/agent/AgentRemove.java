package eip.smart.server.servlet.agent;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.cscommons.model.ServerStatus;
import eip.smart.server.Server;
import eip.smart.server.exception.StatusException;
import eip.smart.server.model.agent.AgentLogic;
import eip.smart.server.model.modeling.ModelingLogic;
import eip.smart.server.servlet.JsonServlet;

/**
 * <b>The servlet AgentRemove take an agent's name as parameter and delete the corresponding Agent from the agent's list attributed to the current modeling.</b>
 *
 * @author Pierre Demessence
 */

@WebServlet(urlPatterns = { "/agent_remove" }, initParams = { @WebInitParam(name = "name", value = "") })
public class AgentRemove extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException, StatusException {
		String name = JsonServlet.getParameter(req, "name");
		AgentLogic agent = Server.getServer().getAgentManager().getAgentByName(name);
		if (agent == null)
			throw new StatusException(ServerStatus.NOT_FOUND.addObjects("agent", "name", name));
		ModelingLogic currentModeling = Server.getServer().getModelingManager().getCurrentModeling();
		if (currentModeling == null)
			throw new StatusException(ServerStatus.MODELING_NO_CURRENT);
		if (!currentModeling.getAgents().contains(agent))
			throw new StatusException(ServerStatus.AGENT_NOT_ADDED);
		currentModeling.removeAgent(agent);
	}
}
