package eip.smart.server.net.http.servlet.debug;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.cscommons.model.ServerStatus;
import eip.smart.server.net.http.servlet.JsonServlet;

/**
 * <b>The servlet ListStatus return the list of the status that can be returned.</b>
 * 
 * @author Pierre Demessence
 */

@WebServlet("/list_status")
public class ListStatus extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException {
		json.writeFieldName("statuses");
		this.mapper.writeValue(json, ServerStatus.values());
	}

}
