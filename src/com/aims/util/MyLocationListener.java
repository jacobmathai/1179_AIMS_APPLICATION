package com.aims.util;

import com.aims.main.ushome;

import constants.WebClient;
import constants.WebConUrl;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

public class MyLocationListener implements LocationListener {
	
	Context h = null;
	public static double firstlat=0.0d;
	public static double firstlong=0.0d;
	
	public MyLocationListener(Context h){
		this.h=h;		
	}

@Override
	public void onLocationChanged(Location loc) {
		if(loc!=null){
			String Text="My current location is:"+" Latitud = "+loc.getLatitude()+" Longitud = "+loc.getLongitude();
			Toast.makeText(h, Text,Toast.LENGTH_LONG).show();
			
			firstlat=loc.getLatitude();
			firstlong=loc.getLongitude();
			
			WebClient.getData(WebConUrl.INSERT_URL + "?user=" + ushome.auth[0]
					+ "&lattitude=" + firstlat + "&longitude=" + firstlong);
		}
		else{
			Toast.makeText(h, "not coming", Toast.LENGTH_LONG).show();
		}		
		// TODO Auto-generated method stub		
	}

	public void onProviderDisabled(String provider) {
		Toast.makeText(h, "Gps Disabled", Toast.LENGTH_SHORT).show();
		// TODO Auto-generated method stub		
	}

	public void onProviderEnabled(String provider) {
		Toast.makeText(h, "Gps Enabled", Toast.LENGTH_SHORT).show();
		// TODO Auto-generated method stub		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub		
	}	
}
