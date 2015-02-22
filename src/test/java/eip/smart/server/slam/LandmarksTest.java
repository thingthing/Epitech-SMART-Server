/**
 * @author Thing-leoh Nicolas
 */
package eip.smart.server.slam;

import java.io.IOException;
import java.util.ArrayList;

import eip.smart.model.geometry.Point;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import eip.smart.server.slam.Landmarks;
import eip.smart.server.slam.Landmarks.Landmark;

@SuppressWarnings("static-method")
public class LandmarksTest {

	private Landmarks parent;
	
	@Before
	public void setUp() throws Exception {
		this.parent = new Landmarks();
	}

	@After
	public void tearDown() throws Exception {

	}
	
	@Test
	public void LandmarkCreateTest() {
		Landmarks.Landmark test = this.parent.new Landmark();
		
		Assert.assertEquals(0.0, test.position.getX(), 0);
		Assert.assertEquals(0.0, test.position.getY(), 0);
		Assert.assertEquals(0.0, test.position.getZ(), 0);
		Assert.assertEquals(-1, test.id);
		Assert.assertEquals(Landmarks.LIFE, test.life);
		Assert.assertEquals(0, test.totalTimeObserved);
		Assert.assertEquals(-1.0, test.range, 0);
		Assert.assertEquals(-1.0, test.bearing, 0);
		Assert.assertEquals(0.0, test.agentPosition.getX(), 0);
		Assert.assertEquals(0.0, test.agentPosition.getY(), 0);
		Assert.assertEquals(0.0, test.agentPosition.getZ(), 0);
	}
	
	@Test
	public void addToDBTest() {
		//Create new landmark
		Landmarks.Landmark test = this.parent.new Landmark();
		double posx = 5.2;
		double posy = 4.3;
		double posz = 0.0;
		double bearing = 6.1;
		double range = 15.0;
		
		//Set some values
		test.position = new Point(posx, posy, posz);
		test.bearing = bearing;
		test.range = range;
		
		//Save old values to compare
		int oldDbSize = this.parent.getDBSize();
		
		//Add landmark to db
		int newLandmarkId = this.parent.addToDB(test);
		
		//Check valid id
		Assert.assertNotSame(-1, newLandmarkId);
		//Check if db size went up
		Assert.assertEquals(oldDbSize + 1, this.parent.getDBSize());
		//Check value of new Landmark in DB
		Landmarks.Landmark result = this.parent.getLandmarkDB().get(newLandmarkId);
		Assert.assertEquals(posx, result.position.getX(), 0);
		Assert.assertEquals(posy, result.position.getY(), 0);
		Assert.assertEquals(posz, result.position.getZ(), 0);
		Assert.assertEquals(Landmarks.LIFE, result.life);
		Assert.assertEquals(oldDbSize, result.id);
		Assert.assertEquals(1, result.totalTimeObserved);
		Assert.assertEquals(range, result.range, 0);
		Assert.assertEquals(bearing, result.bearing, 0);
	}
}
