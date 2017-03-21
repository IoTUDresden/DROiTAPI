package eu.vicci.driver.robot.tools;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import eu.vicci.driver.robot.exception.TimeoutException;
import eu.vicci.driver.robot.exception.UnknownRobotException;
import eu.vicci.driver.robot.grabbing.Grabber;
import eu.vicci.driver.robot.movement.Engine;
import eu.vicci.driver.robot.util.RosServiceCaller;
import eu.vicci.driver.robot.util.rosmsg.ServiceListMsg;
import eu.vicci.driver.robot.util.rosmsg.ServiceResponseMsg;

public class RobotIdentifier {

	private static RobotIdentifier robotIdentifier;
	
	private RobotIdentifier(){}
	
	public static RobotIdentifier  getInstance(){
		if(robotIdentifier!=null) return robotIdentifier;
		else {
			robotIdentifier = new RobotIdentifier();
			return robotIdentifier;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Engine getRobotEngineByIP(String ip, int port) throws UnknownRobotException{
		Type msgType = new TypeToken<ServiceResponseMsg<ServiceListMsg>>(){}.getType();
		RosServiceCaller caller = new RosServiceCaller(ip, port, msgType);
		ServiceResponseMsg<ServiceListMsg> msg;
		try {
			msg = (ServiceResponseMsg<ServiceListMsg>) caller.call("/rosapi/services");
			List<String> services = msg.getValues().getServices();
			for(String s:services){
				if(s.contains("turtlebot")) {
					System.out.println("Turtlebot");
					Class c = Class.forName("eu.vicci.driver.turtlebot.engine.TurtleBotEngine");
					Constructor constructor = c.getConstructor(String.class,int.class);
					Engine e = (Engine) constructor.newInstance(ip,port);
					return e;
				}
				else if(s.contains("youbot")) {
					System.out.println("Youbot");
					Class c = Class.forName("eu.vicci.driver.youbot.engine.YouBotEngine");
					Constructor constructor = c.getConstructor(String.class,int.class);
					Engine e = (Engine) constructor.newInstance(ip,port);
					return e;		
				}
			}
			throw new UnknownRobotException();
//			Class c = Class.forName("eu.vicci.driver.nao.engine.NaoEngine");
//			System.out.println("Nao");
//			Constructor constructor = c.getConstructor(String.class,int.class);
//			Engine e = (Engine) constructor.newInstance(ip,port);			
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		}
		
		return null;
	}
	
	public Grabber getRobotGrabberByIP(String ip, int port){
		// TODO Grabbing Guy please implement this.
		
		return null;
	}
	
}
