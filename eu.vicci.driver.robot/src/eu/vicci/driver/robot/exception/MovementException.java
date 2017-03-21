package eu.vicci.driver.robot.exception;

public abstract class MovementException extends RobotException {
	private static final long serialVersionUID = 1L;
	
	public MovementException(String message) {
		super(message);
	}

}
