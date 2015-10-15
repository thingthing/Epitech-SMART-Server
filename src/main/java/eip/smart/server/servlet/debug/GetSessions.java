package eip.smart.server.servlet.debug;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.cscommons.model.JSONViews;
import eip.smart.server.Server;
import eip.smart.server.servlet.JsonServlet;

/**
 * Servlet implementation class GetAgentsAvailable
 */
@WebServlet("/get_sessions")
public class GetSessions extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, JsonGenerator json) throws ServletException, IOException {
		json.writeFieldName("sessions");
		this.mapper.writerWithView(JSONViews.HTTP.class).writeValue(json, Server.getServer().getAgentManager().getIoAgentContainer());
	}
}
