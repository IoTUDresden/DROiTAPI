package eu.vicci.driver.robot.location;

import eu.vicci.driver.robot.movement.MovementTarget;
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;

/**
 * Base class of all kinds of locations representing a position on the map.
 * 
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
//TODO: Christoph: maybe orientation should be pushed down to TurtleBotDockingStation? Check MovementTarget interface.
public abstract class Location implements MovementTarget {
	private Position position;
	private Orientation orientation;
	
	protected Location(Position position, Orientation orientation) {
		this.position = position;
		this.orientation = orientation;
	}
	
	@Override
	public Position getPosition() {
		return position;
	}
	
	@Override
	public Orientation getOrientation() {
		return orientation;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Location)) {
			return false;
		}
		
		Location location2 = (Location) other;

		return (position.equals(location2.position) && orientation.equals(location2.orientation));
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + position + " towards "  + orientation;
	}
}
