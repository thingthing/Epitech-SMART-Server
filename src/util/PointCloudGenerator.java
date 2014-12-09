package util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eip.smart.model.geometry.Point;

public class PointCloudGenerator {

	public static Point generatePoint() {
		return (new Point(PointCloudGenerator.getRandomDouble(), PointCloudGenerator.getRandomDouble(), PointCloudGenerator.getRandomDouble()));
	}

	public static ArrayList<Point> generatePointCloud(int nb) {
		ArrayList<Point> cloud = new ArrayList<>();
		for (int i = 0; i < nb; i++)
			cloud.add(PointCloudGenerator.generatePoint());
		return (cloud);
	}

	public static String generatePointCloudJSON(int nb) {
		String json = "";
		ObjectMapper mapper = new ObjectMapper();

		try {
			json = mapper.writeValueAsString(PointCloudGenerator.generatePointCloud(nb));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return (json);
	}

	private static double getRandomDouble() {
		double res;
		if (PointCloudGenerator.rng == null)
			PointCloudGenerator.rng = new Random();
		res = (PointCloudGenerator.MIN + PointCloudGenerator.rng.nextDouble() * ((PointCloudGenerator.MAX - PointCloudGenerator.MIN) + 1));
		res = new BigDecimal(res).setScale(PointCloudGenerator.PRECISION, BigDecimal.ROUND_HALF_UP).doubleValue();
		return (res);
	}

	private static Random	rng			= null;

	private static int		PRECISION	= 3;
	private static double	MIN			= -42.0d;
	private static double	MAX			= 42.0d;
}
