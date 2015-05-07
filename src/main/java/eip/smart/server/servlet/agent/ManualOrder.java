package eip.smart.server.servlet.agent;

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
import eip.smart.server.exception.StatusException;
import eip.smart.server.servlet.JsonServlet;

/**
 * <b>The servlet ManualOrder take an agent's name and a Point as parameter set this Point as the new current goal of the corresponding Agent.</b>
 *
 * @author Pierre Demessence
 */

@WebServlet(urlPatterns = { "/manual_order" }, initParams = { @WebInitParam(name = "name", value = "") })
public class ManualOrder extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException, StatusException {
		String name = req.getParameter("name");
		Agent agent = null;
		if (name != null) {
			ArrayList<Agent> agents = Server.getServer().getAgentsAvailable();
			for (Agent a : agents)
				if (name.equals(a.getName()))
					agent = a;
		}

		Point order = null;
		if (req.getParameter("order") != null)
			try {
				order = new ObjectMapper().readValue(req.getParameter("order"), Point.class);
			} catch (IOException e) {}

		if (agent == null)
			throw new StatusException(Status.NOT_FOUND.addObjects("agent", "name", name));
		if (order == null)
			throw new StatusException(Status.MISSING_PARAMETER.addObjects("order"));

		agent.pushOrder(order);
	}
}
