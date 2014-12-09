package eip.smart.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

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

	private final static Logger	LOGGER	= Logger.getLogger(ModelingTest.class.getName());

	private int					delay	= 1000;

	protected void answer(PrintWriter writer, String requestType) {
		Modeling modeling = new Modeling();

		try {
			ModelingTest.LOGGER.log(Level.INFO, "Start of Modeling");
			modeling.dumpAgents();
			ModelingTest.LOGGER.log(Level.INFO, "Modeling at " + modeling.getCompletion() + "%");
			Thread.currentThread();
			Thread.sleep(this.delay);
			while (modeling.getCompletion() < 100.0d) {
				modeling.run();
				modeling.dumpAgents();
				ModelingTest.LOGGER.log(Level.INFO, "Modeling at " + modeling.getCompletion() + "%");
				Thread.currentThread();
				Thread.sleep(this.delay);
			}
			ModelingTest.LOGGER.log(Level.INFO, "End of Modeling");
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
