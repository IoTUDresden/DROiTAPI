package eu.vicci.driver.robot.util;

/**
 * Represents a three-dimensional vector.
 * 
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public class Vector {
	protected double x;
	protected double y;
	protected double z;
	
	/**
	 * Use this to create a two-dimensional vector.
	 */
	public Vector(double x, double y) {
		this(x, y, 0);
	}

	/**
	 * Use this to create a three-dimensional vector.
	 */
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	public double getZ() {
		return z;
	}
	
	private static double square(double a) {
		return a * a;
	}
	
	public <T extends Vector> double calculateDistanceTo(T vector2) {
		//Straight line distance
		return Math.sqrt(square(x - vector2.x) + square(y - vector2.y) + square(z - vector2.z));
	}

	protected void normalize() {
		double length = getLength();
		
		scale(1/length);
	}
	
	protected void scale(double scalar) {
		x *= scalar;
		y *= scalar;
		z *= scalar;
	}
	
	protected double getLength() {
		return Math.sqrt(square(x) + square(y) + square(z));
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Vector)) {
			return false;
		}
		
		Vector vector2 = (Vector) other;
		
		return (x == vector2.x && y == vector2.y && z == vector2.z);
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " (" + x + ", " + y + ", " + z + ")";
	}
}
