package eip.smart.server.net.http.servlet.log;

import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/log")
public class LogServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String filename = System.getProperty("catalina.base") + "/log/log.html";
		FileInputStream fis = new FileInputStream(filename);
		int b = 0;
		while ((b = fis.read()) != -1)
			resp.getOutputStream().write(b);
		fis.close();
	}
}