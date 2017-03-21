package eu.vicci.driver.robot.exception;

/**
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public class NotConnectedException extends RobotException {
	private static final long serialVersionUID = 1L;
	
	public NotConnectedException(String message) {
		super(message);
	}
}
