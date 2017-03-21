package eu.vicci.driver.robot.exception;

/**
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public abstract class GrabbingException extends RobotException {
	private static final long serialVersionUID = 1L;

	public GrabbingException(String message) {
		super(message);
	}	
}
