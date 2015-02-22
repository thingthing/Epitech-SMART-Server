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
		return (this.DBSize);
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
		if (lm.position.getDistance(this.landmarkDB.get(idCompare).position) < MAXERROR && this.landmarkDB.get(idCompare).id != -1) {
			this.landmarkDB.get(idCompare).life = LIFE;
			++this.landmarkDB.get(idCompare).totalTimeObserved;
			this.landmarkDB.get(idCompare).bearing = lm.bearing;
			this.landmarkDB.get(idCompare).range = lm.range;
			this.landmarkDB.get(idCompare).agentPosition = lm.agentPosition;
			return (this.landmarkDB.get(idCompare).id);
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
		for (int i = 0; i < this.DBSize; ++i) {
			int id = this.getAssociation(lm, i);
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
		if (this.DBSize + 1 < this.landmarkDB.size()) {
			Landmark new_elem = new Landmark();
			new_elem.position = new Point(lm.position.getX(), lm.position.getY(), lm.position.getZ());
			new_elem.life = LIFE;
			new_elem.id = this.DBSize;
			new_elem.totalTimeObserved = 1;
			new_elem.bearing = lm.bearing;
			new_elem.range = lm.range;
			new_elem.agentPosition = new Point(lm.agentPosition.getX(), lm.agentPosition.getY(), lm.agentPosition.getZ());
			this.landmarkDB.set(this.DBSize, new_elem);
			
			++this.DBSize;
			return (new_elem.id);
		}
		return (-1);
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
	public void getClosestAssociation(Landmark lm, int id, int totalTimeObserved) {
		Landmark closestLandmark = null;
		double temp;
		double leastDistance = -1;
		
		for (int i = 0; i < this.DBSize; ++i) {
			if (this.landmarkDB.get(i).totalTimeObserved > MINOBSERVATION) {
				temp = lm.position.getDistance(this.landmarkDB.get(i).position);
				if (leastDistance < 0 || temp < leastDistance) {
					leastDistance = temp;
					closestLandmark = this.landmarkDB.get(i);
				}
			}
		}
		if (leastDistance < 0 || closestLandmark == null)
			id = -1;
		else {
			id = closestLandmark.id;
			totalTimeObserved = closestLandmark.totalTimeObserved;
		}
	}
	
	/**
	 * Function use for debug 
	 * 
	 * @return landmark closest to Agent origin
	 */
	public Landmark getOrigin() {
		Landmark lm = new Landmark();
		this.getClosestAssociation(lm, lm.id, lm.totalTimeObserved);
		return (lm);
	}
}
