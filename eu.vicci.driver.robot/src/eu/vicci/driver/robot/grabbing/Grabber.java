package eu.vicci.driver.robot.grabbing;

import java.security.InvalidParameterException;

import eu.vicci.driver.robot.AbstractRoboticDevice;
import eu.vicci.driver.robot.NamedEntity;
import eu.vicci.driver.robot.exception.CannotPositionGrabberException;
import eu.vicci.driver.robot.exception.GrabberNotPresentException;
import eu.vicci.driver.robot.exception.GrabbingTargetOutOfReachException;
import eu.vicci.driver.robot.exception.GrabbingTargetTooLargeException;
import eu.vicci.driver.robot.exception.NotConnectedException;
import eu.vicci.driver.robot.exception.StillHoldingGrabbingTargetException;
import eu.vicci.driver.robot.movement.MovementTarget;
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;

//TODO: Christoph: Take into consideration that the grabber may be mounted to a robot, which influences coordinates!

/**
 * Represents a robotic device used for grabbing things, e.g., a robotic arm.
 * 
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public abstract class Grabber extends AbstractRoboticDevice implements NamedEntity, MovementTarget, SemiAutomaticGrabbing, RemoteControlledGrabbing {
	//TODO: GrabbingGuy: Change this if necessary
	public static final int DEFAULT_PORT = 9090;
	
	private String name;
	private GrabbingTarget heldGrabbingTarget = null;
	
	public Grabber(String host, int port) {
		this(DEFAULT_NAME, host, port);
	}

	public Grabber(String name, String host, int port) {
		super(host, port);
		
		this.name = name;
	}

	/**
	 * Check whether the grabber is able to grab the specified grabbing target and throw an exception if it is not.
	 *  
	 * @param grabbingTarget The target to grab.
	 * 
	 * @throws StillHoldingGrabbingTargetException Thrown if the grabber is still holding another grabbing target.
	 * @throws GrabbingTargetTooLargeException Thrown if the dimensions of the grabbing target extend beyond the maximum dimensions grabbable by the grabber.
	 * @throws GrabbingTargetOutOfReachException Thrown if the grabbing target cannot be reached by the grabber, e.g., because a robotic arm cannot be extended far enough.
	 */
	protected void checkCanGrabGrabbingTarget(GrabbingTarget grabbingTarget) throws StillHoldingGrabbingTargetException, GrabbingTargetTooLargeException, GrabbingTargetOutOfReachException {
		if (holdsGrabbingTarget()) {
			//Automatic release of grabbed target might be dangerous.
			//Release grabbed target manually before continuing.
			GrabbingTarget heldGrabbingTarget = getHeldGrabbingTarget();
			throw new StillHoldingGrabbingTargetException(heldGrabbingTarget);
		}
		
		checkGrabbingTargetNotTooLarge(grabbingTarget);
		checkGrabbingTargetWithinReach(grabbingTarget);
	}
	
	/**
	 * Check whether the dimensions of grabbing target can principally be grabbed by the grabber and throw an exception if they cannot.
	 * 
	 * @param grabbingTarget The target to grab.
	 * 
	 * @throws GrabbingTargetTooLargeException Thrown if the dimensions of the grabbing target extend beyond the maximum dimensions grabbable by the grabber.
	 */
	protected abstract void checkGrabbingTargetNotTooLarge(GrabbingTarget grabbingTarget) throws GrabbingTargetTooLargeException;
	
	/**
	 * Check whether the grabber can reach the grabbing target and throw an exception if it cannot.
	 * 
	 * @param grabbingTarget The target to grab.
	 * 
	 * @throws GrabbingTargetOutOfReachException Thrown if the grabbing target cannot be reached by the grabber, e.g., because a robotic arm cannot be extended far enough.
	 */
	protected abstract void checkGrabbingTargetWithinReach(GrabbingTarget grabbingTarget) throws GrabbingTargetOutOfReachException;
	
	@Override
	public void positionGrabber(GrabbingTarget grabbingTarget) throws CannotPositionGrabberException, NotConnectedException {
		Position position = grabbingTarget.getPosition();
		Orientation orientation = grabbingTarget.getOrientation();
		
		positionGrabber(position, orientation);
	}

	@Override
	public void positionGrabber(Orientation orientation) throws CannotPositionGrabberException, NotConnectedException {
		positionGrabber(null, orientation);
	}

	@Override
	public void positionGrabber(Position position, Orientation orientation) throws CannotPositionGrabberException, NotConnectedException {
		checkConnected();
		
		if (orientation == null) {
			throw new InvalidParameterException("Orientation must not be null");
		}
		
		doPositionGrabber(position, orientation);
	}
	
	protected abstract void doPositionGrabber(Position position, Orientation orientation) throws CannotPositionGrabberException;
	
	@Override
	public void grab(GrabbingTarget grabbingTarget) throws GrabberNotPresentException, CannotPositionGrabberException, StillHoldingGrabbingTargetException, GrabbingTargetTooLargeException, GrabbingTargetOutOfReachException, NotConnectedException {
		checkConnected();
		checkCanGrabGrabbingTarget(grabbingTarget);
		
		positionGrabber(grabbingTarget);
		closeGrabber();
		
		heldGrabbingTarget = grabbingTarget;
	}
	
	@Override
	public boolean holdsGrabbingTarget() {
		return (heldGrabbingTarget != null);
	}
	
	@Override
	public GrabbingTarget getHeldGrabbingTarget() {
		return heldGrabbingTarget;
	}
	
	@Override
	public void openGrabber() throws GrabberNotPresentException, NotConnectedException{
		checkConnected();
		doOpenGrabber();
		heldGrabbingTarget = null;
	}
	
	protected abstract void doOpenGrabber();
	
	@Override
	public void closeGrabber() throws GrabberNotPresentException, NotConnectedException {
		checkConnected();
		doCloseGrabber();
	}
	
	protected abstract void doCloseGrabber();
	
	@Override
	public void stopGrabbing() throws NotConnectedException {
		checkConnected();
		doStopGrabbing();
	}
	
	protected abstract void doStopGrabbing();
	
	@Override
	protected void doEmergencyStop() {
		doStopGrabbing();
	}

	@Override
	public String getName() {
		return name;
	}
}
