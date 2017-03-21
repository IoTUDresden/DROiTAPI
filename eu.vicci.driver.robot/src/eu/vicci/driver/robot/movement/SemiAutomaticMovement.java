package eu.vicci.driver.robot.movement;

import eu.vicci.driver.robot.exception.CannotMoveToMovementTargetException;
import eu.vicci.driver.robot.exception.NotConnectedException;
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;

/**
 * Interface capturing methods related to semi automatic move operations.
 *
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public interface SemiAutomaticMovement {
	/**
	 * Make a best effort to move to the specified movement target. Default behavior
	 * is to be placed above the position of the movement target. Concrete move
	 * behavior may be changed by specializations, e.g., to stop outside the bounding
	 * box of an element if one is provided.
	 * 
	 * @param movementTarget The target to move to.
	 * 
	 * @throws CannotMoveToMovementTargetException Thrown if the best effort to reach a target was unsuccessful.
	 * @throws NotConnectedException Thrown if there currently is no connection to the device.
	 */
	public void moveTo(MovementTarget movementTarget) throws CannotMoveToMovementTargetException, NotConnectedException;
	
	
	public void moveToNonblocking(MovementTarget movementTarget) throws CannotMoveToMovementTargetException, NotConnectedException;
	
	/**
	 * Make a best effort to move to the specified position with the given orientation.
	 * Default behavior is to be placed above the specified position.
	 * 
	 * @param position The position to move to.
	 * @param orientation The orientation for the device at the end of the move operation.
	 * 
	 * @throws CannotMoveToMovementTargetException Thrown if the best effort to reach a target was unsuccessful.
	 * @throws NotConnectedException Thrown if there currently is no connection to the device.
	 */
	//TODO: Christoph: Strictly speaking, there is no movement target here so the exception name is somewhat misleading.
	public void moveTo(Position position, Orientation orientation) throws CannotMoveToMovementTargetException, NotConnectedException;
	
	/**
	 * Cancel all currently active move operations and come to a halt.
	 * 
	 * @throws NotConnectedException Thrown if there currently is no connection to the device.
	 */
	public void stopMovement() throws NotConnectedException;
}
