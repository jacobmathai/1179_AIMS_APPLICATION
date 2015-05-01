package com.aims.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.aims.util.GmailSender;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.Toast;

public class AndroidCamera1 extends Activity {
	private static final String TAG = "CameraDemo";
	Camera camera;
	Preview preview;
	Button buttonClick;
	Chronometer ch;
	String img = Environment.getExternalStorageDirectory() + "/aims1.jpg";
	static String detpath = Environment.getExternalStorageDirectory()
			+ "/user_reg.txt";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);

		preview = new Preview(this);

		((FrameLayout) findViewById(R.id.preview)).addView(preview);

		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				// your code here
				preview.camera.takePicture(shutterCallback, rawCallback,
						jpegCallback);

				finish();
			}
		}, 2000);

		// buttonClick = (Button) findViewById(R.id.buttonClick);
		ch = (Chronometer) findViewById(R.id.chronometer1);
		ch.start();
//		send();
		Toast.makeText(getApplicationContext(), "Camera completed1",
				Toast.LENGTH_LONG).show();
		// finish();

		

//		Log.d(TAG, "onCreate'd");
	}

	public void send() {
		String[] det = null;
		try {
			File f = new File(detpath);

			FileInputStream fin = new FileInputStream(f);

			byte[] b = new byte[fin.available()];
			fin.read(b);
			String data = new String(b);
			det = data.split(",");
			fin.close();

			GmailSender sender = new GmailSender("prjaims@gmail.com",
					"prjaimsaims");
			sender.sendMail("Image", "Captured Image", det[3],
					img);
			Thread.sleep(4000);
			File ff = new File(img);
			ff.delete();

			Toast.makeText(getApplicationContext(), "Send completed",
					Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Never");
		}
	}

	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
			Log.d(TAG, "onShutter'd");
		}
	};

	/** Handles data for raw picture */
	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.d(TAG, "onPictureTaken - raw");
		}
	};

	/** Handles data for jpeg picture */
	PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			FileOutputStream outStream = null;
			try {
				// write to local sandbox file system
				// outStream =
				// CameraDemo.this.openFileOutput(String.format("%d.jpg",
				// System.currentTimeMillis()), 0);
				// Or write to sdcard
				outStream = new FileOutputStream(new File(String.format(
						Environment.getExternalStorageDirectory()
								+ "/aims1.jpg", System.currentTimeMillis())));
				outStream.write(data);
				outStream.close();
				send();
				Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length);
			} catch (Exception e) {
				e.printStackTrace();
			}

			Log.d(TAG, "onPictureTaken - jpeg");
		}
	};

}
