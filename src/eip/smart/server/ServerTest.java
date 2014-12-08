package eip.smart.server;

import java.io.IOException;
import java.io.PrintWriter;

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
	protected void answer(PrintWriter writer, String requestType) {
		writer.println("Server is up and running ! Congratulations !");
		writer.println("Server version : " + CSCommons.getVersion());
		writer.println("Request type : " + requestType);
		writer.println("Json version : " + CSCommons.getJacksonVersion());
		writer.println("\n" + CSCommons.testJacksonVersion());
		writer.println("\nEND");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter writer = resp.getWriter();
		this.answer(writer, "GET");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter writer = resp.getWriter();
		this.answer(writer, "POST");
	}
}
