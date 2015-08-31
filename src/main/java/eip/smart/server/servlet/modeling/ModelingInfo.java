package eip.smart.server.servlet.modeling;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.cscommons.model.ServerStatus;
import eip.smart.cscommons.model.modeling.Modeling;
import eip.smart.server.Server;
import eip.smart.server.exception.StatusException;
import eip.smart.server.servlet.JsonServlet;

/**
 * <b>The servlet ModelingInfo return the main data about the current modeling.</b>
 *
 * @author Pierre Demessence
 */

@WebServlet(name = "ModelingInfo", urlPatterns = "/modeling_info")
public class ModelingInfo extends JsonServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException, StatusException {
		Modeling modeling = Server.getServer().getCurrentModeling();
		if (modeling == null)
			throw new StatusException(ServerStatus.MODELING_NO_CURRENT);
		json.writeFieldName("modeling");
		this.mapper.writeValue(json, modeling);
	}

}
