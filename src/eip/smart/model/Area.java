package eip.smart.model;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Pierre Demessence on 10/10/2014.
 */
public class Area {
    private int priority = 0;
    private ArrayList<Polygon> areaToMap = new ArrayList<Polygon>();
    private ArrayList<Point> points = new ArrayList<Point>();
    private ArrayList<Area> subAreas = new ArrayList<Area>();
    private ArrayList<Agent.AgentType> capableAgentTypes = new ArrayList<Agent.AgentType>();
    private double completion = 0.0d;

    public int getPriority() {
        return priority;
    }

    public ArrayList<Polygon> getAreaToMap() {
        return areaToMap;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public ArrayList<Area> getSubAreas() {
        return subAreas;
    }

    public ArrayList<Agent.AgentType> getCapableAgentTypes() {
        return capableAgentTypes;
    }

    public boolean contains(Point point) {
        for (Polygon polygon : this.areaToMap)
            if (polygon.includes(point))
                return (true);
        return (false);
    }

    public double getCompletion() {
        return (completion);
    }

    public void updateCompletion() {
        this.completion += 5.0d + (10.0d - 5.0d) * new Random().nextDouble();
        this.completion = Math.min(this.completion, 100.0d);
    }
}
