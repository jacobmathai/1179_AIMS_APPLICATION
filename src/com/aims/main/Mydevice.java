package com.aims.main;

import java.lang.reflect.Field;

import com.aims.util.MyLocationListener;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Mydevice extends Activity {

	ListView lv;
	
	LocationManager manager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mydevice);
		
		lv = (ListView)findViewById(R.id.devicelist);

		String imeino = ushome.imeino;
		//String phone = tm.getLine1Number();
		String simserial = ushome.simserial;
		String versionno = ushome.versionno;		
	    String fieldName = ushome.fieldName;

			
		String[] dev= new String[]{"IMEI\n"+imeino,"SIM SERIAL no\n"+simserial,"VERSION\n"+versionno,"VERSION NAME\n"+fieldName};
	    ArrayAdapter<String> sr = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,dev);
	    lv.setAdapter(sr);
	
	
	}

    

	

}
