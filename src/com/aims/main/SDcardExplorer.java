package com.aims.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SDcardExplorer extends ListActivity{
	AlertDialog alert=null;
	private List<String> item=null;
	private List<String> path=null;
	private String root="/sdcard";
	public static String sel="";
	
	File file;
	Usreg ur;
	private TextView myPath;
	
	public void onCreate(Bundle icicle){
		super.onCreate(icicle);
		setContentView(R.layout.explorer);
		myPath=(TextView)findViewById(R.id.path);
		getDir(root);
		
		File f2=new File("/sdcard/aims");
		 try {
			
			f2.mkdir();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void getDir(String dirPath){
		myPath.setText("Location :"+dirPath);
		sel=dirPath;
		//Toast.makeText(getApplicationContext(), sel, Toast.LENGTH_SHORT).show();
		item= new ArrayList<String>();
		path=new ArrayList<String>();
		
		File f=new File(dirPath);
		File[] files=f.listFiles();
		if(!dirPath.equals(root)){
			item.add(root);
			path.add(root);
			item.add("../");
			path.add(f.getParent());
			
		}
		for (int index = 0; index < files.length; index++) {
			
			File file=files[index];
			path.add(file.getPath());
			if(file.isDirectory()){
				item.add(file.getName()+"/");
			}			
			else{
				item.add(file.getName());
			}
				
		}
		ArrayAdapter<String> fileList=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,item);
		setListAdapter(fileList);		
	}
	
	protected void onListItemClick(ListView l,View v,int position,long id){
		file=new File(path.get(position));
		
		//File file=new File(path.get(position));
	        
		
		    if(file.isDirectory()){
			if(file.canRead()){
				getDir(path.get(position));
		
				
				
			}
			else{
				new AlertDialog.Builder(this)
				.setTitle("Info")
				.setMessage("Folder cannot be read")
				.setNeutralButton("Close", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dlg, int sumthin) {
					}
				})
				.show();
			}
		}
		else{
			
			String st=file.getName();
		
		}			
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.clear();
		menu.add(0, 0, 0, "Select");
	    return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
    {
 
        switch (item.getItemId())
        {
        case 0:
        	if(file.isDirectory()){
        
        		Intent i = new Intent(SDcardExplorer.this,Usreg.class);
        		startActivity(i);

        	}else{
        		Toast.makeText(getApplicationContext(), "File backup not possible. Please click on a folder to take backup", Toast.LENGTH_SHORT).show();
        	}
        	
        	break;
 
      
        }
        return true;
    }   
	
	
}