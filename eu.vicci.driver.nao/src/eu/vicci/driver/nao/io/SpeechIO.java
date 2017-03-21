//package eu.vicci.driver.nao.io;
//
//import java.util.ArrayList;
//
//import eu.vicci.driver.robot.exception.NotConnectedException;
//import eu.vicci.driver.robot.io.AudioRobotIO;
//import eu.vicci.driver.robot.io.RobotIOLanguage;
//import eu.vicci.nao.driver.util.NaoJavaToRosbridge;
//
///**
// * FixMe: Engine vs. Io: Is a speaker still Engine ?
// * Experimental!
// * @author stefan
// *
// */
//public class SpeechIO extends AudioRobotIO {
//	private boolean isListening = false;
//	private NaoJavaToRosbridge socket = null;
//	@Override
//	protected String inputRaw(RobotIOLanguage language) {
//		// TODO listener pattern for onMessage Events with topic speech
//		return null;
//	}
//
//	@Override
//	public void output(String message, RobotIOLanguage language) {
//		
//		
//		try {
//			sayText(message);
//		} catch (NotConnectedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	public void setWordRecognition(ArrayList words) {
//		//TODO: must be done in console: rosparam set /nao_speech/vocabulary "Hello"
//	}
//
//
//	public void startRecognition() throws NotConnectedException {
//		if (socket == null) throw new NotConnectedException("");
//		getSocket().serviceCall("/start_recognition");
//		isListening = true;
//	}
//
//	public void stopRecognition() {
//		getSocket().serviceCall("/stop_recognition");
//		isListening = false;
//	}
//
//	public void toggleSubscribtionToRecognition() {
//		if (!isListening){
//			try {
//				startRecognition();
//			} catch (NotConnectedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} else {
//			stopRecognition();
//		}
//	}
//	public void sayHello() {
//		String hello = "Hello, I am ready.";
//		try {
//			sayText(hello);
//		} catch (NotConnectedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	public void sayText(String text) throws NotConnectedException {
//		if (socket == null) throw new NotConnectedException("");
//		getSocket().sayText(text);
//		
//	}
//	public NaoJavaToRosbridge getSocket() {
//		return socket;
//	}
//	public void setSocket(NaoJavaToRosbridge socket) {
//		this.socket = socket;
//	}
//
//}
