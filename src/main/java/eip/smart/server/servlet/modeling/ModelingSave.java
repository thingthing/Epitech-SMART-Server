package eip.smart.server.servlet.modeling;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.cscommons.model.ServerStatus;
import eip.smart.server.Server;
import eip.smart.server.exception.StatusException;
import eip.smart.server.model.modeling.ModelingLogic;
import eip.smart.server.servlet.JsonServlet;

/**
 * <b>The servlet ModelingSave save the current modeling.</b>
 *
 * @author Pierre Demessence
 */

@WebServlet(urlPatterns = { "/modeling_save" })
public class ModelingSave extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	/**
	 * @throws StatusException
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, JsonGenerator json) throws ServletException, IOException, StatusException {
		ModelingLogic currentModeling = Server.getServer().getModelingManager().getCurrentModeling();
		if (currentModeling == null)
			throw new StatusException(ServerStatus.MODELING_NO_CURRENT);
		Server.getServer().getModelingManager().getModelingSaver().save(currentModeling);
	}
}
