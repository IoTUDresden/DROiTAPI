package eu.vicci.driver.robot.exception;

import eu.vicci.driver.robot.grabbing.GrabbingTarget;

/**
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public class GrabbingTargetOutOfReachException extends CannotGrabGrabbingTargetException {
	private static final long serialVersionUID = 1L;

	public GrabbingTargetOutOfReachException(GrabbingTarget grabbingTarget) {
		super(grabbingTarget, createMessage(grabbingTarget));
	}

	private static String createMessage(GrabbingTarget grabbingTarget) {
		return "The grabbing target's position " + grabbingTarget.getPosition() + " is out of reach of the grabber.";
	}
}
