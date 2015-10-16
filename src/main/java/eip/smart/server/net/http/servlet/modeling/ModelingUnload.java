package eip.smart.server.net.http.servlet.modeling;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.cscommons.model.ServerStatus;
import eip.smart.server.Server;
import eip.smart.server.net.http.servlet.JsonServlet;
import eip.smart.server.util.exception.StatusException;

/**
 * <b>The servlet ModelingStop stop and save the current modeling.</b>
 *
 * @author Pierre Demessence
 */

@WebServlet("/modeling_unload")
public class ModelingUnload extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException, StatusException {
		if (Server.getServer().getModelingManager().getCurrentModeling() == null)
			throw new StatusException(ServerStatus.MODELING_NO_CURRENT);
		Server.getServer().getModelingManager().modelingUnload();
	}

}
