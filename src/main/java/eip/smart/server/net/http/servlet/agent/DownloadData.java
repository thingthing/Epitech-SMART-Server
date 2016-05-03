package eip.smart.server.net.http.servlet.agent;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import eip.smart.cscommons.model.ServerStatus;
import eip.smart.cscommons.model.geometry.Point3D;
import eip.smart.server.Server;
import eip.smart.server.model.agent.AgentLogic;
import eip.smart.server.net.http.servlet.JsonServlet;
import eip.smart.server.util.exception.StatusException;

/**
 * <b>The servlet DownloadData takes an agent's name and a Point as parameter and set this Point as the new current goal of the corresponding Agent.</b>
 *
 * @author Nicolas Thing-leoh
 */

@WebServlet(urlPatterns = { "/download" }, initParams = { @WebInitParam(name = "name", value = "") })
public class DownloadData extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException, StatusException {
		String name = JsonServlet.getParameter(req, "name");
		AgentLogic agent = Server.getServer().getAgentManager().getAgentByName(name);
		if (agent == null)
			throw new StatusException(ServerStatus.NOT_FOUND.addObjects("agent", "name", name));

		agent.sendDownloadOrder();
	}
}
