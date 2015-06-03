package eip.smart.server;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import eip.smart.model.geometry.Point;
import eip.smart.model.geometry.Polygon;
import eip.smart.model.geometry.Rectangle;
import eip.smart.model.geometry.Square;

public class PolygonTest {
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {}

	@Before
	public void setUp() throws Exception {}

	@After
	public void tearDown() throws Exception {}
	
	
	@Test
	public void testPerimeterSquare() {
		int size = 5;
		Polygon square = new Polygon();
		
		Point p1 = new Point(0, size, 0);
		Point p2 = new Point(size, size, 0);
		Point p3 = new Point(size, 0, 0);
		Point p4 = new Point(0, 0, 0);
		
		square.add(p1);
		square.add(p2);
		square.add(p3);
		square.add(p4);
		
		if (square.getPerimeter() != size * 4)
			Assert.fail("square Perimeter error, result found is" + square.getPerimeter());
		
		Assert.assertTrue(true);
	}
	
	
	@Test
	public void testAreaSquare() {
		int size = 5;
		Polygon square = new Polygon();

		Point p1 = new Point(0, size, 0);
		Point p2 = new Point(size, size, 0);
		Point p3 = new Point(size, 0, 0);
		Point p4 = new Point(0, 0, 0);

		square.add(p1);
		square.add(p2);
		square.add(p3);
		square.add(p4);
	
		if (square.getArea() != Math.pow(size, 2))
			Assert.fail("square Area error, result found is" + square.getArea());
		
		Assert.assertTrue(true);
	}


	@Test
	public void testPerimeterRectangle() {
		int size1 = 5;
		int size2 = 10;
		Polygon rectangle = new Polygon();

		Point p1 = new Point(0, size1, 0);
		Point p2 = new Point(size2, size1, 0);
		Point p3 = new Point(size2, 0, 0);
		Point p4 = new Point(0, 0, 0);

		rectangle.add(p1);
		rectangle.add(p2);
		rectangle.add(p3);
		rectangle.add(p4);
	

		if (rectangle.getPerimeter() != (size1 * 2 + size2 * 2))
			Assert.fail("rectangle Perimeter error, result found is" + rectangle.getPerimeter());
		
		Assert.assertTrue(true);
	}
	
	
	@Test
	public void testAreaRectangle() {
		int size1 = 5;
		int size2 = 10;
		Polygon rectangle = new Polygon();

		Point p1 = new Point(0, size1, 0);
		Point p2 = new Point(size2, size1, 0);
		Point p3 = new Point(size2, 0, 0);
		Point p4 = new Point(0, 0, 0);
		
		rectangle.add(p1);
		rectangle.add(p2);
		rectangle.add(p3);
		rectangle.add(p4);
	
		if (rectangle.getArea() != size1 * size2)
			Assert.fail("square Area error, result found is" + rectangle.getArea());
		
		Assert.assertTrue(true);
	}

	
	@Test
	public void testPerimeterTriangle() {
		int size = 5;
		Polygon triangle = new Polygon();
		
		Point p1 = new Point(0, size, 0);
		Point p2 = new Point(size, 0, 0);
		Point p3 = new Point(0, 0, 0);
		
		triangle.add(p1);
		triangle.add(p2);
		triangle.add(p3);

		if (triangle.getPerimeter() != (size + size + (Math.sqrt(2 * Math.pow(size, 2)))))
			Assert.fail("Triangle Perimeter error, result found is" + triangle.getPerimeter());
		
		Assert.assertTrue(true);
	}
	
	@Test
	public void testPerimeterPentagone() {
		Polygon pentagone = new Polygon();
		
		Point p1 = new Point(0, 5, 0);
		Point p2 = new Point(5, 5, 0);
		Point p3 = new Point(5, 2.5, 0);
		Point p4 = new Point(5, 0, 0);
		Point p5 = new Point(0, 0, 0);

		pentagone.add(p1);
		pentagone.add(p2);
		pentagone.add(p3);
		pentagone.add(p4);
		pentagone.add(p5);

		if (pentagone.getPerimeter() != 20)
			Assert.fail("Pentagone Perimeter error, result found is" + pentagone.getPerimeter());
		
		Assert.assertTrue(true);
	}
	
