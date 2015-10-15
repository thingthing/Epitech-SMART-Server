package eip.smart.server.servlet.debug;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.cscommons.model.modeling.Modeling;
import eip.smart.server.Server;
import eip.smart.server.model.agent.AgentLogic;
import eip.smart.server.servlet.JsonServlet;

/**
 * Servlet implementation class GetAgentInfo
 */
@WebServlet(urlPatterns = { "/clean_server" })
public class CleanServer extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException {
		for (AgentLogic a : Server.getServer().getAgentManager().getAgentsAvailable())
			Server.getServer().getAgentManager().getIoAgentContainer().getByAgent(a).removeAgent();
		for (Modeling m : Server.getServer().getModelingManager().getModelingSaver().list())
			Server.getServer().getModelingManager().getModelingSaver().delete(m.getName());
	}
}
