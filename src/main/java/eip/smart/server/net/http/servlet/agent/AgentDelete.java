package eip.smart.server.net.http.servlet.agent;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.cscommons.model.ServerStatus;
import eip.smart.server.Server;
import eip.smart.server.model.agent.AgentLogic;
import eip.smart.server.net.http.servlet.JsonServlet;
import eip.smart.server.util.exception.StatusException;

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
		String name = JsonServlet.getParameter(req, "name");
		AgentLogic agent = Server.getServer().getAgentManager().getAgentByName(name);
		if (agent == null)
			throw new StatusException(ServerStatus.NOT_FOUND.addObjects("agent", "name", name));
		Server.getServer().getAgentManager().getIoAgentContainer().getByAgent(agent).removeAgent();
	}
}
