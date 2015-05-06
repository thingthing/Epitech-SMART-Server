package eip.smart.server.servlet.modeling;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.model.Status;
import eip.smart.server.Server;
import eip.smart.server.servlet.JsonServlet;

/**
 * <b>The servlet ModelingResume takes back the current modeling.</b>
 * @author Pierre Demessence
*/

@WebServlet("/modeling_resume")
public class ModelingResume extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException {
		if (Server.getServer().getCurrentModeling() == null)
			this.status = Status.MODELING_NO_CURRENT;
		else if (!Server.getServer().isRunning())
			this.status = Status.MODELING_NOT_RUNNING;
		else if (!Server.getServer().isPaused())
			this.status = Status.MODELING_NOT_PAUSED;
		else
			Server.getServer().modelingResume();
	}

}
