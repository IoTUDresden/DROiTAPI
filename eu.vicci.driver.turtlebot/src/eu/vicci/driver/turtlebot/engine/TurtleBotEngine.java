package eu.vicci.driver.turtlebot.engine;

import eu.vicci.driver.robot.exception.CannotMoveToMovementTargetException;
import eu.vicci.driver.robot.exception.NotConnectedException;
import eu.vicci.driver.robot.location.Location;
import eu.vicci.driver.robot.movement.Engine;
import eu.vicci.driver.robot.movement.MovementTarget;
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;
import eu.vicci.driver.robot.util.RobotJavaToRosbridge;
import eu.vicci.driver.robot.util.Vector;
import eu.vicci.driver.turtlebot.location.TurtleBotDockingStation;
import eu.vicci.driver.turtlebot.util.TurtleBotJavaToRosbridge;

public class TurtleBotEngine extends Engine {
	public TurtleBotEngine(String host) {
		super(host);
	}
	
	public TurtleBotEngine(String host, int port) {
		super(host, port);
	}
	
	@Override
	protected RobotJavaToRosbridge createJava2Rosbridge(String host, int port) {
		return new TurtleBotJavaToRosbridge(host, port);
	}
	
	@Override
	protected TurtleBotJavaToRosbridge getSocket() {
		return (TurtleBotJavaToRosbridge) super.getSocket();
	}
	
	@Override
	protected double getRawMaximumSpeed() {
		//Measured empirically.
		return 0.40; //in meters/second (1.39 meters/second = ~5 kph)
	}
	
	@Override
	protected double getRawMaximumAngularSpeed() {
		//Measured empirically.
		return 1.10;
	}
	
	private boolean robotIsDocking = false;
	
	//TODO: Christoph, Dimitri: This should be part of the TurtleBot not its engine. Need to change TurtleBotJava2Rosbridge.
	@Override
	public void moveTo(MovementTarget movementTarget) throws CannotMoveToMovementTargetException, NotConnectedException {
		//Add special behavior when driving to a docking station
		if (movementTarget instanceof TurtleBotDockingStation) {
			//Drive in front and activate docking procedure
			TurtleBotDockingStation dockingStation = (TurtleBotDockingStation) movementTarget;
			Location drivingTargetLocation = dockingStation.calculateDrivingTargetLocation();
			robotIsDocking = true;
			moveTo(drivingTargetLocation);
			if(robotIsDocking) doDocking();
			robotIsDocking = false;
		} else {
			super.moveTo(movementTarget);
		}
	}
	
	@Override
	protected void doMove(Vector linearVelocity, Vector angularVelocity) {
		getSocket().publishVelocityCommand(linearVelocity.getX(), angularVelocity.getZ());
	}

	@Override
	public void moveTo(Position position, Orientation orientation)
			throws CannotMoveToMovementTargetException, NotConnectedException {
		checkConnected();
		getSocket().moveTo(position, orientation);
	}
	
	private void doDocking(){
		getSocket().doDocking();
	}
	
	@Override
	protected boolean doStopMovement() {
		getSocket().cancelMoveTo();
		if(robotIsDocking){
			getSocket().stopDocking();
			robotIsDocking = false;
		}
		return true;
	}

	@Override
	public void moveToNonblocking(MovementTarget movementTarget)
			throws CannotMoveToMovementTargetException, NotConnectedException {
		getSocket().moveToNonblocking(movementTarget.getPosition(), movementTarget.getOrientation());
	}


}
