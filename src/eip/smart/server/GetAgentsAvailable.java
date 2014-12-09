package eip.smart.server;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import eip.smart.model.Agent;
import eip.smart.model.proxy.SimpleAgentProxy;

/**
 * Servlet implementation class GetAgentsAvailable
 */
@WebServlet("/get_agents_available")
public class GetAgentsAvailable extends HttpServlet {
	private static final long	serialVersionUID	= 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayList<SimpleAgentProxy> agents = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();

		for (Agent agent : Server.getServer().getAgentsAvaiable())
			agents.add(agent.getProxy());

		response.getWriter().println(objectMapper.writeValueAsString(agents));
	}
}
