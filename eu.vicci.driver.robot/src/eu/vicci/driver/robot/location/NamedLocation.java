package eu.vicci.driver.robot.location;

import java.security.InvalidParameterException;

import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;

/**
 * A location with a specific name.
 *
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public abstract class NamedLocation extends Location {
	private String name;
	
	protected NamedLocation(String name, Position position, Orientation orientation) {
		super(position, orientation);
		
		checkNamedLocationName(name);
		this.name = name;
	}

	public static void checkNamedLocationName(String name) {
		if (name == null || name == null) {
			throw new InvalidParameterException("Named location name may not be null or empty.");	
		}
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof NamedLocation)) {
			return false;
		}
		
		NamedLocation namedLocation2 = (NamedLocation) other;

		if (name.equals(namedLocation2.name)) {
			return super.equals(other);
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " \"" + name + "\" at " + getPosition() + " towards "  + getOrientation();
	}
}
