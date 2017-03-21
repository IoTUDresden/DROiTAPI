package eu.vicci.driver.robot.grabbing;

import eu.vicci.driver.robot.exception.CannotPositionGrabberException;
import eu.vicci.driver.robot.exception.GrabberNotPresentException;
import eu.vicci.driver.robot.exception.GrabbingTargetOutOfReachException;
import eu.vicci.driver.robot.exception.GrabbingTargetTooLargeException;
import eu.vicci.driver.robot.exception.NotConnectedException;
import eu.vicci.driver.robot.exception.StillHoldingGrabbingTargetException;
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;

/**
 * Interface capturing methods related to semi automatic grab operations.
 *
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public interface SemiAutomaticGrabbing {
	/**
	 * Open the grabber at its current position with the current orientation. Releases the currently
	 * held grabbing target.
	 * 
	 * @throws GrabberNotPresentException Thrown if no grabber is installed for the device.
	 * @throws NotConnectedException Thrown if there currently is no connection to the device.
	 */
	public void openGrabber() throws GrabberNotPresentException, NotConnectedException;
	
	/**
	 * Make a best effort to position the grabber at the specified grabbing target.
	 * 
	 * @param grabbingTarget The target to use as destination for positioning the grabber.
	 * 
	 * @throws GrabberNotPresentException Thrown if no grabber is installed for the device.
	 * @throws CannotPositionGrabberException Thrown if the grabber cannot be positioned at the specified position with the specified orientation.
	 * @throws NotConnectedException Thrown if there currently is no connection to the device.
	 */
	public void positionGrabber(GrabbingTarget grabbingTarget) throws GrabberNotPresentException, CannotPositionGrabberException, NotConnectedException;
	
	/**
	 * Make a best effort to position the grabber at the specified position and orientation.
	 * 
	 * @param position The position to use as destination for positioning the grabber.
	 * @param orientation The orientation to use as destination for positioning the grabber.
	 * 
	 * @throws GrabberNotPresentException Thrown if no grabber is installed for the device.
	 * @throws CannotPositionGrabberException Thrown if the grabber cannot be positioned at the specified position with the specified orientation.
	 * @throws NotConnectedException Thrown if there currently is no connection to the device.
	 */
	public void positionGrabber(Position position, Orientation orientation) throws GrabberNotPresentException, CannotPositionGrabberException, NotConnectedException;
	
	/**
	 * Make a best effort to position the grabber at the specified orientation. The current position of the grabber is maintained.
	 * 
	 * @param position The position to use as destination for positioning the grabber.
	 * 
	 * @throws GrabberNotPresentException Thrown if no grabber is installed for the device.
	 * @throws CannotPositionGrabberException Thrown if the grabber cannot be positioned at the specified position with the specified orientation.
	 * @throws NotConnectedException Thrown if there currently is no connection to the device.
	 */
	public void positionGrabber(Orientation orientation) throws GrabberNotPresentException, CannotPositionGrabberException, NotConnectedException;
	
	/**
	 * Make a best effort to grab the specified grabbing target. If successful, the supplied grabbing target
	 * is henceforth held by the grabber. Make sure to manually release all currently held grabbing targets
	 * using {@link #openGrabber()} before invoking this method.
	 * 
	 * @param grabbingTarget The target to grab.
	 * 
	 * @throws GrabberNotPresentException Thrown if no grabber is installed for the device.
	 * @throws CannotPositionGrabberException Thrown if the grabber cannot be positioned at the specified position with the specified orientation.
	 * @throws StillHoldingGrabbingTargetException Thrown if the grabber is still holding another grabbing target.
	 * @throws NotConnectedException Thrown if there currently is no connection to the device.
	 */
	public void grab(GrabbingTarget grabbingTarget) throws GrabberNotPresentException, CannotPositionGrabberException, StillHoldingGrabbingTargetException, GrabbingTargetTooLargeException, GrabbingTargetOutOfReachException, NotConnectedException;
	
	/**
	 * Cancel all currently active grab operations to come to a halt.
	 * 
	 * @throws GrabberNotPresentException Thrown if no grabber is installed for the device.
	 * @throws NotConnectedException Thrown if there currently is no connection to the device.
	 */
	public void stopGrabbing() throws GrabberNotPresentException, NotConnectedException;
	
	/**
	 * Determine whether the grabber currently holds a previously grabbed grabbing target.
	 * 
	 * @return <code>True</code> if the grabber currently holds a grabbing target, <code>false</code> otherwise.
	 */
	public boolean holdsGrabbingTarget();
	
	/**
	 * Determine the currently held grabbing target.
	 * 
	 * @return The currently held grabbing target or <code>null</code> if there is none.
	 */
	public GrabbingTarget getHeldGrabbingTarget();
}
