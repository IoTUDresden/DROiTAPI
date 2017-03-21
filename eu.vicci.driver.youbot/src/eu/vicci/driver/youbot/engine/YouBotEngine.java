package eu.vicci.driver.youbot.engine;

import eu.vicci.driver.robot.exception.CannotMoveToMovementTargetException;
import eu.vicci.driver.robot.exception.NotConnectedException;
import eu.vicci.driver.robot.movement.Engine;
import eu.vicci.driver.robot.movement.MovementTarget;
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;
import eu.vicci.driver.robot.util.RobotJavaToRosbridge;
import eu.vicci.driver.robot.util.Vector;
import eu.vicci.driver.youbot.util.YouBotJavaToRosbridge;

public class YouBotEngine extends Engine {

	
	public YouBotEngine(String host, int port) {
		super(host, port);
	}

	public YouBotEngine(String host) {
		super(host);
	}
	
	@Override
	protected RobotJavaToRosbridge createJava2Rosbridge(String host, int port) {
		return new YouBotJavaToRosbridge(host, port);
	}
	
	@Override
	protected YouBotJavaToRosbridge getSocket() {
		return (YouBotJavaToRosbridge) super.getSocket();
	}

	@Override
	protected double getRawMaximumSpeed() {
		return 0.30;
	}

	@Override
	protected double getRawMaximumAngularSpeed() {
		return 0.60;
	}

	@Override
	public void moveTo(Position position, Orientation orientation)
			throws CannotMoveToMovementTargetException, NotConnectedException {	
		getSocket().moveTo(position, orientation);
	}


	@Override
	protected void doEmergencyStop() {
		doStopMovement();
		getSocket().publishVelocityCommand(0.0,0.0);
	}

	@Override
	protected void doMove(Vector linearVelocity, Vector angularVelocity) {
		getSocket().publishVelocityCommand(linearVelocity.getX(), angularVelocity.getZ());
	}
	
	@Override
	protected boolean doDisconnect() {
		getSocket().publishVelocityCommand(0.0, 0.0); // Youbot don't stop moving automatically after disconnect!!
		getWebSocketThread().interrupt();
		try {
			getSocket().closeBlocking();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void moveToNonblocking(MovementTarget movementTarget)
			throws CannotMoveToMovementTargetException, NotConnectedException {
		getSocket().moveToNonblocking(movementTarget.getPosition(), movementTarget.getOrientation());
	}

}
