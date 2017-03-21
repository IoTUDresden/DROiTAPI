package eu.vicci.driver.turtlebot.location;

import java.util.List;

import eu.vicci.driver.robot.location.DockingStation;
import eu.vicci.driver.robot.location.Location;
import eu.vicci.driver.robot.location.LocationManager;
import eu.vicci.driver.robot.location.NamedLocation;
import eu.vicci.driver.robot.location.UnnamedLocation;
import eu.vicci.driver.robot.location.serialization.NamedLocationPacker;
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;

/**
 * A docking station used to charge the TurtleBot.
 * 
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public class TurtleBotDockingStation extends DockingStation {
	static {
		//Register a packer to save/load this type of location
		LocationManager.registerLocationPacker(TurtleBotDockingStation.class, new NamedLocationPacker() {
			@Override
			protected NamedLocation unpackAttributes(String name, Position position, Orientation orientation, List<String> wrappedAttributes) {
				return new TurtleBotDockingStation(name, position, orientation);
			}
		});
	}
	
	// This is the distance (in meters) from the docking station to the position where the robot starts the docking procedure
	private static double dockingDistance = 0.8; 
	
	public TurtleBotDockingStation(String name, Position position, Orientation orientation) {
		super(name, position, orientation);
	}
	
	public Location calculateDrivingTargetLocation() {
		Position position = getPosition();
		Orientation orientation = getOrientation();
		
		double angle = orientation.toAngle();
		
		double effectiveX = position.getX() - dockingDistance * Math.cos(angle);
		double effectiveY = position.getY() - dockingDistance * Math.sin(angle);
		
		Position effectivePosition = new Position(effectiveX, effectiveY);
		
		return new UnnamedLocation(effectivePosition, orientation);
	}
}
