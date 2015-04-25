package eip.smart.server.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.model.Status;
import eip.smart.model.proxy.StatusProxy;

/**
 * <b>The servlet ListStatus return the list of the status that can be returned.</b>
 * @author Pierre Demessence
*/

@WebServlet("/list_status")
public class ListStatus extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException {
		ArrayList<StatusProxy> statuses = new ArrayList<>();
		for (Status status : Status.values())
			statuses.add(new StatusProxy(status));
		json.writeFieldName("statuses");
		this.mapper.writeValue(json, statuses);
	}

}
