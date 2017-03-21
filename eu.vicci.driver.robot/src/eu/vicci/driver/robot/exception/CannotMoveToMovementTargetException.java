package eu.vicci.driver.robot.exception;

import eu.vicci.driver.robot.movement.MovementTarget;

public class CannotMoveToMovementTargetException extends MovementException {
	private static final long serialVersionUID = 1L;

	private static MovementTarget movementTarget;
	
	public CannotMoveToMovementTargetException(MovementTarget movementTarget) {
		super(createMessage(movementTarget));
	}
	
	private static String createMessage(MovementTarget movementTarget) {
		return "Unable to move to movement target at " + movementTarget.getPosition();
	}

	public static MovementTarget getMovementTarget() {
		return movementTarget;
	}
}
