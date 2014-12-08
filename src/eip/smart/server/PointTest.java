package eip.smart.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eip.smart.model.Point;
import eip.smart.model.Polygon;

/**
 * Created by vincent buresi on 10/2/14.
 */
@WebServlet(name = "PointTest", urlPatterns = "/point")
public class PointTest extends HttpServlet {
	protected void answer(PrintWriter writer, String requestType) {
		Polygon p = new Polygon();
		p.add(new Point(0, 0, 0));
		p.add(new Point(0, 5, 0));
		p.add(new Point(5, 5, 0));
		p.add(new Point(5, 0, 0));
		writer.println("Square perimeter of size 5 : " + p.getPerimeter());
		writer.println("Square area of size 5 : " + p.getArea());
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
