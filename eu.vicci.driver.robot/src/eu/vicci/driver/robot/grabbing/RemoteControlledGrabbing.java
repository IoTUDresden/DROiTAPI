package eu.vicci.driver.robot.grabbing;

import eu.vicci.driver.robot.exception.GrabberNotPresentException;
import eu.vicci.driver.robot.exception.NotConnectedException;

/**
 * Interface capturing methods related to remote controlled grab operations.
 * 
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public interface RemoteControlledGrabbing {
	/**
	 * Open the grabber at its current position with the current orientation.
	 * 
	 * @throws GrabberNotPresentException Thrown if no grabber is installed for the device.
	 * @throws NotConnectedException Thrown if there currently is no connection to the device.
	 */
	public void openGrabber() throws GrabberNotPresentException, NotConnectedException;

	/**
	 * This method is non-blocking!
	 */
	//TODO: Christoph: Devise a method for remotely controlled movement of the grabber in 3D.
//	public void moveGrabber(Speed speed, ...) throws GrabberNotPresentException, CannotPositionGrabberException, NotConnectedException;
	
	/**
	 * Close the grabber at its current position with the current orientation.
	 * 
	 * @throws GrabberNotPresentException Thrown if no grabber is installed for the device.
	 * @throws NotConnectedException Thrown if there currently is no connection to the device.
	 */
	public void closeGrabber() throws GrabberNotPresentException, NotConnectedException;
	
	/**
	 * Cancel all currently active grab operations to come to a halt.
	 * 
	 * @throws GrabberNotPresentException Thrown if no grabber is installed for the device.
	 * @throws NotConnectedException Thrown if there currently is no connection to the device.
	 */
	public void stopGrabbing() throws GrabberNotPresentException, NotConnectedException;
}
