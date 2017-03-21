package eu.vicci.driver.robot.item;

import eu.vicci.driver.robot.grabbing.GrabbingTarget;
import eu.vicci.driver.robot.movement.MovementTarget;
import eu.vicci.driver.robot.util.Dimensions;
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;

/**
 * Base class of all kinds of items representing "small things" of the real world.
 *
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public abstract class Item implements GrabbingTarget, MovementTarget {
	private Position position;
	private Dimensions dimensions;
	private Orientation orientation;
	
	public Item(Position position, Dimensions dimensions, Orientation orientation) {
		this.position = position;
		this.dimensions = dimensions;
		this.orientation = orientation;
	}
	
	@Override
	public Position getPosition() {
		return position;
	}
	
	@Override
	public Dimensions getDimensions() {
		return dimensions;
	}
	
	@Override
	public Orientation getOrientation() {
		return orientation;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " " + getPosition() + " with dimensions " + getDimensions() + " towards "  + getOrientation();
	}
}
