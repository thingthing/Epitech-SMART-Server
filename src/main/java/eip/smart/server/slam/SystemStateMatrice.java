/**
 *
 */
package eip.smart.server.slam;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import eip.smart.cscommons.model.geometry.v2.Point3D;
import eip.smart.server.model.agent.AgentLogic;

/**
 * @author Nicolas
 *
 */
public class SystemStateMatrice {

	public static AgentLogic getAgent(String name, List<AgentLogic> agentList) {
		for (AgentLogic a : agentList)
			if (name.equals(a.getName()))
				return (a);
		return (null);
	}

	List<AgentLogic>	agents	= new ArrayList<>();

	List<Point3D>		matrice	= new LinkedList<>();

	public SystemStateMatrice() {}

	public SystemStateMatrice(AgentLogic agent) {
		this.agents.add(new AgentLogic(agent));
	}

	public SystemStateMatrice(List<AgentLogic> agents) {
		for (AgentLogic a : agents)
			this.agents.add(new AgentLogic(a));
	}

	/**
	 * Add landmark position to state matrices
	 *
	 * @param landmarkPos
	 *            position to be add
	 * @return the id of the position in the matrices
	 */
	public int addLandmarkPosition(Point3D landmarkPos) {
		this.matrice.add(0, landmarkPos);
		return (this.matrice.size() - 1);
	}

	/**
	 * Get agent position in state matrices
	 *
	 * @param agent
	 *            Agent to get position
	 * @return Position of agent in list
	 */
	public Point3D getAgentPos(AgentLogic agent) {
		AgentLogic tmp = SystemStateMatrice.getAgent(agent.getName(), this.agents);
		if (tmp != null)
			return (tmp.getCurrentPosition());
		return (new Point3D(0.0, 0.0, 0.0));
	}

	/**
	 * Set agent state
	 *
	 * @param agent
	 *            Agent which state has to be set
	 */
	public void setAgentState(AgentLogic agent) {
		AgentLogic tmp = SystemStateMatrice.getAgent(agent.getName(), this.agents);
		if (tmp != null)
			tmp.setCurrentPosition(agent.getCurrentPosition());
	}

	/**
	 * Update agent state with Kalman result
	 *
	 * @param agent
	 *            Agent that we wish to update state
	 * @param bearingAdjustment
	 *            Bearing adjustment calculate by Kalman matrices
	 * @param posAdjustment
	 *            Position adjustment calculate by Kalman matrices
	 */
	public void updateAgentState(AgentLogic agent, Double bearingAdjustment, Point3D posAdjustment) {
		AgentLogic tmp = SystemStateMatrice.getAgent(agent.getName(), this.agents);
		if (tmp != null) {
			tmp.setCurrentPosition(new Point3D(tmp.getCurrentPosition().add(posAdjustment)));
			tmp.setCurrentBearing(tmp.getCurrentBearing() + bearingAdjustment);
		}
	}

	/**
	 * Update landmark position in state matrices
	 *
	 * @param landmarkMatriceId
	 *            the id of the landmark in the state matrices
	 * @param newPos
	 *            the position to be updated
	 */
	public void updateLandmarkPosition(int landmarkMatriceId, Point3D newPos) {
		if (landmarkMatriceId < this.matrice.size())
			this.matrice.set(landmarkMatriceId, newPos);
	}
}
