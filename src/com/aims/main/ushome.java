package com.aims.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.aims.util.MyLocationListener;
import com.aims.util.SmsReceiver;

import constants.WebClient;
import constants.WebConUrl;

public class ushome extends Activity {

	LocationManager manager;
	ImageView mydevice;
	ImageView act;
	public static String[] auth = new String[10];
	public static String[] edit = new String[10];
	public static String[] sim = new String[80];
	public static Context ctx = null;
	static String apppath = Environment.getExternalStorageDirectory()
			+ "/user_reg.txt";
	static String simpath = Environment.getExternalStorageDirectory()
			+ "/sim_no.txt";
	String inp = "";
	static String imeino = "";
	static String fieldName = "";
	static String simserial = "";
	static String versionno = "";
	public static String sdcardfolder = "";
	String result = "";
	static String msgpath = Environment.getExternalStorageDirectory()
			+ "/messages.txt";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ushome);
		ctx = this.getApplicationContext();
		mydevice = (ImageView) findViewById(R.id.imageView1);
		act = (ImageView) findViewById(R.id.imageView2);
		try {
			FileInputStream fin = new FileInputStream(apppath);
			byte[] b = new byte[fin.available()];
			fin.read(b);
			String data = new String(b).trim();
			auth = data.split(",");
			fin.close();

		} catch (Exception e) {
			// TODO: handle exception
		}

		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		// get IMEI
		imeino = tm.getDeviceId();
		// get The Phone Number
		// String phone = tm.getLine1Number();
		simserial = tm.getSimSerialNumber();
		versionno = Build.VERSION.RELEASE;
		Field[] fields = Build.VERSION_CODES.class.getFields();
		for (Field field : fields) {
			fieldName = field.getName();
		}
		Toast.makeText(getBaseContext(), "new SimSerial : " + simserial,
				Toast.LENGTH_LONG).show();

		// String rep = WebClient.getData(WebConUrl.DEVICE_URL + "?user="
		// + auth[0] + "&imei=" + imeino + "&simser=" + simserial
		// + "&version=" + versionno + "&versionname=" + fieldName);
		// if (rep.equals("inserted")) {
		// try {
		// FileOutputStream fout = new FileOutputStream(simpath);
		//
		// byte[] b = simserial.getBytes();
		// fout.write(b);
		// fout.close();
		// } catch (Exception e) {
		// // TODO: handle exception
		// }
		// }

		try {
			FileInputStream fin = new FileInputStream(simpath);
			byte[] b = new byte[fin.available()];
			fin.read(b);
			result = new String(b).trim();
			Toast.makeText(getApplicationContext(), "SimSerial : " + result,
					Toast.LENGTH_LONG).show();
			fin.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		// String result = WebClient.getData(WebConUrl.SERIAL_URL + "?user="
		// + auth[0]);
		if (result.equals(simserial)) {

			Toast.makeText(getApplicationContext(), "equals",
					Toast.LENGTH_SHORT).show();
			try {
				Thread.sleep(2000);
				String repl = WebClient
						.getData(WebConUrl.DEVICE_URL + "?user=" + auth[0]
								+ "&imei=" + imeino + "&simser=" + simserial
								+ "&version=" + versionno + "&versionname="
								+ fieldName);
				// if(repl.equals("exist")){
				// FileOutputStream fout1 = new FileOutputStream(simpath);
				//
				// byte[] b1 = result.getBytes();
				// fout1.write(b1);
				// fout1.close();
				// }
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else {
			if (StartPage.MainActivity.j == 0) {
				Toast.makeText(getApplicationContext(),
						"new serial=" + simserial, Toast.LENGTH_SHORT).show();

				// String phone = WebClient.getData(WebConUrl.PHONE_URL +
				// "?user="+ auth[0]);
				String phoneNo = auth[2];
				// String phoneNo = "1-555-521-5556";
				System.out.println("phone>>>>>" + phoneNo);
				String message = "SIM CHANGE DETECTED!!!!!New Serial Number is "
						+ simserial;

				sendSMS(phoneNo, message);
			}
		}

		manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			buildAlertMessageNoGps();

		}

		LocationListener mlocListener = new MyLocationListener(this);
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10,
				mlocListener);

		mydevice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ushome.this, Mydevice.class);
				startActivity(i);
			}
		});
		act.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent j = new Intent(ushome.this, Contacts.class);
				startActivity(j);

			}
		});

	}

	private void sendSMS(String phoneNumber, String message) {
		// TODO Auto-generated method stub
		StartPage.MainActivity.j = 1;
		System.out.println("inside sendsms");
		PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this,
				ushome.class), 0);
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, pi, null);
		System.out.println("out sendsms");
		lock();

		// finish();
	}

	public void lock() {
		// TODO Auto-generated method stub
		String[] msg = null;
		File f = new File(msgpath);
		try {
			FileInputStream fin = new FileInputStream(f);

			byte[] b = new byte[fin.available()];
			fin.read(b);
			String data = new String(b);
			msg = data.split(",");
			fin.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(msg[2]);
		DevicePolicyManager mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		ActivityManager mAM = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		mDPM.resetPassword(msg[2], 1);
		mDPM.lockNow();
	}

	public boolean onCreateOptionsMenu(Menu m) {
		m.clear();
		m.add(0, 0, 0, "Settings");
		m.add(0, 1, 0, "Exit");

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case 0:
			getInput();
			break;
		case 1:
			Intent intet = new Intent(Intent.ACTION_MAIN);
			intet.addCategory(Intent.CATEGORY_HOME);
			intet.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intet);
			finish();
			break;

		}
		return true;
	}

	private void buildAlertMessageNoGps() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"Your GPS seems to be disabled, do you want to enable it?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(
									@SuppressWarnings("unused") final DialogInterface dialog,
									@SuppressWarnings("unused") final int id) {
								startActivity(new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog,
							@SuppressWarnings("unused") final int id) {
						dialog.cancel();
					}
				});
		final AlertDialog alert = builder.create();
		alert.show();
	}

	public String getInput() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Authentication");
		alert.setMessage("Enter secure password");
		final EditText input = new EditText(this);
		alert.setView(input);
		input.setHint("Enter your application password");
		input.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);

		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				inp = input.getText().toString();
				callExplorer(inp);
			}
		});
		alert.setNegativeButton("CANCEL",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		alert.show();
		return inp;

	}

	public void callExplorer(String password) {
		try {

			if (password.equals(auth[1])) {
				Intent i = new Intent();
				i.setClass(getApplicationContext(), Usreg.class);
				startActivity(i);
			} else {
				Toast.makeText(getApplicationContext(), "Invalid password",
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
