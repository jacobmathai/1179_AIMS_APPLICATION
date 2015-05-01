package com.aims.main;
import java.io.FileOutputStream;
import java.lang.reflect.Field;

import constants.WebClient;
import constants.WebConUrl;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
    /** Called when the activity is first created. */
	 EditText uname;
	 EditText pwd;
	static String user;
	static String apppath = Environment.getExternalStorageDirectory()
			+ "/user_reg.txt";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        uname = (EditText)findViewById(R.id.etxt_main_uname);
        uname.setHint("username");
        pwd = (EditText)findViewById(R.id.etxt_main_pwd);
        pwd.setHint("password");
        Button bt=(Button)findViewById(R.id.bt_main_login);
        Button bt1=(Button)findViewById(R.id.bt_main_cancel);
        bt.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 user= uname.getText().toString();
				String pass= pwd.getText().toString();
				String reply= WebClient.getData(WebConUrl.LOGIN_URL+"?user="+user+"&pass="+pass);
			
			
				if(reply.equals("invalid")){
					
					Toast.makeText(getApplicationContext(), "INVALID LOGIN",Toast.LENGTH_SHORT).show();
					
				}
				else
				{
					
					try {
						String imeino = "";
						String fieldName = "";
						String simserial = "";
						String versionno = "";
						String simpath = Environment.getExternalStorageDirectory()
								+ "/sim_no.txt";
						FileOutputStream fout = new FileOutputStream(apppath);
						String info = user+","+pass+","+reply;
						byte[] b = info.getBytes();
						fout.write(b);
						fout.close();
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

						String rep = WebClient.getData(WebConUrl.DEVICE_URL + "?user="
								+ user + "&imei=" + imeino + "&simser=" + simserial
								+ "&version=" + versionno + "&versionname=" + fieldName);
											
								FileOutputStream fout1 = new FileOutputStream(simpath);

								byte[] b1 = simserial.getBytes();
								fout1.write(b1);
								fout1.close();
							
						
						Toast.makeText(getApplicationContext(), "LOGIN VALID", Toast.LENGTH_SHORT).show();
						Intent i=new Intent(Login.this, ushome.class);
						startActivity(i);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
			
			
		}); 
        bt1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				

	        	Intent intet = new Intent(Intent.ACTION_MAIN);
	        	intet.addCategory(Intent.CATEGORY_HOME);
	        	intet.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        	startActivity(intet);
	        	finish();
				
			}
		});
    }
    
}