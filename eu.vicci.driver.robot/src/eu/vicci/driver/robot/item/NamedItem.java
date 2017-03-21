package eu.vicci.driver.robot.item;

import eu.vicci.driver.robot.util.Dimensions;
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;

/**
 * An item with a specific name. Used, e.g., as output
 * of image recognition operations.
 *
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public class NamedItem extends Item {
	private String name;
	
	public NamedItem(String name, Position position, Dimensions dimensions, Orientation orientation) {
		super(position, dimensions, orientation);
		
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " \"" + name + "\" at " + getPosition() + " with dimensions " + getDimensions() + " towards "  + getOrientation();
	}
}
