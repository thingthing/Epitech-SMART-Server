package eip.smart.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import eip.smart.model.geometry.Point;
import eip.smart.model.geometry.Polygon;
import eip.smart.util.PointCloudGenerator;

/**
 * Created by Pierre Demessence on 10/10/2014.
 */
public class Area implements Serializable {
	private int							priority			= 0;
	private ArrayList<Polygon>			areaToMap			= new ArrayList<>();
	private ArrayList<Point>			points				= new ArrayList<>();
	private ArrayList<Area>				subAreas			= new ArrayList<>();
	private ArrayList<Agent.AgentType>	capableAgentTypes	= new ArrayList<>();
	private double						completion			= 0.0d;

	public Area() {
		this.points = new PointCloudGenerator().generatePointCloud(20);
	}

	public boolean contains(Point point) {
		for (Polygon polygon : this.areaToMap)
			if (polygon.includes(point))
				return (true);
		return (false);
	}

	public ArrayList<Polygon> getAreaToMap() {
		return this.areaToMap;
	}

	public ArrayList<Agent.AgentType> getCapableAgentTypes() {
		return this.capableAgentTypes;
	}

	public double getCompletion() {
		return (this.completion);
	}

	public int getNbPoints() {
		return (this.points.size());
	}

	public ArrayList<Point> getPoints() {
		return this.points;
	}

	public int getPriority() {
		return this.priority;
	}

	public ArrayList<Area> getSubAreas() {
		return this.subAreas;
	}

	public void updateCompletion() {
		this.completion += 5.0d + (10.0d - 5.0d) * new Random().nextDouble();
		this.completion = Math.min(this.completion, 100.0d);
	}
}
