package eip.smart.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eip.smart.model.Modeling;

/**
 * Created by vincent buresi on 10/2/14.
 */
@WebServlet(name = "ModelingTest", urlPatterns = "/modeling")
public class ModelingTest extends HttpServlet {

	private int	delay	= 1000;

	protected void answer(PrintWriter writer, String requestType) {
		Modeling modeling = new Modeling();

		try {
			System.out.println("Start of Modeling");
			System.out.println();
			System.out.println("===== ===== ===== ===== =====");
			modeling.dumpAgents();
			System.out.println("----- ----- ----- ----- -----");
			System.out.println("Modeling at " + modeling.getCompletion() + "%");
			System.out.println("===== ===== ===== ===== =====");
			System.out.println();
			Thread.currentThread();
			Thread.sleep(this.delay);
			while (modeling.getCompletion() < 100.0d) {
				System.out.println();
				System.out.println("===== ===== ===== ===== =====");
				modeling.run();
				System.out.println("----- ----- ----- ----- -----");
				modeling.dumpAgents();
				System.out.println("----- ----- ----- ----- -----");
				System.out.println("Modeling at " + modeling.getCompletion() + "%");
				System.out.println("===== ===== ===== ===== =====");
				System.out.println();
				Thread.currentThread();
				Thread.sleep(this.delay);
			}
			System.out.println("End of Modeling");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter writer = resp.getWriter();
		try {
			this.delay = Integer.parseInt(req.getParameter("delay"));
		} catch (Exception e) {
			this.delay = 1000;
		}
		this.answer(writer, "GET");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter writer = resp.getWriter();
		this.answer(writer, "POST");
	}
}
