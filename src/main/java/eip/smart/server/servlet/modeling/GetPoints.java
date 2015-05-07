package eip.smart.server.servlet.modeling;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.model.Modeling;
import eip.smart.model.Status;
import eip.smart.server.Server;
import eip.smart.server.exception.StatusException;
import eip.smart.server.servlet.JsonServlet;
import eip.smart.util.PointCloudGenerator;

/**
 * <b>The servlet GetPoints return the list of the new points of the current modeling.</b>
 *
 * @author Pierre Demessence
 */

@WebServlet("/get_points")
public class GetPoints extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	/**
	 * @throws StatusException
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, JsonGenerator json) throws ServletException, IOException, StatusException {
		Modeling modeling = Server.getServer().getCurrentModeling();
		if (modeling == null)
			throw new StatusException(Status.MODELING_NO_CURRENT);
		json.writeFieldName("points");
		this.mapper.writeValue(json, new PointCloudGenerator().generatePointCloud(50));
		throw new StatusException(Status.ERR_SIMULATION);
	}

}
