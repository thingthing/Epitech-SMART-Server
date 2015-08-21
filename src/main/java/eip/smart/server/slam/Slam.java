/**
 *
 */
package eip.smart.server.slam;

import java.util.ArrayList;
import java.util.List;

import eip.smart.cscommons.model.geometry.v2.Point3D;
import eip.smart.server.model.agent.AgentLogic;
import eip.smart.server.model.modeling.ModelingLogic;

/**
 * @author Thing-leoh Nicolas
 *
 */
public class Slam {

	public Landmarks					landmarkDB			= new Landmarks();
	public SystemStateMatrice			state				= new SystemStateMatrice();
	// To be set with the new currentModeling
	public ModelingLogic				currentModeling		= null;
	// To be set with the recieveLandmarks
	public List<Landmarks.Landmark>		recieveLandmarks	= null;

	private List<Landmarks.Landmark>	newLandmakrs		= null;
	private List<Landmarks.Landmark>	reobservedLandmakrs	= null;

	public Slam(ModelingLogic currentModeling, List<Landmarks.Landmark> recievedLandmarks) {
		this.currentModeling = currentModeling;
		this.recieveLandmarks = recievedLandmarks;
		this.state = new SystemStateMatrice(this.currentModeling.getAgents());
	}

	/**
	 * Add new landmarks to landmarks DB
	 *
	 * @param newLandmarks
	 *            New landmarks to be add
	 */
	public void addLandmarks(List<Landmarks.Landmark> newLandmarks) {
		if (newLandmarks == null)
			newLandmarks = this.newLandmakrs;
		for (Landmarks.Landmark lm : newLandmarks) {
			int lmId = this.landmarkDB.addToDB(lm);
			int matricesId = this.state.addLandmarkPosition(lm.position);
			this.landmarkDB.setMatriceId(lmId, matricesId);
			// @TODO: Add landmark to covariance matrice
			// @TODO: Calculate covariance
		}
	}

	/**
	 * Update agent position with old and new landmarks:
	 * Add new landmarks from modeling
	 *
	 * @param currentModeling
	 *            Current modeling or null if already set
	 */
	@SuppressWarnings("unused")
	public void updateAgentState(ModelingLogic currentModeling) {
		if (currentModeling == null)
			currentModeling = this.currentModeling;
		for (AgentLogic agent : currentModeling.getAgents())
			this.state.setAgentState(agent);
		// @TODO: Update jacobian matrice
		// @TODO: update noise matrice
		// @TODO: Set and update covariance matrice
		// @TODO: Set recieveLandmarks beforehand or pass it in the param

		this.validationGate(null);
		this.addLandmarks(null);

		for (AgentLogic agent : currentModeling.getAgents()) {
			for (Landmarks.Landmark lm : this.reobservedLandmakrs)
				// @TODO: calculate Kalman gain
				// @TODO: Change to Update state using Kalman gain
				this.state.updateAgentState(agent, 0.0, new Point3D(0.0, 0.0, 0.0));
			agent.setCurrentPosition(this.state.getAgentPos(agent));
		}

		this.landmarkDB.removeBadLandmarks(currentModeling);
	}

	/**
	 * Associate recieveLandmark with db Landmark
	 *
	 * @param recieveLandmarks
	 *            Landmarks recieve from agents or null if recieveLandmarks already set
	 */
	public void validationGate(List<Landmarks.Landmark> recieveLandmarks) {
		if (recieveLandmarks == null)
			recieveLandmarks = this.recieveLandmarks;
		this.newLandmakrs = new ArrayList<>();
		this.reobservedLandmakrs = new ArrayList<>();

		for (Landmarks.Landmark toAssociate : recieveLandmarks) {
			this.landmarkDB.getClosestAssociation(toAssociate);
			if (this.landmarkDB.getAssociation(toAssociate, toAssociate.id) == -1)
				this.newLandmakrs.add(toAssociate);
			else
				this.reobservedLandmakrs.add(toAssociate);
		}
	}
}
