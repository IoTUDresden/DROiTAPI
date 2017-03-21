package eu.vicci.driver.nao;

import java.util.ArrayList;

import eu.vicci.driver.nao.engine.NaoEngine;
import eu.vicci.driver.robot.Robot;
import eu.vicci.driver.robot.exception.NotConnectedException;
import eu.vicci.driver.robot.movement.Engine;
import eu.vicci.driver.robot.util.Vector;
import eu.vicci.nao.driver.util.NaoJavaToRosbridge;

/**
 * @author stefan Email: stefan.herrmann@mailbox.tu-dresden.de
 * 
 */
public class Nao extends Robot {
	private NaoEngine engine = null;

	public Nao(String host) {
		this(host, Engine.DEFAULT_PORT);
	}

	public Nao(String host, int port) {
		this(Robot.DEFAULT_NAME, host, port);
	}

	public Nao(String name, String host) {
		this(name, host, Engine.DEFAULT_PORT);
	}

	public Nao(String name, String host, int port) {
		super(name, new NaoEngine(host, port));
	}

	public void start() {
		try {
			super.connect();
			engine = (NaoEngine) super.getEngine();
		} catch (NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sayText(String text) {
		engine.sayText(text);
	}

	public void setEyeColor(String color) {
		switch (color) {
		case "blue":
			engine.setEyesLEDs(0f, 0f, 1f);
			break;
		case "red":
			engine.setEyesLEDs(1f, 0f, 0f);
			break;
		case "green":
			engine.setEyesLEDs(0f, 1f, 0f);
			break;
		default:
			break;
		}
	}

	public void stiffness() {
		engine.toggleStiffness();
	}

	public void move(Vector v1, Vector v2) {
		engine.doMove(v1, v2);
	}

	public void stopMoving() {
		try {
			engine.stopMovement();
		} catch (NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void speech() {
		engine.toggleSubscribtionToRecognition();
	}

	public void doMoveForward() {
		engine.doMoveForward();
	}

	public void doMoveBackward() {
		engine.doMoveBackward();
	}

	public void doMoveRight() {
		engine.doMoveRight();
	}

	public void doMoveLeft() {
		engine.doMoveLeft();
	}


	@Override
	public NaoEngine getEngine() {
		// TODO Auto-generated method stub
		return (NaoEngine) super.getEngine();
	}
}
