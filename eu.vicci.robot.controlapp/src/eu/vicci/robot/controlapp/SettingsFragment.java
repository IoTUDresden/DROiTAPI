package eu.vicci.robot.controlapp;

import eu.vicci.turtlebot.controlapp.R;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;


public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener{

	public static final String KEY_DEBUG_MODE = "key_debug_mode";
	
	public static final String KEY_ROBOT_IP = "key_robot_ip";
	public static final String KEY_ROBOT_PORT = "key_robot_port";
	public static final String KEY_ROBOT_PUBLISHING_DELAY = "key_robot_publishing_delay";
	
	public static final String KEY_VIDEO_ENABLE = "key_video_enable";
	public static final String KEY_VIDEO_IP = "key_video_ip";
	public static final String KEY_VIDEO_PORT = "key_video_port";
	public static final String KEY_VIDEO_TOPIC = "key_video_topic";
	public static final String KEY_VIDEO_QUALITY = "key_video_quality";
	
	private Preference prefRobotIP, prefRobotPort, prefPublishingDelay;
	private Preference prefVideoIP, prefVideoPort, prefVideoTopic;
	private ListPreference prefVideoQuality;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    	initPreference();
    	initSummerys();
    }
    
    private void initPreference(){
    	prefRobotIP = findPreference(KEY_ROBOT_IP);
    	prefRobotPort = findPreference(KEY_ROBOT_PORT);
    	prefPublishingDelay = findPreference(KEY_ROBOT_PUBLISHING_DELAY);
    	
    	prefVideoIP = findPreference(KEY_VIDEO_IP);
    	prefVideoPort = findPreference(KEY_VIDEO_PORT);
    	prefVideoTopic = findPreference(KEY_VIDEO_TOPIC);
    	prefVideoQuality = (ListPreference) findPreference(KEY_VIDEO_QUALITY);
    }
    
    private void initSummerys() {
    	SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
    	prefRobotIP.setSummary(sharedPreferences.getString(KEY_ROBOT_IP, ""));
    	prefRobotPort.setSummary(sharedPreferences.getString(KEY_ROBOT_PORT, ""));
    	prefPublishingDelay.setSummary(sharedPreferences.getString(KEY_ROBOT_PUBLISHING_DELAY, "")+" ms");
        
    	prefVideoIP.setSummary(sharedPreferences.getString(KEY_VIDEO_IP, ""));
    	prefVideoPort.setSummary(sharedPreferences.getString(KEY_VIDEO_PORT, ""));
    	prefVideoTopic.setSummary(sharedPreferences.getString(KEY_VIDEO_TOPIC, ""));
    	prefVideoQuality.setSummary(prefVideoQuality.getEntry());
	}
    

    @Override
    public void onResume() {
    	super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    	super.onPause();
    }
    
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key ) {
		initSummerys();
	}
    
	
}
