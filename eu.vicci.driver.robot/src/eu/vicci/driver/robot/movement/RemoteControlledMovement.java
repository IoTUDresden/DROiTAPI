package eu.vicci.driver.robot.movement;

import eu.vicci.driver.robot.exception.NotConnectedException;
import eu.vicci.driver.robot.util.Speed;

/**
 * Interface capturing methods related to remote controlled move operations.
 * 
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public interface RemoteControlledMovement {
	/**
	 * Move at the provided speed with the specified curve radius until another
	 * move operation is issued. 
	 * 
	 * This method is non-blocking!
	 * 
	 * @param speed The speed and general direction too use.
	 * @param curveRadius The radius of the curve in meters.
	 */
	//TODO: Christoph: This method is unstable.
	public void move(Speed speed, double curveRadius) throws NotConnectedException;
	
	/**
	 * Cancel all currently active move operations and come to a halt.
	 * 
	 * @throws NotConnectedException Thrown if there currently is no connection to the device.
	 */
	public void stopMovement() throws NotConnectedException;
}
