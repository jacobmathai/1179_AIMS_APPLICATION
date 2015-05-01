package com.aims.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.aims.util.FolderZipper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Usreg extends Activity {

	static EditText profile, lock, backup, lockpass, camera;
	public static TextView sdcardpath;
	Button confirm, back, browse;
	public static String promsg = "";
	public static String lockmsg = "";
	public static String backupmsg = "";
	public static String lockpassword = "";
	public static String backupfolderpath = "";
	public static String cameramsg = "";

	static String msgpath = Environment.getExternalStorageDirectory()
			+ "/messages.txt";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usreg);

		profile = (EditText) findViewById(R.id.et_usreg_name);
		lock = (EditText) findViewById(R.id.et_usreg_eid);
		lockpass = (EditText) findViewById(R.id.et_usreg_bno);
		backup = (EditText) findViewById(R.id.et_backupmsg);
		sdcardpath = (TextView) findViewById(R.id.tv_sdcard_path);
		camera = (EditText) findViewById(R.id.et_cameramsg);

		browse = (Button) findViewById(R.id.bt_browse);
		confirm = (Button) findViewById(R.id.btpasss);
		back = (Button) findViewById(R.id.btback);

		profile.setText(promsg);
		lock.setText(lockmsg);
		lockpass.setText(lockpassword);
		backup.setText(backupmsg);
		sdcardpath.setText(SDcardExplorer.sel);

		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Usreg.this, ushome.class);
				startActivity(i);
			}
		});

		browse.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				promsg = profile.getText().toString();
				lockmsg = lock.getText().toString();
				backupmsg = backup.getText().toString();
				lockpassword = lockpass.getText().toString();
				Intent i = new Intent(Usreg.this, SDcardExplorer.class);
				startActivity(i);
			}
		});

		confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				promsg = profile.getText().toString();
				lockmsg = lock.getText().toString();
				backupmsg = backup.getText().toString();
				lockpassword = lockpass.getText().toString();
				backupfolderpath = sdcardpath.getText().toString();
				cameramsg = camera.getText().toString();
				ushome.sdcardfolder = SDcardExplorer.sel;
				File f = new File(msgpath);
				try {
					FileOutputStream fout = new FileOutputStream(f);
					String cont = promsg + "," + lockmsg + "," + lockpassword
							+ "," + backupmsg + "," + cameramsg + ",";
					fout.write(cont.getBytes());
					fout.close();

					FolderZipper.zipFolder(backupfolderpath,
							"/sdcard/aims/backup.zip");

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Toast.makeText(getApplicationContext(), "Settings saved",
						Toast.LENGTH_SHORT).show();
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					// TODO: handle exception
				}
				Intent i = new Intent(Usreg.this, ushome.class);
				startActivity(i);

			}
		});
	}

}
