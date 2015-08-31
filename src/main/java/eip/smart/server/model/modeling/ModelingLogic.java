package eip.smart.server.model.modeling;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eip.smart.cscommons.model.agent.Agent;
import eip.smart.cscommons.model.geometry.Point3D;
import eip.smart.cscommons.model.modeling.Area;
import eip.smart.cscommons.model.modeling.Modeling;
import eip.smart.server.model.agent.AgentLogic;

public class ModelingLogic extends Modeling {
	private final static Logger	LOGGER	= LoggerFactory.getLogger(ModelingLogic.class);

	// TODO THIS METHOD IS SHIT
	protected static double getDiffPoint(Point3D point3d, Point3D point3d2) {
		return (Math.abs((Math.abs(point3d.getX() - point3d2.getX())) - (Math.abs(point3d.getY() - point3d2.getY()))));
	}

	public ModelingLogic() {
		super();
	}

	public ModelingLogic(Modeling modeling) {
		super(modeling);
	}

	public ModelingLogic(String name) {
		super(name);
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

	@Override
	public List<AgentLogic> getAgents() {
		List<AgentLogic> res = new ArrayList<>();
		System.out.println(super.getAgents());
		for (Agent a : super.getAgents())
			res.add(new AgentLogic(a));
		return res;
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
		ModelingLogic.LOGGER.info("Modeling (" + this.name + ") running (tick " + this.tick + ")");
		this.updateAreaAgentsAttributed();
		this.updateAgents();
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

	/**
	 * update agents'state
	 *
	 * @see Agent
	 */
	private void updateAgents() {
		ModelingLogic.LOGGER.info("->Updating Agents...");
		for (AgentLogic agent : this.getAgents())
			agent.updateState();
	}

	/**
	 * update agents'attributed's areas
	 *
	 * @see Agent
	 */
	private void updateAreaAgentsAttributed() {
		// for (Area a : this.areas)
		// a.updateCompletion();
		Area dest;
		if (this.areas.size() > 0)
			for (AgentLogic agent : this.getAgents()) {
				dest = this.areas.get(0);
				for (Area area : this.areas)
					if (ModelingLogic.getDiffPoint(agent.getCurrentPosition(), area.getAvgPoint()) < ModelingLogic.getDiffPoint(agent.getCurrentPosition(), dest.getAvgPoint()))
						dest = area;
				agent.setDestination(dest);
			}
		ModelingLogic.LOGGER.info("->Attributing number of agents for each area...");
	}
}
