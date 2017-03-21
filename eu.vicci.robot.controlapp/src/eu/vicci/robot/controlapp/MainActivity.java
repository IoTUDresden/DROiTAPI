package eu.vicci.robot.controlapp;

import eu.vicci.robot.controlapp.view.WebcamView;
import eu.vicci.turtlebot.controlapp.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity {
	
	private MainThread mainThread;
	private WebcamView webcamView;
	
	private ImageButton butStop1, butStop2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		setContentView(R.layout.activity_main);
		webcamView = (WebcamView) findViewById(R.id.webcam_stream_view);
		
		butStop1 = (ImageButton) findViewById(R.id.button_stop);
		butStop2 = (ImageButton) findViewById(R.id.button_stop2);
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
	}
		
	@Override
	protected void onStart() {
		super.onStart();
		webcamView.setVisibility(View.INVISIBLE);
		mainThread = new MainThread(this,webcamView);
		mainThread.execute();
		doDisconnect();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		mainThread.cancel(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			transitToSettingActivity();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void transitToSettingActivity(){
		Intent intent = new Intent(this, SettingActivity.class);
		startActivity(intent);
	}


	
	private void doDisconnect(){
		mainThread.setDoDisconnect(true);
		butStop1.setImageResource(R.drawable.green_128_n);
		butStop2.setImageResource(R.drawable.green_128_n);
	}
	
	private void doConnect(){
		mainThread.setDoDisconnect(false);
		butStop2.setImageResource(R.drawable.red_128_n);
		butStop1.setImageResource(R.drawable.red_128_n);
	}
			
	private boolean toggleFlag = false;
	
	public boolean onClick(View v) {
		if(toggleFlag){
			doDisconnect();
		} else {
			doConnect();
		}
		toggleFlag = !toggleFlag;
		return false;
	}

}
