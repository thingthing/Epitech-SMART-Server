package eip.smart.server.net.http.servlet.modeling;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.cscommons.model.JSONViews;
import eip.smart.server.Server;
import eip.smart.server.net.http.servlet.JsonServlet;

/**
 * <b>The servlet ModelingList return the list of the modelings saved in the server.</b>
 *
 * @author Pierre Demessence
 */

@WebServlet(urlPatterns = { "/modeling_list" })
public class ModelingList extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, JsonGenerator json) throws ServletException, IOException {
		json.writeFieldName("modelings");
		this.mapper.writerWithView(JSONViews.HTTP.class).writeValue(json, Server.getServer().getModelingManager().getModelings());
	}
}
