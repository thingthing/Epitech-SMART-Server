package eip.smart.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.model.Status;
import eip.smart.util.PointCloudGenerator;

/**
 * Servlet implementation class GetPoints
 */
@WebServlet("/get_points")
public class GetPoints extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, JsonGenerator json) throws ServletException, IOException {
		json.writeFieldName("points");
		this.mapper.writeValue(json, new PointCloudGenerator().generatePointCloud(50));

		this.status = Status.SIMULATION;
	}

}
