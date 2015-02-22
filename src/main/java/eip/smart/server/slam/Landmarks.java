/**
 * 
 */
package eip.smart.server.slam;

import java.util.ArrayList;

import eip.smart.model.geometry.Point;

/**
 * @author Thing-leoh Nicolas
 *
 */
public class Landmarks {

	/**
	 * @TODO: Find a way to maybe put them in static const and unsigned
	 */
	// If a landmarks is within this distance of another landmarks, its the same
	// landmarks (in cm i think)
	public final static double MAXERROR = 0.5;
	// Number of times a landmark must be observed to be recognized as a
	// landmark
	public final static int MINOBSERVATION = 15;
	// Use to reset life counter (counter use to determine whether to discard a
	// landmark or not)
	public final static int LIFE = 40;
	private ArrayList<Landmark> landmarkDB = new ArrayList<Landmark>();
	private int idCounter = 0;
	
	public class Landmark {
		public Point position = new Point(0.0, 0.0, 0.0);
		public int id = -1;
		// a life counter to determine whether to discard a landmark or not
		public int life = LIFE;
		// the number of times we have seen the landmark
		public int totalTimeObserved = 0;
		// last observed range from agent to landmark
		public double range = -1;
		// last observed bearing from agent to landmark
		public double bearing = -1;
		// last position of agent when landmark was observed
		public Point agentPosition = new Point(0.0, 0.0, 0.0);
		
		public Landmark() {};
		
		public Landmark(Point position, int life, int totalTimeObserved, double range, double bearing, Point agentPosition) {
			this.position = position;
			this.life = life;
			this.totalTimeObserved = totalTimeObserved;
			this.range = range;
			this.bearing = bearing;
			this.agentPosition = agentPosition;
			this.id = Landmarks.this.idCounter;
		}
	}
	
	public Landmarks() {}
	
	public int getDBSize() {
		return (this.landmarkDB.size());
	}
	
	/**
	 * Associate a landmark with its possible counterpart in DB
	 * 
	 * @param lm
	 *            Landmark we want to associate
	 * @param idCompare
	 *            Id of landmark in DB we want to check with
	 * @return id of associated Landmark in DB or -1
	 */
	public int getAssociation(Landmark lm, int idCompare) {
		Landmark toCompare = this.landmarkDB.get(idCompare);
		
		if (lm.position.getDistance(toCompare.position) < MAXERROR && toCompare.id != -1) {
			toCompare.life = LIFE;
			++toCompare.totalTimeObserved;
			toCompare.bearing = lm.bearing;
			toCompare.range = lm.range;
			toCompare.agentPosition = lm.agentPosition;
			return (toCompare.id);
		}
		return (-1);
	}
	
	/**
	 * Search and find which landmark in DB can be associated with param
	 * 
	 * @param lm
	 *            Landmark we want to associate
	 * @return id of associated Landmark in DB or -1
	 */
	public int getAssociation(Landmark lm) {
		for (Landmark toCompare: this.landmarkDB) {
			int id = this.getAssociation(lm, toCompare.id);
			if (id != -1)
				return (id);
		}
		return (-1);
	}
	
	/**
	 * Add a copy of parameter to DB
	 * 
	 * @param lm
	 *            Landmark we want to add to DB
	 * @return id of Landmark added in DB or -1 if error
	 */
	public int addToDB(Landmark lm) {
		Landmark new_elem = new Landmark(lm.position, LIFE, 1, lm.range, lm.bearing, lm.agentPosition);
		
		try {
			this.landmarkDB.add(new_elem);
		} catch (Exception e) {
			//@TODO: Show exception somewhere
		return (-1);
	}
	
		++this.idCounter;
		return (new_elem.id);
	}

	public ArrayList<Landmark> getLandmarkDB() {
		return (this.landmarkDB);
	}
	
	/**
	 * Get valid Landmark in DB closest to param. A valid Landmark is a Landmark
	 * which was observed enough time (more than MINOBSERVATION)
	 * 
	 * @param lm
	 *            Landmark we want to compare
	 * @param id
	 *            out parameter: Id of closest valid Landmark found
	 * @param totalTimeObserved
	 *            out parameter: Total time observed of closest valid Landmark
	 *            found
	 */
	public void getClosestAssociation(Landmark lm) {
		Landmark closestLandmark = null;
		double leastDistance = -1;
		
		for (Landmark toCompare: this.landmarkDB) {
			if (toCompare.totalTimeObserved > MINOBSERVATION) {
				double temp = lm.position.getDistance(toCompare.position);
				if (leastDistance < 0 || temp < leastDistance) {
					leastDistance = temp;
					closestLandmark = toCompare;
				}
			}
		}
		
		if (leastDistance < 0 || closestLandmark == null) {
			lm.id = -1;
			lm.totalTimeObserved = -1;
		}
		else {
			lm.id = closestLandmark.id;
			lm.totalTimeObserved = closestLandmark.totalTimeObserved;
		}
	}
	
	/**
	 * Function use for debug 
	 * 
	 * @return landmark closest to Agent origin
	 */
	public Landmark getOrigin() {
		Landmark lm = new Landmark();
		this.getClosestAssociation(lm);
		return (lm);
	}
}
