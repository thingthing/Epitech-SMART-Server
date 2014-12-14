package eip.smart.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eip.smart.model.geometry.Point;

public class PointCloudGenerator {

	private Random	rng			= null;

	private int		precision	= 3;
	private double	min			= -42.0d;
	private double	max			= 42.0d;

	public PointCloudGenerator() {}

	public PointCloudGenerator(int precision, double min, double max) {
		this.precision = precision;
		this.min = min;
		this.max = max;
	}

	private Point generatePoint() {
		return (new Point(this.getRandomDouble(), this.getRandomDouble(), this.getRandomDouble()));
	}

	public ArrayList<Point> generatePointCloud(int nb) {
		ArrayList<Point> cloud = new ArrayList<>();
		for (int i = 0; i < nb; i++)
			cloud.add(this.generatePoint());
		return (cloud);
	}

	public String generatePointCloudJSON(int nb) {
		String json = "";
		ObjectMapper mapper = new ObjectMapper();

		try {
			json = mapper.writeValueAsString(this.generatePointCloud(nb));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return (json);
	}

	private double getRandomDouble() {
		double res;
		if (this.rng == null)
			this.rng = new Random();
		res = (this.min + this.rng.nextDouble() * ((this.max - this.min) + 1));
		res = new BigDecimal(res).setScale(this.precision, BigDecimal.ROUND_HALF_UP).doubleValue();
		return (res);
	}
}
