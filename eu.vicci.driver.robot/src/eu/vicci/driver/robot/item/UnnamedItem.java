package eu.vicci.driver.robot.item;

import eu.vicci.driver.robot.util.Dimensions;
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;

/**
 * An item with no further information. Used, e.g., as output
 * of object tracking operations.
 *
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public class UnnamedItem extends Item {
	public UnnamedItem(Position position, Dimensions dimensions, Orientation orientation) {
		super(position, dimensions, orientation);
	}
}
