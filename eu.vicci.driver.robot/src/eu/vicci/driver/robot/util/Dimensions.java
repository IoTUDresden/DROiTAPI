package eu.vicci.driver.robot.util;

/**
 * Wrapper class for the dimensions of an object in space.
 * Width is x-size, height is y-size and depth is z-size.
 * 
 * Double values represent meters.
 * 
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public class Dimensions {
	private double width;
	private double height;
	private double depth;
	
	public Dimensions(double width, double height) {
		this(width, height, 0);
	}

	public Dimensions(double width, double height, double depth) {
		this.width = width;
		this.height = height;
		this.depth = depth;
	}
	
	public double getX() {
		return width;
	}

	public double getY() {
		return height;
	}
	
	public double getZ() {
		return depth;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " (" + width + ", " + height + ", " + depth + ")";
	}
}
