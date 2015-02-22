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
	
}