	@Test
	public void testAreaPentagone() {
		Polygon pentagone = new Polygon();
		
		Point p1 = new Point(0, 5, 0);
		Point p2 = new Point(5, 5, 0);
		Point p3 = new Point(5, 2.5, 0);
		Point p4 = new Point(5, 0, 0);
		Point p5 = new Point(0, 0, 0);

		pentagone.add(p1);
		pentagone.add(p2);
		pentagone.add(p3);
		pentagone.add(p4);
		pentagone.add(p5);

		if (pentagone.getArea() != 25)
			Assert.fail("Pentagone Area error, result found is" + pentagone.getArea());
		
		Assert.assertTrue(true);
	}

	
	@Test
	public void testAreaTriangle() {

		Polygon triangle = new Polygon();
		Point p1 = new Point(0, 5, 0);
		Point p2 = new Point(5, 0, 0);
		Point p3 = new Point(0, 0, 0);
		
		triangle.add(p1);
		triangle.add(p2);
		triangle.add(p3);

		if (triangle.getArea() != 12.5)
			Assert.fail("Triangle Area error, result found is" + triangle.getArea());
		
		Assert.assertTrue(true);
	}
	
	@Test
	public void testTrueIncludesSquare() {
		int size = 5;
		Polygon square = new Polygon();

		Point p1 = new Point(0, size, 0);
		Point p2 = new Point(size, size, 0);
		Point p3 = new Point(size, 0, 0);
		Point p4 = new Point(0, 0, 0);

		Point pointTest = new Point(0, size / 2, 0);
		
		square.add(p1);
		square.add(p2);
		square.add(p3);
		square.add(p4);
	
		if (!square.includes(pointTest))
			Assert.fail("square Includes error, result should be \"true\"");
		
		Assert.assertTrue(true);
	}
	
	@Test
	public void testFalseIncludesSquare() {
		int size = 5;
		Polygon square = new Polygon();

		Point p1 = new Point(0, size, 0);
		Point p2 = new Point(size, size, 0);
		Point p3 = new Point(size, 0, 0);
		Point p4 = new Point(0, 0, 0);

		Point pointTest = new Point(0, size + 1, 0);
		
		square.add(p1);
		square.add(p2);
		square.add(p3);
		square.add(p4);
	
		if (square.includes(pointTest))
			Assert.fail("square Includes error, result should be \"false\"");
		
		Assert.assertTrue(true);
	}

	
	@Test
	public void testTrueIncludesTriangle() {
		int size = 5;
		Polygon triangle = new Polygon();
		Point p1 = new Point(0, 5, 0);
		Point p2 = new Point(5, 0, 0);
		Point p3 = new Point(0, 0, 0);
		
		Point pointTest = new Point(0, size / 2, 0);

		triangle.add(p1);
		triangle.add(p2);
		triangle.add(p3);
		
		if (!triangle.includes(pointTest))
			Assert.fail("triangle Includes error, result should be \"true\"");		
		Assert.assertTrue(true);
	}
	
	
	@Test
	public void testfalseIncludesTriangle() {
		int size = 5;
		Polygon triangle = new Polygon();
		Point p1 = new Point(0, 5, 0);
		Point p2 = new Point(5, 0, 0);
		Point p3 = new Point(0, 0, 0);
		
		Point pointTest = new Point(0, size + 2, 0);

		triangle.add(p1);
		triangle.add(p2);
		triangle.add(p3);
		
		if (triangle.includes(pointTest))
			Assert.fail("triangle Includes error, result should be \"false\"");
		
		Assert.assertTrue(true);
	}
	
	@Test
	public void testSquare()
	{
		int size = 5;
		Polygon square = new Polygon();
		
		Point p1 = new Point(0, size, 0);
		Point p2 = new Point(size, size, 0);
		Point p3 = new Point(size, 0, 0);
		Point p4 = new Point(0, 0, 0);
		
		square.add(p1);
		square.add(p2);
		square.add(p3);
		square.add(p4);
		
		Polygon square2 = new Square(new Point(0, 0, 0), size);
		if (square2.getPoints().size() != 4)
			Assert.fail("error, wrong faces number");
		else if (square2.getPerimeter() != square.getPerimeter())
			Assert.fail("error; wrong perimeter");
		else if (square2.getArea() != square.getArea())
			Assert.fail("error; wrong area");	
		Assert.assertTrue(true);
	}

	@Test
	public void testRectangle()
	{
		int size1 = 5;
		int size2 = 10;
		
		Polygon rectangle = new Polygon();
		
		Point p1 = new Point(0, size1, 0);
		Point p2 = new Point(size2, size1, 0);
		Point p3 = new Point(size2, 0, 0);
		Point p4 = new Point(0, 0, 0);
		
		rectangle.add(p1);
		rectangle.add(p2);
		rectangle.add(p3);
		rectangle.add(p4);
		
		Polygon rectangle2 = new Rectangle(new Point(0, 0, 0), size1, size2);
		if (rectangle2.getPoints().size() != 4)
			Assert.fail("error, wrong faces number");
		else if (rectangle2.getPerimeter() != rectangle.getPerimeter())
			Assert.fail("error; wrong perimeter");
		else if (rectangle2.getArea() != rectangle.getArea())
			Assert.fail("error; wrong area");
		Assert.assertTrue(true);
	}

}
