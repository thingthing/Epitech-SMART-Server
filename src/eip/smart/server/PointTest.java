package eip.smart.server;

import eip.smart.cscommons.CSCommons;
import eip.smart.model.Point;
import eip.smart.model.Polygon;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by vincent buresi on 10/2/14.
 */
public class PointTest extends HttpServlet {
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
        Polygon p = new Polygon();
        p.add(new Point(0, 0, 0));
        p.add(new Point(0, 5, 0));
        p.add(new Point(5, 5, 0));
        p.add(new Point(5, 0, 0));
        writer.println("Square perimeter of size 5 : "+p.getPerimeter());
        writer.println("Square area of size 5 : "+p.getArea());
    }
}