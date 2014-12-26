package eip.smart.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.model.Modeling;
import eip.smart.model.Status;
import eip.smart.server.Server;

/**
 * Created by vincent buresi on 10/2/14.
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
