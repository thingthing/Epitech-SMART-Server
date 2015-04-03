/**
 *
 */
package eip.smart.server.slam;

import java.util.ArrayList;

import eip.smart.model.Agent;
import eip.smart.model.Modeling;

/**
 * @author Thing-leoh Nicolas
 *
 */
public class Slam {

	public Landmarks						landmarkDB			= new Landmarks();
	// To be set with the new currentModeling
	public Modeling							currentModeling		= null;
	// To be set with the recieveLandmarks
	public ArrayList<Landmarks.Landmark>	recieveLandmarks	= null;

	private ArrayList<Landmarks.Landmark>	newLandmakrs		= null;
	private ArrayList<Landmarks.Landmark>	reobservedLandmakrs	= null;

	public Slam(Modeling currentModeling, ArrayList<Landmarks.Landmark> recievedLandmarks) {
		this.currentModeling = currentModeling;
		this.recieveLandmarks = recievedLandmarks;
	}

	/**
	 * Add new landmarks to landmarks DB
	 *
	 * @param newLandmarks
	 *            New landmarks to be add
	 */
	public void addLandmarks(ArrayList<Landmarks.Landmark> newLandmarks) {
		if (newLandmarks == null)
			newLandmarks = this.newLandmakrs;
		for (Landmarks.Landmark lm : newLandmarks)
			this.landmarkDB.addToDB(lm);
		// @TODO: Add landmark to state matrice and get slamID
		// @TODO: Save SlamId with lmID
		// @TODO: Add landmark to covariance matrice
		// @TODO: Calculate covariance
	}

	/**
	 * Update agent position with old and new landmarks:
	 * Add new landmarks from modeling
	 *
	 * @param currentModeling
	 *            Current modeling or null if already set
	 */
	public void updateAgentState(Modeling currentModeling) {
		if (currentModeling == null)
			currentModeling = this.currentModeling;
		for (Agent agent : currentModeling.getAgents()) {
			// @TODO: update state matrice
			// @TODO: Update jacobian matrice
			// @TODO: update noise matrice
			// @TODO: Set and update covariance matrice
		}
		// @TODO: Set recieveLandmarks beforehand or pass it in the param
		this.validationGate(null);
		this.addLandmarks(null);
		for (Agent agent : currentModeling.getAgents())
			for (Landmarks.Landmark lm : this.reobservedLandmakrs) {
				// @TODO: calculate Kalman gain
				// @TODO: Update state using Kalman gain
			}
		// @TODO: Set agent position: agent.setCurrentPosition(position);
		this.landmarkDB.removeBadLandmarks(currentModeling);
	}

	/**
	 * Associate recieveLandmark with db Landmark
	 *
	 * @param recieveLandmarks
	 *            Landmarks recieve from agents or null if recieveLandmarks already set
	 */
	public void validationGate(ArrayList<Landmarks.Landmark> recieveLandmarks) {
		if (recieveLandmarks == null)
			recieveLandmarks = this.recieveLandmarks;
		this.newLandmakrs = new ArrayList<Landmarks.Landmark>();
		this.reobservedLandmakrs = new ArrayList<Landmarks.Landmark>();

		for (Landmarks.Landmark toAssociate : recieveLandmarks) {
			this.landmarkDB.getClosestAssociation(toAssociate);
			if (this.landmarkDB.getAssociation(toAssociate, toAssociate.id) == -1)
				this.newLandmakrs.add(toAssociate);
			else
				this.reobservedLandmakrs.add(toAssociate);
		}
	}
}
