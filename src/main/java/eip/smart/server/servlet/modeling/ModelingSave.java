package eip.smart.server.servlet.modeling;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.model.Status;
import eip.smart.server.Server;
import eip.smart.server.servlet.JsonServlet;

/**
 * <b>The servlet ModelingSave save the current modeling.</b>
 * @author Pierre Demessence
*/

@WebServlet(urlPatterns = { "/modeling_save" })
public class ModelingSave extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, JsonGenerator json) throws ServletException, IOException {
		if (Server.getServer().getCurrentModeling() == null)
			this.status = Status.MODELING_NO_CURRENT;
		else
			Server.getServer().modelingSave();
	}
}