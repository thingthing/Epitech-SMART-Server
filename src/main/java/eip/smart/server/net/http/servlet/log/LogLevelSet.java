package eip.smart.server.net.http.servlet.log;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.cscommons.model.ServerStatus;
import eip.smart.server.net.http.servlet.JsonServlet;
import eip.smart.server.util.exception.StatusException;

/**
 * <b>The servlet AgentDelete take an agent's name as parameter and delete the corresponding Agent from the available agents' list of the server.</b>
 *
 * @author Pierre Demessence
 */

@WebServlet(urlPatterns = { "/log_level_set" }, initParams = { @WebInitParam(name = "level", value = "") })
public class LogLevelSet extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException, StatusException {
		String name = JsonServlet.getParameter(req, "level");
		Level level = Level.toLevel(name, null);
		if (level == null)
			throw new StatusException(ServerStatus.NOT_FOUND.addObjects("level", "name", name));
		((Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME)).setLevel(level);
	}
}
