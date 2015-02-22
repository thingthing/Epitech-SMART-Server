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
}
