package eu.vicci.driver.robot.location;

import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;

/**
 * Abstract base class for docking stations used to recharge robots.
 *
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public abstract class DockingStation extends NamedLocation {
	protected DockingStation(String name, Position position, Orientation orientation) {
		super(name, position, orientation);
	}
}
