package eu.vicci.driver.turtlebot;

import eu.vicci.driver.robot.Robot;
import eu.vicci.driver.robot.movement.Engine;
import eu.vicci.driver.robot.movement.MovementTarget;
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;
import eu.vicci.driver.turtlebot.engine.TurtleBotEngine;

/**
 * Client class for controlling a TurtleBot.
 * 
 * @see eu.vicci.driver.turtlebot.example.TurtleBotExample
 * 
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public class TurtleBot extends Robot {
	public TurtleBot(String host) {
		this(host, Engine.DEFAULT_PORT);
	}

	public TurtleBot(String host, int port) {
		this(Robot.DEFAULT_NAME, host, port);
	}

	public TurtleBot(String name, String host) {
		this(name, host, Engine.DEFAULT_PORT);
	}

	public TurtleBot(String name, String host, int port) {
		super(name, new TurtleBotEngine(host, port));
	}

	/**
	 * Deprecated as of 13.12.2013 as other robots might not "drive", e.g., the NAO. 
	 * 
	 * @deprecated use {@link moveTo()} instead.  
	 */
	@Deprecated
	public void driveTo(MovementTarget movementTarget) {
		try {
			moveTo(movementTarget);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Deprecated as of 13.12.2013 as other robots might not "drive", e.g., the NAO. 
	 * 
	 * @deprecated use {@link moveTo()} instead.  
	 */
	@Deprecated
	public void driveTo(Position position, Orientation orientation) {
		try {
			moveTo(position, orientation);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
