package eip.smart.server.net.http.servlet.modeling;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.cscommons.model.ServerStatus;
import eip.smart.server.Server;
import eip.smart.server.net.http.servlet.JsonServlet;
import eip.smart.server.util.exception.StatusException;

/**
 * <b>The servlet ModelingSave save the current modeling.</b>
 *
 * @author Pierre Demessence
 */

@WebServlet(urlPatterns = { "/modeling_save" })
public class ModelingSave extends JsonServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @throws StatusException
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, JsonGenerator json) throws ServletException, IOException, StatusException {
		if (Server.getServer().getModelingManager().getCurrentModeling() == null)
			throw new StatusException(ServerStatus.MODELING_NO_CURRENT);
		Server.getServer().getModelingManager().modelingSave();
	}
}
