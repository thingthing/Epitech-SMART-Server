package eip.smart.server.servlet;

import java.io.File;
import java.io.IOException;
import java.lang.SecurityException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eip.smart.model.Status;

@WebServlet("/log_clean")
public class LogCleanServlet extends JsonServlet {

    private final static Logger	LOGGER	= LoggerFactory.getLogger(LogCleanServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException {
		String filename = System.getProperty("catalina.base") + "/log/log.html";
		File log = new File(filename);
		try {
        	log.delete();
			log.createNewFile();
		} catch (IOException | SecurityException e) {
			throw new StatusException(Status.ERR_UNKNOWN.addObjects(e.getMessage()));
		}
        LogCleanServlet.LOGGER.info("Log cleaned.");
	}
}