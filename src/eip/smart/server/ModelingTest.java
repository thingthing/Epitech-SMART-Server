package eip.smart.server;

import eip.smart.model.Modeling;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by vincent buresi on 10/2/14.
 */
public class ModelingTest extends HttpServlet {

    private int delay = 1000;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        try {
            this.delay = Integer.parseInt(req.getParameter("delay"));
        } catch (Exception e) {
            this.delay = 1000;
        }
        answer(writer, "GET");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        answer(writer, "POST");
    }

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
            Thread.currentThread().sleep(this.delay);
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
                Thread.currentThread().sleep(this.delay);
            }
            System.out.println("End of Modeling");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
