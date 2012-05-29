package com.example.option;



import com.example.option.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;

import android.widget.TextView;

public class Test_saveActivity extends Activity implements OnClickListener{
	//RadioGroup rg;
	ImageButton rd1, rd2;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        rd1 = (ImageButton)findViewById(R.id.imageButton1);
    	rd2 = (ImageButton)findViewById(R.id.imageButton2);
    	rd1.setOnClickListener(this);
    	rd2.setOnClickListener(this);
    }
    public void onClick(View v) {
    	// TODO Auto-generated method stub
    	switch (v.getId()) {
    	case R.id.imageButton1:
    	
    	Intent myIntent = new Intent(v.getContext(), c.class);
        startActivityForResult(myIntent, 0);
    	break;
    	case R.id.imageButton2:
    	
    	Intent myIntent1 = new Intent(v.getContext(), sci.class);
        startActivityForResult(myIntent1, 0);
    	break;
    	
    	
    	
    	default:
    	break;
    	}
    	}
    	}
