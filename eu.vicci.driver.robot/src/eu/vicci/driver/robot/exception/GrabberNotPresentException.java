package eu.vicci.driver.robot.exception;

import eu.vicci.driver.robot.Robot;

public class GrabberNotPresentException extends GrabbingException {
	private static final long serialVersionUID = 3714974404037187278L;

	private Robot robot;
	
	public GrabberNotPresentException(Robot robot) {
		super(createMessage(robot));
	}

	private static String createMessage(Robot robot) {
		return "No grabber present on robot \"" + robot.getName() + "\".";
	}

	public Robot getRobot() {
		return robot;
	}
}
