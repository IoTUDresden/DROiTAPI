package eu.vicci.driver.robot.util.rosmsg;

import eu.vicci.driver.robot.location.UnnamedLocation;
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;

public class PoseMsg {

	private PositionMsg position;
	private OrientationMsg orientation;
	
	public PoseMsg(){
	}

	public PositionMsg getPosition() {
		return position;
	}

	public void setPosition(PositionMsg position) {
		this.position = position;
	}

	public OrientationMsg getOrientation() {
		return orientation;
	}

	public void setOrientation(OrientationMsg orientation) {
		this.orientation = orientation;
	}
	
	public UnnamedLocation toUnnamedLocation(){
		Position p = new Position(position.getX(), position.getY(), position.getZ());
		Orientation o = new Orientation(orientation.getX(), orientation.getY(), orientation.getZ(), orientation.getW());
		return new UnnamedLocation(p, o);
	}

	@Override
	public String toString() {
		return "PoseMsg [position=" + position + ", orientation=" + orientation
				+ "]";
	}
	
}
