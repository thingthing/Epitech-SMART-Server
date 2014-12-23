package eip.smart.server.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.model.proxy.SimpleModelingProxy;
import eip.smart.server.Server;

/**
 * Servlet implementation class ModelingLoad
 */
@WebServlet(urlPatterns = { "/modeling_list" })
public class ModelingList extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, JsonGenerator json) throws ServletException, IOException {
		ArrayList<SimpleModelingProxy> modelings = Server.getServer().modelingList();
		json.writeFieldName("modelings");
		this.mapper.writeValue(json, modelings);
	}
}
