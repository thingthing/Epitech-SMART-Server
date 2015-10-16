package eip.smart.server.util.configuration;

import java.io.File;

public class LocationManager {
	private static final String	DEFAULT_PARENT_FOLDER	= System.getProperty("catalina.base");

	public static final String	LOCATION_MODELINGS		= new File(LocationManager.DEFAULT_PARENT_FOLDER, "modelings").getAbsolutePath();
	public static final String	LOCATION_CONFIG			= new File(LocationManager.DEFAULT_PARENT_FOLDER, "config").getAbsolutePath();
}
