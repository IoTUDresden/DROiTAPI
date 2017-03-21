package eu.vicci.driver.nao.engine;

import java.util.ArrayList;

//import eu.vicci.driver.nao.io.SpeechIO;
import eu.vicci.driver.robot.exception.CannotMoveToMovementTargetException;
import eu.vicci.driver.robot.exception.NotConnectedException;
import eu.vicci.driver.robot.movement.Engine;
import eu.vicci.driver.robot.movement.MovementTarget;
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;
import eu.vicci.driver.robot.util.RobotJavaToRosbridge;
import eu.vicci.driver.robot.util.Vector;
import eu.vicci.nao.driver.util.NaoJavaToRosbridge;

/**
 * @author stefan
 * 
 */
public class NaoEngine extends Engine {
	private boolean isStiff = false;
	private boolean isListening = false;
	private boolean isMoving = false; // failsafe against accidental stiffness
										// removing
//	private SpeechIO tts;

	public NaoEngine(String host) {
		super(host);
		// TODO Auto-generated constructor stub
	}

	public NaoEngine(String host, int port) {
		super(host, port);
		System.out.println("Nao Engine: " + host + ":" + port);
//		tts = new SpeechIO();

	}

	@Override
	protected RobotJavaToRosbridge createJava2Rosbridge(String host, int port) {
		System.out.println("Nao Bridge connect");
		return new NaoJavaToRosbridge(host, port, this);
	}

	@Override
	protected NaoJavaToRosbridge getSocket() {
		return (NaoJavaToRosbridge) super.getSocket();
	}

	@Override
	public void moveTo(Position position, Orientation orientation)
			throws CannotMoveToMovementTargetException, NotConnectedException {
		getSocket().publishGoalCanceable(position, orientation);
	}

	@Override
	public void moveTo(MovementTarget movementTarget)
			throws CannotMoveToMovementTargetException, NotConnectedException {
		// TODO: Find a way to track the position
		// Orientation ot = movementTarget.getOrientation();
		// Position pos = movementTarget.getPosition();
		//
		// System.out.println("Moving to: "+movementTarget.toString());
		// this.moveTo(pos, ot);
		// super.moveTo(movementTarget);
	}

	@Override
	public void doMove(Vector linearVelocity, Vector angularVelocity) {
		enableStiffness();
		isMoving = true;
		// TODO: needs to Stand up first sometimes
		getSocket().publishVelocityCommand("/cmd_vel", linearVelocity,
				angularVelocity);
	}

	@Override
	protected double getRawMaximumSpeed() {
		return 1.0;
	}

	@Override
	protected double getRawMaximumAngularSpeed() {
		return 1.0;
	}

	public void setEyesLEDs(Float red, Float blue, Float green) {
		getSocket().setEyesLEDs(red, blue, green);

	}

	public void toggleStiffness() {
		if (!isStiff) {
			enableStiffness();
		} else {
			if (!isMoving)
				disableStiffness();
			else
				System.out
						.println("Warning: Don't try to remove stiffness while walking.");
		}
	}

	public void enableStiffness() {
		if (!isStiff) {
			getSocket().serviceCall("/body_stiffness/enable");
			isStiff = true;
		}
	}

	public void disableStiffness() {
		if (isStiff && !isMoving) {
			getSocket().serviceCall("/body_stiffness/disable");
			isStiff = false;
		}
	}

	public void doMoveForward() {
		Vector linear = new Vector(0.5, 0, 0);
		Vector angular = new Vector(0, 0, 0);
		doMove(linear, angular);
	}

	public void doMoveBackward() {
		Vector linear = new Vector(-0.5, 0, 0);
		Vector angular = new Vector(0, 0, 0);
		doMove(linear, angular);
	}

	public void doMoveLeft() {
		Vector linear = new Vector(0, 0.5, 0);
		Vector angular = new Vector(0, 0, 0);
		doMove(linear, angular);
	}

	public void doMoveRight() {

		Vector linear = new Vector(0, -0.5, 0);
		Vector angular = new Vector(0, 0, 0);
		doMove(linear, angular);
	}

	public void doSteerRight() {

		Vector linear = new Vector(0, 0, 0);
		Vector angular = new Vector(0, 0, -0.5);
		doMove(linear, angular);
	}

	public void doSteerLeft() {

		Vector linear = new Vector(0, 0, 0);
		Vector angular = new Vector(0, 0, 0.5);
		doMove(linear, angular);
	}

	@Override
	public void stopMovement() throws NotConnectedException {
		getSocket().serviceCall("/stop_walk_srv");
		isMoving = false;
	}

	@Deprecated
	public void setWordRecognition(ArrayList words) {
		// TODO: must be done in console: rosparam set /nao_speech/vocabulary
		// "Hello"
	}

	@Deprecated
	public void startRecognition() {
		getSocket().serviceCall("/start_recognition");
		isListening = true;
	}

	@Deprecated
	public void stopRecognition() {
		getSocket().serviceCall("/stop_recognition");
		isListening = false;
	}

	@Deprecated
	public void toggleSubscribtionToRecognition() {
		if (!isListening) {

			startRecognition();

		} else {
			stopRecognition();
		}
	}

	public void sayHello() {
		String hello = "Hello, I am ready.";

		sayText(hello);

	}

	@Deprecated
	public void sayText(String text) {
		getSocket().sayText(text);

	}

	@Override
	public void moveToNonblocking(MovementTarget movementTarget)
			throws CannotMoveToMovementTargetException, NotConnectedException {
		// TODO Auto-generated method stub
		
	}
}
