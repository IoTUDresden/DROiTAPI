package eu.vicci.nao.driver.util;

import java.util.Map;

import org.java_websocket.handshake.ServerHandshake;

import com.google.gson.internal.LinkedTreeMap;

import eu.vicci.driver.nao.engine.NaoEngine;
//import eu.vicci.driver.nao.io.SpeechIO;
import eu.vicci.driver.robot.exception.NotConnectedException;
import eu.vicci.driver.robot.util.RobotJavaToRosbridge;
import eu.vicci.driver.robot.util.Vector;

/**
 * @author stefan
 * 
 */
public class NaoJavaToRosbridge extends RobotJavaToRosbridge {
	private NaoEngine engine;
//	private SpeechIO speechInput;

	/**
	 * @param host
	 * @param port
	 */
	public NaoJavaToRosbridge(String host, int port, NaoEngine engine) {
		super(host, port);
		this.engine = engine;
	}

	@Override
	public void onOpen(ServerHandshake sh) {
		setEyesLEDs(1.0f, 0.0f, 0.0f);
		String hello = "Hello, I am connected.";
		sayText(hello);
		subscribe("/tactile_touch");
		subscribe("/word_recognized");
		subscribe("/bumper");
		super.onOpen(sh);
	}

	/**
	 * Adds support for strafing left/right
	 * 
	 * @param topic
	 * @param linear
	 * @param angular
	 */
	
	public void publishVelocityCommand(String topic, Vector linear,
			Vector angular) {
		Map<String,Map<String,Double>> msg = new LinkedTreeMap<String,Map<String,Double>>();
		Map<String,Double> linearVector = new LinkedTreeMap<String,Double>();
		Map<String,Double> angularVector = new LinkedTreeMap<String,Double>();
		
		linearVector.put("x", linear.getX());
		linearVector.put("y", linear.getY());
		linearVector.put("z", linear.getZ());
		
		angularVector.put("x", angular.getX());
		angularVector.put("y", angular.getY());
		angularVector.put("z", angular.getZ());
		
		msg.put("linear", linearVector);
		msg.put("angular",angularVector);
		publish(topic, msg);
	};
	
//	public void publishVelocityCommand(String topic, Vector linear,
//			Vector angular) {
//		String velo = "{'linear':{'x':" + linear.getX() + ",'y':"
//				+ linear.getY() + ",'z':" + linear.getZ() + "},'angular':{'x':"
//				+ angular.getX() + ",'y':" + angular.getY() + ",'z':"
//				+ angular.getZ() + "}}";
//		publish(topic, velo);
//	}

	@Override
	public void onMessage(String m) {
		String message = m;
		System.out.println("Answer: " + message);
		if (message.contains("bumper")) {
			// IF Bumpers are pressed, and is walking, stop movement!
			try {
				engine.stopMovement();
			} catch (NotConnectedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (message.contains("recognized")) {
			// TODO: Handle Speech
//			 speechInput.input("String", m);
		}

		super.onMessage(message);

	}

	/**
	 * Nao specific publishing
	 * 
	 * @param topic
	 * @param message
	 */
	public void publish(String topic, String message, String type) {
		String newMessage = "{'op': 'publish', 'topic': '" + topic
				+ "','type': '" + type + "', 'msg':" + message + "}";
		newMessage = newMessage.replace("'", "\"");
		System.out.println(newMessage);
		send(newMessage);
	}

	/**
	 * Calls a service without arguments.serviceName
	 * 
	 * @param serviceName
	 */
	public void serviceCall(String serviceName) {
		String message = "{'op': 'call_service', 'service': '" + serviceName
				+ "' }";
		message = message.replace("'", "\"");
		System.out.println(message);
		send(message);
	}

	/**
	 * Calls a service with arguments.
	 * 
	 * @param serviceName
	 * @param args
	 */
	public void serviceCall(String serviceName, String args) {
		String message = "{'op': 'call_service', 'service': '" + serviceName
				+ "', 'args':" + args + "}";
		message = message.replace("'", "\"");
		send(message);
	}

	public void sayText(String text) {
		String topic = "/speech";
		String type = "std_msgs/String";
		text = "{'data':'" + text + "'}";
		publish(topic, text, type);
	}

	/**
	 * Sets LEDs. Float Values between 0 to 1 (0 off, 1 Full) Eg.: 0.5
	 * 
	 * @param red
	 * @param blue
	 * @param green
	 */
	public void setEyesLEDs(Float red, Float blue, Float green) {
		String topic = "/fade_rgb";
		String ledName = "AllLeds";
		String duration = "0";
		String type = "nao_msgs/FadeRGB";
		Float a = 0.0f;
		String color = "{'r':" + red + ", 'g':" + blue + ", 'b':" + green
				+ ",'a':" + a + "}";
		String msg = "{'led_name':'" + ledName + "', 'color':" + color
				+ ", 'fade_duration':" + duration + "}";
		System.out.println("Msg: " + msg);
		publish(topic, msg, type);
	}

	/**
	 * TODO: Connection between Engine <-> JavaToRosBridge <-> RobotIO = ?
	 * 
	 * @param speechInput
	 */
//	public void setSpeechInput(SpeechIO speechInput) {
//		this.speechInput = speechInput;
//	}

}
