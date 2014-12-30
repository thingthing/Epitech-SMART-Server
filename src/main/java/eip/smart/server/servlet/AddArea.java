package eip.smart.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import eip.smart.model.Area;
import eip.smart.model.Status;
import eip.smart.server.Server;

/**
 * Servlet implementation class GetAgentInfo
 */
@WebServlet(urlPatterns = { "/add_area" })
public class AddArea extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException {

		Area area = null;
		if (req.getParameter("area") != null)
			try {
				area = new ObjectMapper().readValue(req.getParameter("area"), Area.class);
			} catch (Exception e) {}

		if (area == null)
			this.status = Status.AREA_NO_GIVEN;
		else if (Server.getServer().getCurrentModeling() == null)
			this.status = Status.MODELING_NO_CURRENT;
		else
			Server.getServer().getCurrentModeling().addArea(area);
	}
}
