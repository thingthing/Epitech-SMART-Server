package eip.smart.server.servlet.modeling;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.model.Modeling;
import eip.smart.model.Status;
import eip.smart.server.Server;
import eip.smart.server.servlet.JsonServlet;

/**
 * <b>The servlet ModelingInfo return the main data about the current modeling.</b>
 * @author Pierre Demessence
*/

@WebServlet(name = "ModelingInfo", urlPatterns = "/modeling_info")
public class ModelingInfo extends JsonServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException {
		Modeling modeling = Server.getServer().getCurrentModeling();
		if (modeling == null)
			this.status = Status.MODELING_NO_CURRENT;
		else {
			json.writeFieldName("modeling");
			this.mapper.writeValue(json, modeling);
		}
	}

}
