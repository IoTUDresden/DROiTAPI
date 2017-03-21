package eu.vicci.driver.robot.exception;

import eu.vicci.driver.robot.grabbing.GrabbingTarget;

/**
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public class StillHoldingGrabbingTargetException extends CannotGrabGrabbingTargetException {
	private static final long serialVersionUID = 1L;

	public StillHoldingGrabbingTargetException(GrabbingTarget grabbingTarget) {
		super(grabbingTarget, createMessage(grabbingTarget));
	}

	private static String createMessage(GrabbingTarget grabbingTarget) {
		return "The grabber is still holding the grabbing target " + grabbingTarget + ". Release the grabbing target manually before grabbing a new one.";
	}
}
