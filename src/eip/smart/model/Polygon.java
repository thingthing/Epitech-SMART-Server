package eip.smart.model;

import java.util.ArrayList;

/**
 * Created by Pierre Demessence on 10/10/2014.
 */
public class Polygon {

	private ArrayList<Point>	points;

	public Polygon() {
		this.points = new ArrayList<>();
	}

	public Polygon(ArrayList<Point> points) {
		this.points = points;
	}

	public boolean add(Point point) {
		return this.points.add(point);
	}

	/**
	 * Return the area of the polygon using only the x and y coordinates of the points.
	 * 
	 * @return the area of the polygon.
	 */
	public double getArea() {
		double area = 0;
		int j;

		j = this.points.size() - 1;
		for (int i = 0; i < this.points.size(); i++) {
			area += (this.points.get(j).getX() + this.points.get(i).getX()) * (this.points.get(j).getY() - this.points.get(i).getY());
			j = i;
		}
		return (area / 2.0d);
	}

	public double getPerimeter() {
		double perimeter = 0;
		Point lastPoint;

		if (!this.isFinite())
			return (0);

		lastPoint = this.points.get(0);
		for (int i = 1; i < this.points.size(); i++) {
			perimeter += lastPoint.getDistance(this.points.get(i));
			lastPoint = this.points.get(i);
		}
		perimeter += lastPoint.getDistance(this.points.get(0));
		return (perimeter);
	}

	/**
	 * Checks if the
	 * 
	 * @param point
	 *            the point to check.
	 * @return true if the point is inside the polygon.
	 *         http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html#The C Code
	 */
	public boolean includes(Point point) {
		boolean res = false;
		for (int i = 0, j = this.points.size() - 1; i < this.points.size(); j = i++)
			if (((this.points.get(i).getY() > point.getY()) != (this.points.get(j).getY() > point.getY())) && (point.getX() < ((this.points.get(j).getX() - this.points.get(i).getX()) * (point.getY() - this.points.get(i).getY()) / (this.points.get(j).getY() - this.points.get(i).getY()) + this.points.get(i).getX())))
				res = !res;
		return (res);
	}

	/**
	 * Check if the polygon is "finite".
	 * 
	 * @return true if the Polygon contains at least 3 points.
	 */
	public boolean isFinite() {
		return (this.points.size() > 2);
	}
}
