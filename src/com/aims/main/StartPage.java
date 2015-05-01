package com.aims.main;

import java.io.File;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;

public class StartPage extends DeviceAdminReceiver {

public static class MainActivity extends Activity {
	/** Called when the activity is first created. */
	
	 static final int RESULT_ENABLE = 1;

     public static DevicePolicyManager mDPM;
     ActivityManager mAM;
     public static ComponentName mDeviceAdminSample;
     
	static String apppath = Environment.getExternalStorageDirectory()
			+ "/user_reg.txt";
	static int j = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        mAM = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        mDeviceAdminSample = new ComponentName(MainActivity.this, StartPage.class);
        
        Intent intent = new   Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);  
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);   
       // intent.putExtra(DevicePolicyManager.ACTION_SET_NEW_PASSWORD, mDeviceAdminSample);   
        startActivityForResult(intent, RESULT_ENABLE);
		setContentView(R.layout.main);
		
		WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		boolean b=wifiManager.isWifiEnabled();
		if(!b){
			wifiManager.setWifiEnabled(true);
		}
	

		new Thread() {
			@Override
			public void run() {
				for (int i = 0; i < 5; i++) {

					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				try {
					File f = new File(apppath);
					if (!f.exists()) {
						
						Intent i = new Intent(MainActivity.this, Login.class);
						startActivity(i);
					} else {
						Intent i = new Intent(MainActivity.this, ushome.class);
						startActivity(i);
					}

				} catch (Exception e) {
					// TODO: handle exception
				}

			}

		}.start();

	}
}
}