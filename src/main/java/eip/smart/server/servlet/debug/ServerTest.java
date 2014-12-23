package eip.smart.server.servlet.debug;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eip.smart.cscommons.CSCommons;

/**
 * Created by vincent buresi on 10/2/14.
 */
@WebServlet(name = "ServerTest", urlPatterns = "/test")
public class ServerTest extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.getWriter().println("Server is up and running ! Congratulations !");
		resp.getWriter().println("Server version : " + CSCommons.getVersion());
		resp.getWriter().println("Request type : " + "GET");
		resp.getWriter().println("Json version : " + CSCommons.getJacksonVersion());
		resp.getWriter().println("\n" + CSCommons.testJacksonVersion());
		resp.getWriter().println("\nEND");
	}

}
