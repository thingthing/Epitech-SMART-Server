package eip.smart.server.net.http.servlet.modeling;

import com.fasterxml.jackson.core.JsonGenerator;
import eip.smart.cscommons.model.ServerStatus;
import eip.smart.cscommons.model.geometry.PointCloud;
import eip.smart.cscommons.model.geometry.PointCloud3DGenerator;
import eip.smart.cscommons.model.modeling.Modeling;
import eip.smart.server.Server;
import eip.smart.server.model.modeling.ModelingLogic;
import eip.smart.server.net.http.servlet.JsonServlet;
import eip.smart.server.util.exception.StatusException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <b>The servlet GetPoints return the list of the new points of the current modeling.</b>
 *
 * @author Pierre Demessence
 */

@WebServlet(urlPatterns = {"/get_points"}, initParams = {@WebInitParam(name = "from", value = ""), @WebInitParam(name = "nb", value = "")})
public class GetPoints extends JsonServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @throws StatusException
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response, JsonGenerator json) throws ServletException, IOException, StatusException {
        ModelingLogic currentModeling = Server.getServer().getModelingManager().getCurrentModeling();
        Modeling modeling = currentModeling;
        PointCloud p;
        if (modeling == null) {
            p = new PointCloud3DGenerator().generatePointCloud(10);
            // throw new StatusException(ServerStatus.MODELING_NO_CURRENT);
        } else {
            p = currentModeling.getMapping();
        }
        String from, nb;
        if ((from = getParameter(request, "from", false)) != null && (nb = getParameter(request, "nb", false)) != null) {
            try {
                p = p.getSubPointCloud(Integer.parseInt(from), Integer.parseInt(nb));
            } catch (NumberFormatException e) {
                throw new StatusException(ServerStatus.ERROR_PARAMETER.addObjects("from or nb"));
            }
        }
        json.writeFieldName("pointcloud");
        this.mapper.writeValue(json, p);
    }

}
