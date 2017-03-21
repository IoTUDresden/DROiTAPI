package eu.vicci.driver.robot.util;

/**
 * Wrapper class for a position in space.
 * 
 * Double values represent meters as distance from the (arbitrarily chosen) origin of the map.
 * 
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public class Position extends Vector {
	/**
	 * Use this for two-dimensional positions.
	 */
	public Position(double x, double y) {
		super(x, y);
	}
	
	/**
	 * Use this for three-dimensional positions.
	 */
	public Position(double x, double y, double z) {
		super(x, y, z);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Position) {
			return super.equals(other);
		}

		return false;
	}
}
