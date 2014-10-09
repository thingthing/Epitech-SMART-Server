package eip.smart.server;

import eip.smart.cscommons.CSCommons;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by vincent buresi on 10/2/14.
 */
public class ServerTest extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        answer(writer, "GET");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        answer(writer, "POST");
    }

    protected void answer(PrintWriter writer, String requestType) {
        writer.println("Server is up and running ! Congratulations !");
        writer.println("Server version : " + CSCommons.getVersion());
        writer.println("Request type : " + requestType);
        writer.println("Json version : " + CSCommons.getJacksonVersion());
        writer.println("\n" + CSCommons.testJacksonVersion());
        writer.println("\nEND");
    }
}
