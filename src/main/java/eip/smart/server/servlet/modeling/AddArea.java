package eip.smart.server.servlet.modeling;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import eip.smart.cscommons.model.ServerStatus;
import eip.smart.cscommons.model.modeling.Area;
import eip.smart.server.Server;
import eip.smart.server.exception.StatusException;
import eip.smart.server.model.modeling.ModelingLogic;
import eip.smart.server.servlet.JsonServlet;

/**
 * <b>The servlet AddArea take an Area as parameter and add it to the current Modeling.</b>
 *
 * @author Pierre Demessence
 */

@WebServlet(urlPatterns = { "/add_area" })
public class AddArea extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException, StatusException {
		Area area = null;
		if (JsonServlet.getParameter(req, "area") != null)
			try {
				area = new ObjectMapper().readValue(JsonServlet.getParameter(req, "area"), Area.class);
			} catch (Exception e) {}

		if (area == null)
			throw new StatusException(ServerStatus.MISSING_PARAMETER.addObjects("area"));
		ModelingLogic currentModeling = Server.getServer().getModelingManager().getCurrentModeling();
		if (currentModeling == null)
			throw new StatusException(ServerStatus.MODELING_NO_CURRENT);
		currentModeling.addArea(area);
	}
}
