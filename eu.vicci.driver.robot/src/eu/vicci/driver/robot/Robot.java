package eu.vicci.driver.robot;

import eu.vicci.driver.robot.exception.CannotMoveToMovementTargetException;
import eu.vicci.driver.robot.exception.CannotPositionGrabberException;
import eu.vicci.driver.robot.exception.GrabberNotPresentException;
import eu.vicci.driver.robot.exception.GrabbingTargetOutOfReachException;
import eu.vicci.driver.robot.exception.GrabbingTargetTooLargeException;
import eu.vicci.driver.robot.exception.NotConnectedException;
import eu.vicci.driver.robot.exception.StillHoldingGrabbingTargetException;
import eu.vicci.driver.robot.exception.UnknownRobotException;
import eu.vicci.driver.robot.grabbing.Grabber;
import eu.vicci.driver.robot.grabbing.GrabbingTarget;
import eu.vicci.driver.robot.grabbing.RemoteControlledGrabbing;
import eu.vicci.driver.robot.grabbing.SemiAutomaticGrabbing;
import eu.vicci.driver.robot.location.Location;
import eu.vicci.driver.robot.movement.Engine;
import eu.vicci.driver.robot.movement.MovementTarget;
import eu.vicci.driver.robot.movement.RemoteControlledMovement;
import eu.vicci.driver.robot.movement.SemiAutomaticMovement;
import eu.vicci.driver.robot.tools.RobotIdentifier;
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;
import eu.vicci.driver.robot.util.Speed;


/**
 * Represents complex robots consisting of an engine and, possibly, a grabber.
 * Class serves as facade to the subsystems and acts as proxy that forwards calls
 * to these subsystems where appropriate.
 *
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public class Robot implements NamedEntity, RoboticDevice, MovementTarget, SemiAutomaticMovement, RemoteControlledMovement, SemiAutomaticGrabbing, RemoteControlledGrabbing {
	private String name;
	
	private Engine engine;
	private Grabber grabber;
	
	public Robot(String host) throws UnknownRobotException{
		this(Robot.DEFAULT_NAME, host, Engine.DEFAULT_PORT);
	}

	public Robot(String host, int port) throws UnknownRobotException{
		this(Robot.DEFAULT_NAME, host, port);
	}

	public Robot(String name, String host) throws UnknownRobotException{
		this(name, host, Engine.DEFAULT_PORT);
	}

	public Robot(String name, String host, int port) throws UnknownRobotException{
		this(name,RobotIdentifier.getInstance().getRobotEngineByIP(host, port),RobotIdentifier.getInstance().getRobotGrabberByIP(host, port));
	}
	
	protected Robot(Engine engine) {
		initialize(DEFAULT_NAME, engine, null);
	}

	public Robot(Engine engine, Grabber grabber) {
		initialize(DEFAULT_NAME, engine, grabber);
	}
	
	public Robot(String name, Engine engine) {
		initialize(name, engine, null);
	}
	
	public Robot(String name, Engine engine, Grabber grabber) {
		initialize(name, engine, grabber);
	}

	private void initialize(String name, Engine engine, Grabber grabber) {
		this.name = name;
		this.engine = engine;
		this.grabber = grabber;
	}
	
	@Override
	public void connect() throws NotConnectedException {
		//NOTE: This might be problematic if only one fails.
		if(engine!=null) engine.connect();
		
		if (hasGrabber()) {
			grabber.connect();
		}
	}

	@Override
	public void disconnect() throws NotConnectedException {
		//NOTE: This might be problematic if only one fails.
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 		
		engine.disconnect();
		
		if (hasGrabber()) {
			grabber.disconnect();
		}
	}

	@Override
	public boolean getIsConnected() {
		return engine!=null && engine.getIsConnected() && (!hasGrabber() || grabber.getIsConnected());
	}

	public Location getLocation() {
		return engine.getLocation();
	}
	
	/**
	 * Initialize or correct the location on the map, the robot currently
	 * thinks it is located at.
	 * 
	 * @param location The (correct) location of the robot on the map.
	 * 
	 * @throws NotConnectedException Thrown if there currently is no connection to the device.
	 */
	public void setLocation(Location location) throws NotConnectedException {
		engine.setLocation(location);
	}
	
	@Override
	public Position getPosition() {
		Location location = getLocation();
		return location.getPosition();
	}

	@Override
	public Orientation getOrientation() {
		Location location = getLocation();
		return location.getOrientation();
	}

	
	@Override
	public void moveTo(MovementTarget movementTarget) throws CannotMoveToMovementTargetException, NotConnectedException {
		engine.moveTo(movementTarget);
	}

	@Override
	public void moveTo(Position position, Orientation orientation) throws CannotMoveToMovementTargetException, NotConnectedException {
		engine.moveTo(position, orientation);
	}
	
	@Override
	public void moveToNonblocking(MovementTarget movementTarget) throws CannotMoveToMovementTargetException, NotConnectedException{
		engine.moveToNonblocking(movementTarget);
	
	}

	@Override
	public void move(Speed speed, double curveRadius) throws NotConnectedException {
		engine.move(speed, curveRadius);
	}
	
	@Override
	public void stopMovement() throws NotConnectedException {
		engine.stopMovement();
	}
	

	public boolean hasGrabber() {
		return grabber != null;
	}
	
	protected void checkGrabberPresent() throws GrabberNotPresentException {
		if (!hasGrabber()) {
			throw new GrabberNotPresentException(this);
		}	
	}
	
