package eu.vicci.driver.robot.util;

/**
 * Wrapper class for an orientation in space represented as quaternion.
 * 
 * Double values represent meters.
 * 
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public class Orientation {
	private double o_x;
	private double o_y;
	private double o_z;
	private double o_w;
	
	/**
	 * Use this for two-dimensional orientations. Parameters describe two-dimensional portion of a quaternion.
	 */
	public Orientation(double o_z, double o_w) {
		this(0, 0, o_z, o_w);
	}

	/**
	 * Use this for three-dimensional orientations. Parameters describe a quaternion.
	 */
	public Orientation(double o_x, double o_y, double o_z, double o_w) {
		this.o_x = o_x;
		this.o_y = o_y;
		this.o_z = o_z;
		this.o_w = o_w;
	}

	public double getO_X() {
		return o_x;
	}

	public double getO_Y() {
		return o_y;
	}
	
	public double getO_Z() {
		return o_z;
	}

	public double getO_W() {
		return o_w;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Orientation)) {
			return false;
		}
	
		Orientation orientation2 = (Orientation) other;
		
		return (o_x == orientation2.o_x && o_y == orientation2.o_y && o_z == orientation2.o_z && o_w == orientation2.o_w);
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " (" + o_x + ", " + o_y + ", " + o_z + ", " + o_w + ")";
	}
	
	/**
	 * @return The Euler angle of the two-dimensional portion of this quaternion in RAD.
	 */
	public double toAngle() {
		return  Math.atan2((2 * o_z * o_w), (1 - 2 * o_z * o_z));
	}
}
