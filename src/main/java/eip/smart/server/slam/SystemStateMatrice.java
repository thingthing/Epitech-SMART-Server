/**
 *
 */
package eip.smart.server.slam;

import java.util.ArrayList;
import java.util.LinkedList;

import eip.smart.model.Agent;
import eip.smart.model.geometry.Point;

/**
 * @author Nicolas
 *
 */
public class SystemStateMatrice {

	ArrayList<Agent>	agents	= new ArrayList<Agent>();
	LinkedList<Point>	matrice	= new LinkedList<Point>();

	public SystemStateMatrice() {}

	public SystemStateMatrice(Agent agent) {
		this.agents.add(new Agent(agent));
	}

	public SystemStateMatrice(ArrayList<Agent> agents) {
		for (Agent a : agents)
			this.agents.add(new Agent(a));
	}

	/**
	 * Add landmark position to state matrices
	 *
	 * @param landmarkPos
	 *            position to be add
	 * @return the id of the position in the matrices
	 */
	public int addLandmarkPosition(Point landmarkPos) {
		this.matrice.push(landmarkPos);
		return (this.matrice.size() - 1);
	}

	public Agent getAgent(String name, ArrayList<Agent> agentList) {
		for (Agent a : agentList)
			if (name.equals(a.getName()))
				return (a);
		return (null);
	}

	/**
	 * Get agent position in state matrices
	 * 
	 * @param agent
	 *            Agent to get position
	 * @return Position of agent in list
	 */
	public Point getAgentPos(Agent agent) {
		Agent tmp = this.getAgent(agent.getName(), this.agents);
		if (tmp != null)
			return (tmp.getCurrentPosition());
		return (new Point(0.0, 0.0, 0.0));
	}

	/**
	 * Set agent state
	 *
	 * @param agent
	 *            Agent which state has to be set
	 */
	public void setAgentState(Agent agent) {
		Agent tmp = this.getAgent(agent.getName(), this.agents);
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
	public void updateAgentState(Agent agent, Double bearingAdjustment, Point posAdjustment) {
		Agent tmp = this.getAgent(agent.getName(), this.agents);
		if (tmp != null) {
			tmp.setCurrentPosition(tmp.getCurrentPosition().add(posAdjustment));
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
	public void updateLandmarkPosition(int landmarkMatriceId, Point newPos) {
		if (landmarkMatriceId < this.matrice.size())
			this.matrice.set(landmarkMatriceId, newPos);
	}
}
