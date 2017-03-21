package eu.vicci.driver.robot.exception;

/**
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;

public class CannotPositionGrabberException extends GrabbingException {
	private static final long serialVersionUID = 1L;
	
	private Position position;
	private Orientation orientation;
	
	public CannotPositionGrabberException(Position position, Orientation orientation) {
		super(createMessage(position, orientation));
		
		this.position = position;
		this.orientation = orientation;
	}
	
	private static String createMessage(Position position, Orientation orientation) {
		return "Unable to position grabber at " + position + " with orientation " + orientation + ".";
	}

	public Position getPosition() {
		return position;
	}

	public Orientation getOrientation() {
		return orientation;
	}
}
