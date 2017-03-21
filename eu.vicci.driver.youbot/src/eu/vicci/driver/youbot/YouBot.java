package eu.vicci.driver.youbot;

import eu.vicci.driver.robot.Robot;
import eu.vicci.driver.robot.grabbing.Grabber;
import eu.vicci.driver.robot.movement.Engine;
import eu.vicci.driver.youbot.engine.YouBotEngine;

/**
 * Client class for controlling a KUKA youBot.
 * 
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public class YouBot extends Robot {
	
	public YouBot(YouBotEngine engine, Grabber grabber) {
		super(engine, grabber);
	}

	public YouBot(YouBotEngine engine) {
		super(engine);
	}

	public YouBot(String name, YouBotEngine engine, Grabber grabber) {
		super(name, engine, grabber);
	}

	public YouBot(String name, YouBotEngine engine) {
		super(name, engine);
	}

	public YouBot(String host) {
		this(host, Engine.DEFAULT_PORT);
	}

	public YouBot(String host, int port) {
		this(Robot.DEFAULT_NAME, host, port);
	}

	public YouBot(String name, String host) {
		this(name, host, Engine.DEFAULT_PORT);
	}

	public YouBot(String name, String host, int port) {
		super(name, new YouBotEngine(host, port));
	}
		
}
