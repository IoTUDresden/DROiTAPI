package eu.vicci.driver.robot.exception;

import eu.vicci.driver.robot.grabbing.GrabbingTarget;

/**
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public class GrabbingTargetTooLargeException extends CannotGrabGrabbingTargetException {
	private static final long serialVersionUID = 1L;

	public GrabbingTargetTooLargeException(GrabbingTarget grabbingTarget) {
		super(grabbingTarget, createMessage(grabbingTarget));
	}

	private static String createMessage(GrabbingTarget grabbingTarget) {
		return "The grabbing target's size " + grabbingTarget.getDimensions() + " exceeds the maximum size grabbable by the grabber.";
	}
}
