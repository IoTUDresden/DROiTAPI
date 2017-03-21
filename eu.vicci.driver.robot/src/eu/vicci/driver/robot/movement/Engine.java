package eu.vicci.driver.robot.movement;

import eu.vicci.driver.robot.AbstractRoboticDevice;
import eu.vicci.driver.robot.exception.CannotMoveToMovementTargetException;
import eu.vicci.driver.robot.exception.NotConnectedException;
import eu.vicci.driver.robot.location.Location;
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;
import eu.vicci.driver.robot.util.RobotJavaToRosbridge;
import eu.vicci.driver.robot.util.Speed;
import eu.vicci.driver.robot.util.Vector;

/**
 * Represents the moveable base of a robot etc.
 * 
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public abstract class Engine extends AbstractRoboticDevice implements SemiAutomaticMovement, RemoteControlledMovement {

	public static final int DEFAULT_PORT = 9090;
	public static final int CONNECTION_TIMEOUT_IN_MS = 5000;
	
	private RobotJavaToRosbridge socket;
	private Thread webSocketThread;

	public Engine(String host) {
		super(host, DEFAULT_PORT);
	}

	public Engine(String host, int port) {
		super(host, port);
	}

	protected RobotJavaToRosbridge createJava2Rosbridge(String host, int port) {
		return new RobotJavaToRosbridge(host, port);
	}

	@Override
	protected boolean doConnect(String host, int port) {
		socket = createJava2Rosbridge(host, port);
		webSocketThread = new Thread(socket);
		webSocketThread.start();
		long timeOfTimerStart = System.currentTimeMillis();
		while(!socket.isConnected()){
			if(isTimeoutReached(timeOfTimerStart)){
				return false;
			} else {
				Thread.yield();
			}
		}
		return true;
	}
	
	private boolean isTimeoutReached(long timeOfTimerStart){
		return (System.currentTimeMillis()-timeOfTimerStart)>CONNECTION_TIMEOUT_IN_MS;
	}

	@Override
	protected boolean doDisconnect() {
		webSocketThread.interrupt();
		try {
			socket.closeBlocking();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return true;
	}

	// Location
	public Location getLocation() {
		try {
			checkConnected();
			Location latestLocation = socket.getLocation();
	
			while (latestLocation == null) {
				latestLocation = socket.getLocation();
				Thread.yield();
			}
			
			return latestLocation;
		} catch(NotConnectedException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setLocation(Location location) throws NotConnectedException {
		checkConnected();
		socket.publishInitialPose(location.getPosition(),location.getOrientation());
		try {
			// This pause is necessary otherwise it doesn't work.
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void moveTo(MovementTarget movementTarget) throws CannotMoveToMovementTargetException, NotConnectedException {
		//Simply drive to the given position with the given orientation.
		Position position = movementTarget.getPosition();
		Orientation orientation = movementTarget.getOrientation();
		moveTo(position, orientation);
	}

	@Override
	public abstract void moveTo(Position position, Orientation orientation) throws CannotMoveToMovementTargetException, NotConnectedException;
	
	@Override
	public void move(Speed speed, double curveRadius)  throws NotConnectedException {
		checkConnected();
				
		if(curveRadius > 1) curveRadius = 1;
		if(curveRadius < -1) curveRadius = -1;
		
		//TODO: Dimitri: Get a sensible value for the maximum speed of the engine from somewhere. Measuring it and setting it as a constant would be OK, but it requires specialized subclasses of the engine.
		final double maximumRawSpeed = getRawMaximumSpeed(); //in meters/second (1.39 meters/second = ~5 kph)
		
		double rawSpeed = speed.getRawSpeed(maximumRawSpeed);
		//TODO: Dimitri: Check these calculations. You're the expert! :)
		
		//linearVelocity.x means moving forth/back
		//linearVelocity.y means strafing left/right
		//linearVelocity.z is unused
		
		//in meters/second
		double linearVelocityX = rawSpeed;
		if(speed.getBackward()) rawSpeed = -rawSpeed;
		//NOTE: We do not support strafing right now.
		double linearVelocityY = 0.0;
		Vector linearVelocity = new Vector(linearVelocityX, linearVelocityY, 0); 

		//angularVelocity.x is unused (in 2D)
		//angularVelocity.y is unused (in 2D)
		//angularVelocity.z means rotation speed around own axis
		
		//in radians/second
		//TODO: Is this correct? Should he sign be factored into that? 
		//double angularVelocityZ = curveRadius == 0 ? 0 : (rawSpeed / curveRadius);
		// Dimitri: yes we need a sign but it already comes from curveRadius now.
		
		double angularVelocityZ = getRawMaximumAngularSpeed()*curveRadius;
		
		Vector angularVelocity = new Vector(0, 0, angularVelocityZ);
		
		doMove(linearVelocity, angularVelocity);
	}
	
	protected abstract void doMove(Vector linearVelocity, Vector angularVelocity);
	
	protected abstract double getRawMaximumSpeed();
	
	protected abstract double getRawMaximumAngularSpeed();
	
	@Override
	public void stopMovement() throws NotConnectedException {
		checkConnected();
		doStopMovement();
	}

	protected boolean doStopMovement() {
		socket.cancelMoveTo();
		return true;
	}
	
	@Override
	protected void doEmergencyStop() {
		doStopMovement();
	}

	protected RobotJavaToRosbridge getSocket() {
		return socket;
	}

	public Thread getWebSocketThread() {
		return webSocketThread;
	}

	@Override
	public void moveToNonblocking(MovementTarget movementTarget)
			throws CannotMoveToMovementTargetException, NotConnectedException {
		
	}
	
	@Override
	public boolean getIsConnected() {
		return (socket!=null) && socket.isConnected();
	}

}
