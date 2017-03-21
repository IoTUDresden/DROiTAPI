package eu.vicci.driver.robot.movement;

import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;

/**
 * Common interface for all classes serving as target of a semi-automatic movement.
 * 
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public interface MovementTarget {
	public Position getPosition();
	public Orientation getOrientation();
}
