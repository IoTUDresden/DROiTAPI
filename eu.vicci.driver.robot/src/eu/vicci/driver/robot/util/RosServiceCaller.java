package eu.vicci.driver.robot.util;

import java.lang.reflect.Type;

import org.java_websocket.handshake.ServerHandshake;

import com.google.gson.Gson;

import eu.vicci.driver.robot.exception.TimeoutException;
import eu.vicci.driver.robot.util.rosmsg.ServiceResponseMsg;

public class RosServiceCaller extends RobotJavaToRosbridge {

	private Type msgType;
	
	private static final long TIMEOUT = 5000;
	
	public RosServiceCaller(String host,Type msgType) {
		super(host);
		this.msgType = msgType;
	}

	public RosServiceCaller(String host, int port, Type msgType) {
		super(host, port);
		this.msgType = msgType;
	}

	@Override
	public void onMessage(String message) {
		Gson gson = new Gson();
		response = gson.fromJson(message, msgType);
	}

	@Override
	public void onOpen(ServerHandshake shs) {
	}

	@SuppressWarnings("rawtypes")
	ServiceResponseMsg response = null;

	@SuppressWarnings("rawtypes")
	public ServiceResponseMsg call(String serviceName) throws TimeoutException{
		response = null;
		Thread t = new Thread(this);
		t.start();
		
		long timer = 0;
		long time = System.currentTimeMillis();
		while (!isConnected()) {
			timer = System.currentTimeMillis() - time;
			if(timer > TIMEOUT) throw new TimeoutException("Connection TIMOUT reached ( "+TIMEOUT+" ms)");
			Thread.yield();
		}
		serviceCall(serviceName);
		while (response == null) {
			Thread.yield();
		}
		
		try {
			closeBlocking();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t.interrupt();
		return response;
	}
	
}
