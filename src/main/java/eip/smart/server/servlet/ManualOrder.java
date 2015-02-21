package eip.smart.server.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import eip.smart.model.Agent;
import eip.smart.model.Status;
import eip.smart.model.geometry.Point;
import eip.smart.server.Server;

/**
 * Servlet implementation class GetAgentInfo
 */
@WebServlet(urlPatterns = { "/manual_order" }, initParams = { @WebInitParam(name = "name", value = "") })
public class ManualOrder extends JsonServlet {
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

		Point order = null;
		if (req.getParameter("order") != null)
			try {
				order = new ObjectMapper().readValue(req.getParameter("order"), Point.class);
			} catch (IOException e) {}

		if (agent == null)
			this.status = Status.AGENT_NOT_FOUND;
		else if (order == null)
			this.status = Status.ORDER_NO_GIVEN;
		else
			agent.pushOrder(order);
	}
}
