package eu.vicci.driver.youbot;

import eu.vicci.driver.robot.exception.CannotPositionGrabberException;
import eu.vicci.driver.robot.exception.GrabbingTargetOutOfReachException;
import eu.vicci.driver.robot.exception.GrabbingTargetTooLargeException;
import eu.vicci.driver.robot.grabbing.Grabber;
import eu.vicci.driver.robot.grabbing.GrabbingTarget;
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;

public class YouBotGrabber extends Grabber {

	public YouBotGrabber(String host, int port) {
		super(host, port);
	}

	public YouBotGrabber(String name, String host, int port) {
		super(name, host, port);
	}
	
	//TODO: Christoph: Lift this method
	@Override
	protected boolean doConnect(String host, int port) {
		//TODO: GrabbingGuy: Implement this
		return false;
	}

	//TODO: Christoph: Lift this method
	@Override
	protected boolean doDisconnect() {
		//TODO: GrabbingGuy: Implement this
		return false;
	}
	
	//TODO: Christoph: Lift this method
	@Override
	protected void checkGrabbingTargetNotTooLarge(GrabbingTarget grabbingTarget) throws GrabbingTargetTooLargeException {
		//Check whether there is a position in which the grabber can take the specified
		//target to hold its full dimensions.
		
		//TODO: GrabbingGuy: Insert check whether grabber is not large enough
//		if (!true) {
//			throw new GrabbingTargetTooLargeException(grabbingTarget);
//		}
	}
	
	//TODO: Christoph: Lift this method
	@Override
	protected void checkGrabbingTargetWithinReach(GrabbingTarget grabbingTarget) throws GrabbingTargetOutOfReachException {
		//Check whether arm is long enough to reach the specified coordinates.
		
		//TODO: GrabbingGuy: Insert check whether grabber can reach grabbing target
//		if (!true) {
//			throw new GrabbingTargetOutOfReachException(grabbingTarget);
//		}
	}
	
	//TODO: Christoph: Lift this method
	@Override
	public Position getPosition() {
		//TODO: GrabbingGuy: Implement this to return position of "hand"
		return null;
	}

	//TODO: Christoph: Lift this method
	@Override
	public Orientation getOrientation() {
		//TODO: GrabbingGuy: Implement this to return orientation of "hand"
		return null;
	}
	

	@Override
	protected void doPositionGrabber(Position position, Orientation orientation) throws CannotPositionGrabberException {
		//NOTE: Position MAY be null. In that case, the current position of the grabber is to be maintained and only the orientation is to be changed.
		
		//TODO: GrabbingGuy: Implement this
		//Use throw new CannotPositionGrabberException(position, orientation); where appropriate
	}
	
	@Override
	protected void doOpenGrabber() {
		//TODO: GrabbingGuy: Implement this
	}

	@Override
	protected void doCloseGrabber() {
		//TODO: GrabbingGuy: Implement this
	}
	
	@Override
	protected void doStopGrabbing() {
		//TODO: GrabbingGuy: Cancel grabbing attempt!
	}
}
