package eip.smart.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eip.smart.model.geometry.Point;
import eip.smart.model.geometry.Rectangle;
import eip.smart.model.geometry.Square;

/**
 * Created by vincent buresi on 10/2/14.
 */
@WebServlet(name = "PointTest", urlPatterns = "/point")
public class PointTest extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Square p1 = new Square(new Point(0, 0, 0), 5);
		resp.getWriter().println("Square area of size 5 :\n" + p1);
		resp.getWriter().println("Square perimeter of size 5 : " + p1.getPerimeter());
		resp.getWriter().println("Square area of size 5 : " + p1.getArea());

		resp.getWriter().println();

		Rectangle p2 = new Rectangle(new Point(0), 4, 2);
		resp.getWriter().println("Rectangle perimeter of width 4 and height 2 :\n" + p2);
		resp.getWriter().println("Rectangle perimeter of width 4 and height 2 : " + p2.getPerimeter());
		resp.getWriter().println("Rectangle area of width 4 and height 2 : " + p2.getArea());
	}
}
