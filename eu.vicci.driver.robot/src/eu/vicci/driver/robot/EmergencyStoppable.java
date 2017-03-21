package eu.vicci.driver.robot;

import eu.vicci.driver.robot.exception.NotConnectedException;

public interface EmergencyStoppable {
	/**
	 * Halts all actuators instantly for safety reasons.
	 * 
	 * @throws NotConnectedException Thrown if there currently is no connection to the device.
	 */
	public void emergencyStop() throws NotConnectedException;
}
