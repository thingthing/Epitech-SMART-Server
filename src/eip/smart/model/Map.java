package eip.smart.model;

import java.util.ArrayList;

/**
 * Created by Pierre Demessence on 10/10/2014.
 */
public class Map {
    private int                 priority;
    private ArrayList<Polygon>  areaToMap;
    private ArrayList<Point>    points;

    public boolean contains(Point point) {
        for (Polygon polygon : this.areaToMap)
            if (polygon.includes(point))
                return (true);
        return (false);
    }
}
