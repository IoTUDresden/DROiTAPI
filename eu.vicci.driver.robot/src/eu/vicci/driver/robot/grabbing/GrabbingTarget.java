package eu.vicci.driver.robot.grabbing;

import eu.vicci.driver.robot.util.Dimensions;
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;

/**
 * Common interface for all classes serving as target of a semi-automatic grab operation.
 * 
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public interface GrabbingTarget {
	public Position getPosition();
	public Orientation getOrientation();
	public Dimensions getDimensions();
}
