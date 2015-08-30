package eip.smart.server.servlet;

import java.io.File;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/log_clean")
public class LogCleanServlet extends HttpServlet {

    private final static Logger	LOGGER	= LoggerFactory.getLogger(LogCleanServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
		String filename = System.getProperty("catalina.base") + "/log/log.html";
		File log = new File(filename);
        log.delete();
        LogCleanServlet.LOGGER.info("Log cleaned.");
	}
}