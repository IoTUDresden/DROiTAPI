package eu.vicci.robot.controlapp;

import java.net.URI;

import eu.vicci.driver.robot.exception.UnknownRobotException;
import eu.vicci.robot.controlapp.util.gyro.GyroSensorListener;
import eu.vicci.robot.controlapp.util.gyro.GyroSensorReader;
import eu.vicci.robot.controlapp.util.robot.RobotCommandPublisherThread;
import eu.vicci.robot.controlapp.util.webcam.InputStreamProvider;
import eu.vicci.robot.controlapp.view.WebcamView;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.View;

public class MainThread extends AsyncTask<Void, String, Void> implements GyroSensorListener {

	public static final String MJPEG_SERVER_URL_UNFORMATED = "http://%s:%s/stream?topic=%s?quality=%s";
	
	private Context context;
	private SharedPreferences sharedPreferences;
	private WebcamView webcamView;
	private RobotCommandPublisherThread robotThread;
	
	private boolean doDisconnect = true;
	
	public MainThread(Context context, WebcamView webcamView){
		this.context = context;
		this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		this.webcamView = webcamView;
	}
	
	@Override
	protected Void doInBackground(Void... arg0) {		
		GyroSensorReader gyroSensorReader = new GyroSensorReader(context);
		gyroSensorReader.setGyroSensorListner(this);
		
		String host = getHostFromPreferences();
		int port = getPortFromPreferences();

		
		InputStreamProvider isp = null;
		if(isVideoEnabled()){
			((MainActivity) context).runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					webcamView.setVisibility(View.VISIBLE);
				}
			});
			
			isp = new InputStreamProvider(createURI(), webcamView);
			isp.setPriority(Thread.MAX_PRIORITY);
			isp.start();
		}
		

		try {
			robotThread = new RobotCommandPublisherThread(host, port);
			robotThread.setPublishingDelay(getPublishingDelay());
			gyroSensorReader.start();
			robotThread.start();
		} catch (UnknownRobotException e) {
			System.out.println("unknown robot");
		}
		
		
		while(!isCancelled()){
			Thread.yield();
		}
		
		gyroSensorReader.stop();
		if(robotThread!=null) robotThread.interrupt();
		if(isp!=null) isp.interrupt();
		return null;
	}
	
//	private boolean isDebugMode(){
//		return sharedPreferences.getBoolean(SettingsFragment.KEY_DEBUG_MODE, false);
//	}

	private boolean isVideoEnabled() {
		return sharedPreferences.getBoolean(SettingsFragment.KEY_VIDEO_ENABLE, false);
	}
	
	private String getHostFromPreferences(){
		return sharedPreferences.getString(SettingsFragment.KEY_ROBOT_IP, "");
	}
	
	private int getPortFromPreferences(){
		return Integer.parseInt(sharedPreferences.getString(SettingsFragment.KEY_ROBOT_PORT, ""));
	}
	
	private int getPublishingDelay(){
		return Integer.parseInt(sharedPreferences.getString(SettingsFragment.KEY_ROBOT_PUBLISHING_DELAY, ""));
	}
	
	private URI createURI() {
		String videoIP = sharedPreferences.getString(SettingsFragment.KEY_VIDEO_IP, "");
		String videoPort = sharedPreferences.getString(SettingsFragment.KEY_VIDEO_PORT, "");
		String videoTopic = sharedPreferences.getString(SettingsFragment.KEY_VIDEO_TOPIC, "");
		String videoQuality = sharedPreferences.getString(SettingsFragment.KEY_VIDEO_QUALITY, "");
		String uriString = String.format(MJPEG_SERVER_URL_UNFORMATED, videoIP, videoPort, videoTopic, videoQuality);
		return URI.create(uriString);
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
		
	}

	@Override
	public void onSensorData(double xAxis, double yAxis) {
		double xValue = (Math.abs(xAxis) < 0.15f) ? 0 : xAxis * 3;
		double yValue = (Math.abs(yAxis) < 0.15f) ? 0 : yAxis * 3;

		if (xValue > 1.0)
			xValue = 1.0f;
		if (yValue > 1)
			yValue = 1;
		if (xValue < -1.0)
			xValue = -1.0f;
		if (yValue < -1)
			yValue = -1;
		if(!doDisconnect)robotThread.tryPublishingVelocityCommand(yValue, -xValue);
	}

	public void setDoDisconnect(boolean doDisconnect) {
		this.doDisconnect = doDisconnect;
	}
	
}
