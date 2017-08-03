package eu.vicci.driver.turtlebot.example;

import eu.vicci.driver.robot.Robot;
import eu.vicci.driver.robot.util.Speed;
import eu.vicci.driver.robot.util.SpeedLevel;
import eu.vicci.driver.turtlebot.TurtleBot;

public class MoveTurtleBot {
	private static final String HOST = "192.168.1.58";
	private static final int PORT = 9999;
	private static final Speed SPEED = new Speed(SpeedLevel.MEDIUM);
	
	public static void main(String[] args) throws Exception{
		new MoveTurtleBot().run();		
	}
	
	public void run() throws Exception{
		Robot r = new TurtleBot(HOST, PORT);
		r.connect();
		Thread.sleep(2000);
		r.move(SPEED, 0);
		Thread.sleep(1000);
		r.stopMovement();
		r.disconnect();
	}

}
