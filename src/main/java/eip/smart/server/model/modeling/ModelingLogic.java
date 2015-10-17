package eip.smart.server.model.modeling;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eip.smart.cscommons.model.agent.Agent;
import eip.smart.cscommons.model.geometry.Point3D;
import eip.smart.cscommons.model.geometry.PointCloud3DGenerator;
import eip.smart.cscommons.model.modeling.Area;
import eip.smart.cscommons.model.modeling.Modeling;
import eip.smart.cscommons.model.modeling.ModelingState;
import eip.smart.server.Server;
import eip.smart.server.model.agent.AgentLogic;
import eip.smart.server.slam.Landmarks;
import eip.smart.server.slam.Slam;

public class ModelingLogic extends Modeling {
	private final static Logger	LOGGER	= LoggerFactory.getLogger(ModelingLogic.class);

	// TODO THIS METHOD IS SHIT
	protected static double getDiffPoint(Point3D point3d, Point3D point3d2) {
		return (Math.abs((Math.abs(point3d.getX() - point3d2.getX())) - (Math.abs(point3d.getY() - point3d2.getY()))));
	}

	protected Slam	slam;

	public ModelingLogic() {
		super();
	}

	public ModelingLogic(Modeling modeling) {
		super(modeling);
		this.slam = new Slam(this, new ArrayList<Landmarks.Landmark>());
	}

	public ModelingLogic(String name) {
		super(name);
		this.slam = new Slam(this, new ArrayList<Landmarks.Landmark>());
	}

	/**
	 * Add an agent to the modeling
	 *
	 * @see Agent
	 * @param agent
	 *            Agent that will be added to the modeling
	 */
	public void addAgent(AgentLogic agent) {
		this.agents.add(agent);
	}

	/**
	 * Add an area to the modeling
	 *
	 * @see Area
	 * @param area
	 *            Area that will be added to the modeling
	 */
	public void addArea(Area area) {
		this.areas.add(area);
	}

	public void addPoint(Point3D point) {
		this.mapping.add(point);
	}

	public void addPoints(List<Point3D> points) {
		this.mapping.add(points);
	}

	public void calculateCompletion() {
		// TODO
		this.completion = 0;
	}

	@Override
	public List<AgentLogic> getAgents() {
		List<AgentLogic> res = new ArrayList<>();
		List<? extends Agent> agents = super.getAgents();
		if (agents.isEmpty())
			return (res);
		for (AgentLogic a : Server.getServer().getAgentManager().getAgentsAvailable())
			if (agents.contains(a))
				res.add(a);
		return res;
	}

	public Slam getSlam() {
		return (this.slam);
	}

	/**
	 * remove an agent of the modeling
	 *
	 * @see Agent
	 * @param agent
	 *            agent, the one we'll remove
	 */
	public void removeAgent(Agent agent) {
		this.agents.remove(agent);
	}

	/**
	 * main method of of the modeling development, it:
	 * <ul>
	 * <li>update agents'position</li>
	 * <li>update agents'attributed'areas</li>
	 * <li>update agents'destination's area</li>
	 * <li>update agents'destination's points</li>
	 * </ul>
	 *
	 * @see Agent
	 */
	public void run() {
		++this.tick;
		ModelingLogic.LOGGER.trace("Modeling (" + this.name + ") running (tick " + this.tick + ")");
		this.updateAreaAgentsAttributed();
	}

	public void setAgents(List<Agent> agents) {
		this.agents = agents;
	}

	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setObsolete(boolean obsolete) {
		this.obsolete = obsolete;
	}

	public void setState(ModelingState s) {
		this.state = s;
	}

	@SuppressWarnings("static-method")
	public void stop() {
		for (AgentLogic agent : Server.getServer().getAgentManager().getAgentsAvailable())
			agent.setCurrentDestination(null);
	}

	/**
	 * update agents'attributed's areas
	 *
	 * @see Agent
	 */
	@SuppressWarnings("static-method")
	private void updateAreaAgentsAttributed() {
		// TODO for (AgentLogic agent : this.getAgents()) {
		for (AgentLogic agent : Server.getServer().getAgentManager().getAgentsAvailable()) {
			PointCloud3DGenerator r = new PointCloud3DGenerator();
			if (agent.getCurrentDestination() == null)
				agent.setCurrentDestination(r.generateIntPoint());
		}
	}
}
