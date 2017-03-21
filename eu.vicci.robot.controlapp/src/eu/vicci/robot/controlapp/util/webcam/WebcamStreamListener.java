package eu.vicci.robot.controlapp.util.webcam;

import android.graphics.Bitmap;

public interface WebcamStreamListener {

	public void onBitmapReceived(Bitmap bitmap);
	
}
