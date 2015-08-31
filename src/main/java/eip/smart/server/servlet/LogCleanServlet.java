package eip.smart.server.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.cscommons.model.ServerStatus;
import eip.smart.server.exception.StatusException;

@WebServlet("/log_clean")
public class LogCleanServlet extends JsonServlet {

	private final static Logger	LOGGER	= LoggerFactory.getLogger(LogCleanServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException, StatusException {
		String filename = System.getProperty("catalina.base") + "/log/log.html";
		File log = new File(filename);
		try {
			log.delete();
			log.createNewFile();
		} catch (IOException | SecurityException e) {
			throw new StatusException(ServerStatus.ERR_UNKNOWN.addObjects(e.getMessage()));
		}
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		ContextInitializer ci = new ContextInitializer(lc);
		lc.reset();
		try {
			ci.autoConfig();
		} catch (JoranException e) {
			e.printStackTrace();
		}
		LogCleanServlet.LOGGER.info("Log cleaned.");
	}
}