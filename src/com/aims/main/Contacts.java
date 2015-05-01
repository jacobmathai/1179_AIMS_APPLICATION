package com.aims.main;

import constants.WebClient;
import constants.WebConUrl;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

;

public class Contacts extends Activity {
	
	
	   Cursor cursor=null;
	 String details="";
	 static ContentResolver cr;// = getContentResolver();
	ArrayAdapter<String> myAdapterInstance;
	SimpleCursorAdapter adapter;
	String[] projection = new String[] { Phone._ID, Phone.DISPLAY_NAME,
			Phone.NUMBER };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts);
		
		
		
		/*
		 * Cursor cursor = getContentResolver().query(People.CONTENT_URI, null,
		 * null, null, null);
		 */
		 cursor = managedQuery(Phone.CONTENT_URI, projection, null,
				null, Phone.DISPLAY_NAME + " ASC");

		final ListView lv = (ListView) findViewById(R.id.listView2);

		adapter = new SimpleCursorAdapter(this, // Context
				R.layout.contactslist, // xml definintion of each listView item
				cursor, // Cursor
				new String[] { "display_name", Phone.NUMBER }, // Columns to
																// select From
				new int[] { R.id.textView11, R.id.textView12 } // Object to bind
																// to
		);

	
		lv.setAdapter(adapter);
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
	//	readcontacts();
		
	}
//	public  void readcontacts() {
//		// TODO Auto-generated method stub
//		 while (cursor.moveToNext()) {
//		        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//		        String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//		     
//		         details+=name+":"+phone+",";
//		       
//		        
//		    }
//		 System.out.println(details);
//		 WebClient.getData(WebConUrl.PHONE_URL + "?user=" + ushome.auth[0]
//					+ "&contacts=" + details);
//	}
	public boolean onCreateOptionsMenu(Menu m) {
		m.clear();
	
		m.add(0, 1, 0, "Back");

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		
		case 1:
			Intent j=new  Intent(Contacts.this, ushome.class);
			startActivity(j);
			finish();
			break;

		}
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}
}
