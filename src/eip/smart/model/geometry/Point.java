package eip.smart.model.geometry;

/**
 * Created by Pierre Demessence on 09/10/2014.
 */
public class Point {

	public static Point add(Point p1, Point p2) {
		return new Point(p1.getX() + p2.getX(), p1.getY() + p2.getY(), p1.getZ() + p2.getZ());
	}

	public static Point tranlate(Point p, double d) {
		return Point.add(p, new Point(d));
	}

	private double	x	= 0;
	private double	y	= 0;
	private double	z	= 0;

	public Point(double c) {
		this.x = c;
		this.y = c;
		this.z = c;
	}

	public Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double getDistance(Point p) {
		return (Math.sqrt(Math.pow(this.x - p.x, 2) + Math.pow(this.y - p.y, 2) + Math.pow(this.z - p.z, 2)));
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public double getZ() {
		return this.z;
	}

	@Override
	public String toString() {
		return "(" + this.getX() + ";" + this.getY() + ";" + this.getZ() + ")";
	}
}
