package com.aims.util;


import com.aims.main.StartPage;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartMyServiceAtBootReceiver extends BroadcastReceiver {

   
	@Override
    public void onReceive(Context context, Intent intent) {
            Intent i = new Intent(context, StartPage.MainActivity.class);  
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);  
    }

}