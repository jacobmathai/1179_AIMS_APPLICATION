package com.aims.main;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aims.util.GmailSender;

public class Androidcamera extends Activity implements SurfaceHolder.Callback  {
	Camera camera;
	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;
	boolean previewing = false;
	LayoutInflater controlInflater = null;

	final int RESULT_SAVEIMAGE = 0;
	String img = Environment.getExternalStorageDirectory()
			+ "/capture.jpg";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main1);		
		Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFormat(PixelFormat.UNKNOWN);
		surfaceView = (SurfaceView) findViewById(R.id.camerapreview);
		surfaceView.setVisibility(0);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		controlInflater = LayoutInflater.from(getBaseContext());
		View viewControl = controlInflater.inflate(R.layout.control, null);
		LayoutParams layoutParamsControl = new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		this.addContentView(viewControl, layoutParamsControl);

	

		LinearLayout layoutBackground = (LinearLayout) findViewById(R.id.linearl);
		try{
		Thread.sleep(2000);
		} catch (Exception e) {
			// TODO: handle exception
		}
		camera.autoFocus(myAutoFocusCallback);
		
		layoutBackground.performClick();
		layoutBackground.setOnClickListener(new LinearLayout.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
		
				camera.autoFocus(myAutoFocusCallback);
				//try {
				//Thread.sleep(2000);
				//send();
				//} catch (Exception e) {
					// TODO: handle exception
				//}
			}
		});
		
		new Thread(){
			public void run() {
				
				try {
					for (int i = 0; i < 10; i++) {
						Thread.sleep(250);
					}
					
					camera.takePicture(myShutterCallback, myPictureCallback_RAW,
							myPictureCallback_JPG);
					Thread.sleep(2000);
					StartPage.MainActivity.mDPM.lockNow();
					Thread.sleep(2000);
					send();
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			};
		}.start();

	}

	AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback() {

		@Override
		public void onAutoFocus(boolean arg0, Camera arg1) {
			// TODO Auto-generated method stub
			// buttonTakePicture.setEnabled(true);
			camera.takePicture(myShutterCallback, myPictureCallback_RAW,
					myPictureCallback_JPG);

		}
	};

	ShutterCallback myShutterCallback = new ShutterCallback() {

		@Override
		public void onShutter() {
			// TODO Auto-generated method stub

		}
	};

	PictureCallback myPictureCallback_RAW = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] arg0, Camera arg1) {
			// TODO Auto-generated method stub

		}
	};

	PictureCallback myPictureCallback_JPG = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] arg0, Camera arg1) {
			// TODO Auto-generated method stub
			/*
			 * Bitmap bitmapPicture = BitmapFactory.decodeByteArray(arg0, 0,
			 * arg0.length);
			 */

			// Uri uriTarget =
			// getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, new
			// ContentValues());
			File file = new File(Environment.getExternalStorageDirectory()
					+ File.separator + "capture.jpg");
			OutputStream imageFileOS;
			try {
				// imageFileOS =
				// getContentResolver().openOutputStream(uriTarget);
				imageFileOS = new FileOutputStream(file);
				imageFileOS.write(arg0);
				imageFileOS.flush();
				imageFileOS.close();
				Toast.makeText(Androidcamera.this,
						"Image saved: " + file.getAbsolutePath(),
						Toast.LENGTH_LONG).show();
				send();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			camera.startPreview();
		}

	};
	static String detpath = Environment.getExternalStorageDirectory()
			+ "/user_reg.txt";
	public void send(){
		String[] det = null;
		try {
			File f = new File(detpath);
			try {
				FileInputStream fin = new FileInputStream(f);

				byte[] b = new byte[fin.available()];
				fin.read(b);
				String data = new String(b);
				det = data.split(",");
				fin.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			GmailSender sender = new GmailSender("akhilsivan987@gmail.com",
					"chathayam987");
			sender.sendMail("Image", "Captured Image",
					det[4], img);
			Thread.sleep(4000);
			File ff = new File(img);
			ff.delete();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	}
	/*
	@Override
	public void onAttachedToWindow()
	{  
	       this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);     
	       super.onAttachedToWindow();  
	}*/
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		if (previewing) {
			camera.stopPreview();
			previewing = false;
		}

		if (camera != null) {
			try {
				camera.setPreviewDisplay(surfaceHolder);
				camera.startPreview();
				previewing = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		camera = Camera.open(1);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		camera.stopPreview();
		camera.release();
		camera = null;
		previewing = false;
	}
}

