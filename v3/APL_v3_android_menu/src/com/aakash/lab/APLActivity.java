package com.aakash.lab;

import com.aakash.lab.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
//import android.webkit.WebView;
import android.widget.ImageButton;
//import android.widget.TableLayout;
//import android.widget.TableRow;

import android.widget.TextView;

public class APLActivity extends Activity implements OnClickListener{
	// Initialisation parameters for image buttons
	ImageButton rd1, rd2, rd3, rd4;
	// parameters for text view
	TextView tv1,tv2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);
	rd1 = (ImageButton)findViewById(R.id.imageButton1);
	rd2 = (ImageButton)findViewById(R.id.imageButton2);	
	rd3 = (ImageButton)findViewById(R.id.imageButton3);
	rd4 = (ImageButton)findViewById(R.id.imageButton4);
	tv1 = (TextView)findViewById(R.id.textView1);
	tv2 = (TextView)findViewById(R.id.textView2);
	rd1.setOnClickListener(this);
	rd2.setOnClickListener(this);	
	rd3.setOnClickListener(this);
	rd4.setOnClickListener(this);
	
	}
	

	public void onClick(View v) {
	// TODO Auto-generated method stub
	switch (v.getId()) {
	case R.id.imageButton1:
	
	Intent myIntent = new Intent(v.getContext(), c.class);
    startActivityForResult(myIntent, 0);
	break;
	case R.id.imageButton2:
	
	Intent myIntent1 = new Intent(v.getContext(), cp.class);
    startActivityForResult(myIntent1, 0);
	break;
	
	case R.id.imageButton3:
	
	Intent myIntent3 = new Intent(v.getContext(), py.class);
    startActivityForResult(myIntent3, 0);
	
	break;
	case R.id.imageButton4:
		
		Intent myIntent4 = new Intent(v.getContext(), sci.class);
	    startActivityForResult(myIntent4, 0);
		
		break;
	
	default:
	break;
	}
	}
	 @Override
	 // implemented application exit for the user
	 
	    public void onBackPressed() {
	     
	         AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage("Are you sure you want to exit?")
	           .setCancelable(false)
	           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   finish();
	            	   android.os.Process.killProcess(android.os.Process.myPid());
	               }
	           })
	           .setNegativeButton("No", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                    dialog.cancel();
	               }
	           });
	    AlertDialog alert = builder.create();
	    alert.show();
	        }
	
	
	}