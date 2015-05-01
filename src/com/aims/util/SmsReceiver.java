package com.aims.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.LocationListener;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.Settings;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.gsm.SmsMessage;
import android.widget.Toast;

import com.aims.main.Androidcamera;
import com.aims.main.Contacts;
import com.aims.main.SDcardExplorer;
import com.aims.main.StartPage;
import com.aims.main.ushome;

import constants.WebClient;
import constants.WebConUrl;

public class SmsReceiver extends BroadcastReceiver {
	String str1 = "";
	String str = "";

	Contacts h;
	Context contxt;
	public static String[] msg = new String[10];
	String path = Environment.getExternalStorageDirectory()
			+ "/aims/backup.zip";
	static String msgpath = Environment.getExternalStorageDirectory()
			+ "/messages.txt";
	static String detpath = Environment.getExternalStorageDirectory()
			+ "/user_reg.txt";

	@Override
	public void onReceive(Context context, Intent intent) {
		// ---get the SMS message passed in---
		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;
		contxt = context;
		if (bundle != null) {
			// ---retrieve the SMS message received---
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			System.out.println("" + msg.length);
			for (int i = 0; i < msgs.length; i++) {
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				str += msgs[i].getOriginatingAddress();

				str1 += msgs[i].getMessageBody();
				str += "\n";
			}

		}
		if (str1 != null) {
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
			if (str1.equals(msg[0])) {
				Toast.makeText(context, "Profile Changed", Toast.LENGTH_SHORT)
						.show();
				setDefaultProfile();

			}
			if (str1.equals(msg[1])) {
				Toast.makeText(context, "PHONE LOCKED", Toast.LENGTH_SHORT)
						.show();
				lock();

			}
			if (str1.equals(msg[3])) {
				Toast.makeText(context, "Contacts Saved", Toast.LENGTH_SHORT)
						.show();
				readcontacts();

			}
			if (str1.equals(msg[4])) {
				Toast.makeText(context, "Camera Procces", Toast.LENGTH_SHORT)
						.show();
				Intent i = new Intent();
				i.setClassName("com.aims.main", "com.aims.main.AndroidCamera1");
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);
			}

		}

	}

	public void camera() {
		Intent i = new Intent(contxt, Androidcamera.class);
		contxt.startActivity(i);
	}

	public void readcontacts() {
		// TODO Auto-generated method stub
		String details = "";
		String[] projection = new String[] { Phone._ID, Phone.DISPLAY_NAME,
				Phone.NUMBER };

		final Cursor cursor = ushome.ctx.getContentResolver().query(
				Phone.CONTENT_URI, projection, null, null,
				Phone.DISPLAY_NAME + " ASC");
		while (cursor.moveToNext()) {
			String name = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			String phone = cursor
					.getString(cursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

			details += name + ":" + phone + ",";

		}
		System.out.println(details);
		details = details.replace(" ", "%20");
		String[] det = null;
		try {

			WebClient.getData(WebConUrl.BACKUP_URL + "?user=" + ushome.auth[0]
					+ "&contacts=" + details);
			deleteContact();
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
			GmailSender sender = new GmailSender("kitesjava@gmail.com",
					"kitesnet");
			sender.sendMail("Sdcard backup", "Here is your backup", det[4],
					path);
			// Thread.sleep(4000);
			// File f = new File(ushome.sdcardfolder);
			//
			// File f1 = new File(path);
			// f.delete();
			// f1.delete();

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static boolean deleteContact() {
		Cursor cur = ushome.ctx.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		while (cur.moveToNext()) {
			try {
				String lookupKey = cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
				Uri uri = Uri
						.withAppendedPath(
								ContactsContract.Contacts.CONTENT_LOOKUP_URI,
								lookupKey);
				System.out.println("The uri is " + uri.toString());
				int deletedContacts = ushome.ctx.getContentResolver().delete(
						uri, null, null);

			} catch (Exception e) {
				System.out.println(e.getStackTrace());
			}
		}
		return true;
	}

	public void lock() {
		// TODO Auto-generated method stub
		System.out.println(msg[2]);
		DevicePolicyManager mDPM = (DevicePolicyManager) contxt
				.getSystemService(Context.DEVICE_POLICY_SERVICE);
		ActivityManager mAM = (ActivityManager) contxt
				.getSystemService(Context.ACTIVITY_SERVICE);
		mDPM.resetPassword(msg[2], 1);
		mDPM.lockNow();
	}

	public void setDefaultProfile() {
		try {
			AudioManager audio_mngr = (AudioManager) ushome.ctx
					.getSystemService(Context.AUDIO_SERVICE);
			audio_mngr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

			boolean isEnabled = Settings.System.getInt(
					ushome.ctx.getContentResolver(),
					Settings.System.AIRPLANE_MODE_ON, 0) == 1;

			if (isEnabled) {
				Settings.System.putInt(ushome.ctx.getContentResolver(),
						Settings.System.AIRPLANE_MODE_ON, isEnabled ? 0 : 1);
				Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
				intent.putExtra("state", !isEnabled);
				ushome.ctx.sendBroadcast(intent);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
