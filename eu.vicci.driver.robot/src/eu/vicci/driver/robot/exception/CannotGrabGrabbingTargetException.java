package eu.vicci.driver.robot.exception;

import eu.vicci.driver.robot.grabbing.GrabbingTarget;

/**
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public abstract class CannotGrabGrabbingTargetException extends GrabbingException {
	private static final long serialVersionUID = 1L;
	
	private GrabbingTarget grabbingTarget;
	
	public CannotGrabGrabbingTargetException(GrabbingTarget grabbingTarget, String message) {
		super(message);
		
		this.grabbingTarget = grabbingTarget;
	}

	public GrabbingTarget getGrabbingTarget() {
		return grabbingTarget;
	}
}
