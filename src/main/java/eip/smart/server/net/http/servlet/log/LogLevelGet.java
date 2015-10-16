package eip.smart.server.net.http.servlet.log;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.server.net.http.servlet.JsonServlet;
import eip.smart.server.util.exception.StatusException;

/**
 * <b>The servlet AgentDelete take an agent's name as parameter and delete the corresponding Agent from the available agents' list of the server.</b>
 *
 * @author Pierre Demessence
 */

@WebServlet(urlPatterns = { "/log_level_get" })
public class LogLevelGet extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException, StatusException {
		json.writeFieldName("level");
		this.mapper.writeValue(json, ((Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME)).getLevel().toString());
	}
}
