package com.aims.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.aims.util.GmailSender;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

class Preview extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = "Preview";

	SurfaceHolder mHolder;
	public Camera camera;
	public Context context;
	String img = Environment.getExternalStorageDirectory() + "/aims1.jpg";
	static String detpath = Environment.getExternalStorageDirectory()
			+ "/user_reg.txt";

	@SuppressWarnings("deprecation")
	Preview(Context context) {
		super(context);
		this.context = context;

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, acquire the camera and tell it where
		// to draw.
		camera = Camera.open(1);
		try {
			camera.setPreviewDisplay(holder);

			camera.setPreviewCallback(new PreviewCallback() {

				public void onPreviewFrame(byte[] data, Camera arg1) {
					FileOutputStream outStream = null;
					try {
						outStream = new FileOutputStream(new File(String
								.format(Environment
										.getExternalStorageDirectory()
										+ "/aims1.jpg", System
										.currentTimeMillis())));
						outStream.write(data);
						outStream.close();
						Log.d(TAG, "onPreviewFrame - wrote bytes: "
								+ data.length);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
					}
					send();
					Preview.this.invalidate();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// Surface will be destroyed when we return, so stop the preview.
		// Because the CameraDevice object is not a shared resource, it's very
		// important to release it when the activity is paused.
		camera.stopPreview();
		camera = null;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// Now that the size is known, set up the camera parameters and begin
		// the preview.
		Camera.Parameters parameters = camera.getParameters();
		List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
		Camera.Size cs = sizes.get(0);
		parameters.setPreviewSize(cs.width, cs.height);
		camera.setParameters(parameters);
		camera.startPreview();
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		Paint p = new Paint(Color.RED);
		Log.d(TAG, "draw");
		canvas.drawText("PREVIEW", canvas.getWidth() / 2,
				canvas.getHeight() / 2, p);
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
			System.out.println(">>>>>>>>>>>>>>>>>>><<<<<<<<<<<      "
					+ det.length);
			fin.close();

			GmailSender sender = new GmailSender("mobocop2k15@gmail.com",
					"mobocop@2k15");
			sender.sendMail("Image", "Captured Image", det[3], img);
			Thread.sleep(4000);
			File ff = new File(img);
			ff.delete();

			Toast.makeText(context, "Send completed", Toast.LENGTH_LONG).show();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Never");
		}
		
	}
}