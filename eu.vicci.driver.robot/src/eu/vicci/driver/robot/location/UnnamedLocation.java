package eu.vicci.driver.robot.location;

import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;

/**
 * A location with no further information. Used, e.g., for remote control
 * when a user clicks on an arbitrary spot on visual representation of the map.
 * 
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public class UnnamedLocation extends Location {
	public UnnamedLocation(Position position, Orientation orientation) {
		super(position, orientation);
	}
	
}
