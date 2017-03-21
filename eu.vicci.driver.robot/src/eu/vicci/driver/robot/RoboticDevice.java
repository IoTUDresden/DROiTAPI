package eu.vicci.driver.robot;

import eu.vicci.driver.robot.exception.NotConnectedException;

/**
 * Common interface for robotic devices such as robots, grabbers etc.
 * 
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public interface RoboticDevice extends EmergencyStoppable {
	/**
	 * Establish a connection to the device to enable it to 
	 * receive further commands.
	 *  
	 * @throws NotConnectedException Thrown if the attempt to connect to the device was unsuccessful.
	 */
	public void connect() throws NotConnectedException;
	
	/**
	 * Break connection to the device making it unable to receive further
	 * commands and freeing it for control by another peer.
	 * 
	 * @throws NotConnectedException Thrown if there currently is no connection to the device.
	 */
	public void disconnect() throws NotConnectedException;

	/**
	 * Determine whether the device is currently connected.
	 * 
	 * @return <code>True</code> if the device currently is connected, <code>false</code> otherwise.
	 */
	public boolean getIsConnected();
}
