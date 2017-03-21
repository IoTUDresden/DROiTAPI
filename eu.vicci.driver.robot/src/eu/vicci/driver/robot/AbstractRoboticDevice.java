package eu.vicci.driver.robot;

import eu.vicci.driver.robot.exception.NotConnectedException;

/**
 * Base implementation for robotic devices such as robots, grabbers etc.
 *
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public abstract class AbstractRoboticDevice implements RoboticDevice {
	private String host;
	private int port;
	
	private boolean isConnected;

	public AbstractRoboticDevice(String host, int port) {
		this.host = host;
		this.port = port;

		isConnected = false;
	}

	@Override
	public void connect() throws NotConnectedException {
		if (doConnect(host, port)) {
			isConnected = true;
		} else {
			throw new NotConnectedException("Failed to establish connection to " + host + ":" + port);
		}
	}

	protected abstract boolean doConnect(String host, int port);
	
	@Override
	public void disconnect() throws NotConnectedException {
		checkConnected();
		
		if (doDisconnect()) {
			isConnected = false;
		}
	}

	protected abstract boolean doDisconnect();
	
	/**
	 * Check whether there currently is a connection and throw an exception if there is not.
	 * 
	 * @throws NotConnectedException Thrown if there currently is no connection to the device.
	 */
	protected void checkConnected() throws NotConnectedException {
		if (!isConnected) {
			String message = getClass().getSimpleName();
			
			if (this instanceof NamedEntity) {
				NamedEntity namedEntity = (NamedEntity) this;
				message += " \"" + namedEntity.getName() + "\"";
			} else {
				message += " " + this;
			}
			
			message += " is not connected to " + host + ":" + port;

			throw new NotConnectedException(message);
		}
	}

	@Override
	public boolean getIsConnected() {
		return isConnected;
	}

	@Override
	public void emergencyStop() throws NotConnectedException {
		checkConnected();
		doEmergencyStop();
	}

	protected abstract void doEmergencyStop();
}