//	@Override
//	public void moveGrabber(Direction direction, Speed speed) throws GrabberNotPresentException, CannotPositionGrabberException, NotConnectedException {
//		checkGrabberPresent();
//		grabber.moveGrabber(direction, speed);
//	}
	
	@Override
	public void positionGrabber(GrabbingTarget grabbingTarget) throws GrabberNotPresentException, CannotPositionGrabberException, NotConnectedException {
		checkGrabberPresent();
		grabber.positionGrabber(grabbingTarget);
	}

	@Override
	public void positionGrabber(Position position, Orientation orientation) throws GrabberNotPresentException, CannotPositionGrabberException, NotConnectedException {
		checkGrabberPresent();
		grabber.positionGrabber(position, orientation);
	}

	@Override
	public void positionGrabber(Orientation orientation) throws GrabberNotPresentException, CannotPositionGrabberException, NotConnectedException {
		checkGrabberPresent();
		grabber.positionGrabber(orientation);
	}

	@Override
	public void closeGrabber() throws GrabberNotPresentException, NotConnectedException {
		checkGrabberPresent();
		grabber.closeGrabber();
	}

	@Override
	public void grab(GrabbingTarget grabbingTarget) throws GrabberNotPresentException, CannotPositionGrabberException, StillHoldingGrabbingTargetException, GrabbingTargetTooLargeException, GrabbingTargetOutOfReachException, NotConnectedException {
		checkGrabberPresent();
		grabber.grab(grabbingTarget);
	}

	@Override
	public void openGrabber() throws GrabberNotPresentException, NotConnectedException {
		checkGrabberPresent();
		grabber.openGrabber();
	}
	
	@Override
	public void stopGrabbing() throws GrabberNotPresentException, NotConnectedException {
		checkGrabberPresent();
		grabber.stopGrabbing();
	}

	@Override
	public boolean holdsGrabbingTarget() {
		if (hasGrabber()) {
			return grabber.holdsGrabbingTarget();
		}

		return false;
	}

	@Override
	public GrabbingTarget getHeldGrabbingTarget() {
		if (hasGrabber()) {
			return grabber.getHeldGrabbingTarget();
		}
		
		return null;
	}


	@Override
	public void emergencyStop() throws NotConnectedException {
		engine.emergencyStop();
		
		if (hasGrabber()) {
			grabber.emergencyStop();
		}
	}
	
	@Override
	public String getName() {
		return name;
	}

	public Engine getEngine() {
		return engine;
	}

	protected Grabber getGrabber() {
		return grabber;
	}
}
