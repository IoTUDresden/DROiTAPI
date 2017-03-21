/**
 * 
 */
package eu.vicci.nao.driver.example;

import eu.vicci.driver.nao.Nao;
import eu.vicci.driver.robot.exception.CannotMoveToMovementTargetException;
import eu.vicci.driver.robot.exception.NotConnectedException;
import eu.vicci.driver.robot.location.Location;
import eu.vicci.driver.robot.location.Site;
import eu.vicci.driver.robot.location.UnnamedLocation;
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;


/**
 * @author stefan
 *
 */
public class NaoExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String host = "192.168.1.7";
		Nao nao = new Nao("Chuck", host);
		GUI gui = GUI.getInstance();
		gui.setNao(nao);
		nao.start();
		
//		testSzenario(nao);
	}
	public static void testSzenario(Nao nao){
		// Create locations (sites or docking stations) once.
//		LocationManager.addLocation();
//		LocationManager.addLocation(new Site("Cabinet", new Position(23.0, 44.5), new Orientation(56.9, 78.5)));
		Site cabinet = new Site("Trash Bin", new Position(15.0, 12.9), new Orientation(46.2, 32.7));
		// Determine an initial location
		Position initialPosition = new Position(23.1, 14.7);
		Orientation initialOrientation = new Orientation(52.08, 12.31);
		Location initialLocation = new UnnamedLocation(initialPosition, initialOrientation);



		try {
		// Initialize the TurtleBot's location.
		nao.setLocation(initialLocation);

		// Have the TurtleBot drive to a target.
		System.out.println("Walking to: " + cabinet);
		nao.moveTo(cabinet);

		// Stop the TurtleBot is requested by user or exceptional event occurs.
			nao.stopMovement();
		} catch (NotConnectedException | CannotMoveToMovementTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}	
