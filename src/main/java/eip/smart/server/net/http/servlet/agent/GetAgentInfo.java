package eip.smart.server.net.http.servlet.agent;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.cscommons.model.JSONViews;
import eip.smart.cscommons.model.ServerStatus;
import eip.smart.server.Server;
import eip.smart.server.model.agent.AgentLogic;
import eip.smart.server.net.http.servlet.JsonServlet;
import eip.smart.server.util.exception.StatusException;

/**
 * <b>The servlet GetAgentInfo take an agent's name as parameter and return informations about the corresponding Agent.</b>
 *
 * @author Pierre Demessence
 */

@WebServlet(urlPatterns = { "/get_agent_info" }, initParams = { @WebInitParam(name = "name", value = "") })
public class GetAgentInfo extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException, StatusException {
		String name = JsonServlet.getParameter(req, "name");
		AgentLogic agent = Server.getServer().getAgentManager().getAgentByName(name);
		if (agent == null)
			throw new StatusException(ServerStatus.NOT_FOUND.addObjects("agent", "name", name));
		json.writeFieldName("agent");
		this.mapper.writerWithView(JSONViews.HTTP.class).writeValue(json, agent);
	}
}
