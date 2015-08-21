package eip.smart.server.servlet.debug;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.cscommons.model.JSONViews;
import eip.smart.cscommons.model.ServerStatus;
import eip.smart.cscommons.model.agent.Agent;
import eip.smart.cscommons.model.geometry.Point2D;
import eip.smart.cscommons.model.geometry.Point3D;
import eip.smart.cscommons.model.geometry.PointCloud2D;
import eip.smart.cscommons.model.geometry.PointCloud3D;
import eip.smart.cscommons.model.geometry.polygon.Polygon;
import eip.smart.cscommons.model.modeling.Area;
import eip.smart.cscommons.model.modeling.Modeling;
import eip.smart.server.servlet.JsonServlet;

/**
 * Servlet implementation class SerializationTest
 */
@WebServlet(urlPatterns = { "/serialization_test" })
public class SerializationTest extends JsonServlet {
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, JsonGenerator json) throws ServletException, IOException {
		List<Object> objects = new ArrayList<>();
		objects.add(new Agent("AgentTest"));
		objects.add(new Point2D(1, 2));
		objects.add(new Point3D(2, 3, 4));
		objects.add(new PointCloud2D());
		objects.add(new PointCloud3D());
		objects.add(new Area());
		objects.add(new Modeling("ModelingTest"));
		objects.add(new Polygon());
		objects.add(ServerStatus.OK);
		for (Object object : objects) {
			json.writeFieldName(object.getClass().getSimpleName());
			this.mapper.writerWithView(JSONViews.ALL.class).writeValue(json, object);
		}
	}
}
