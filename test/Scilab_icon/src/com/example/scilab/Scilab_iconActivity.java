package com.example.scilab;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class Scilab_iconActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        WebView engine = (WebView) findViewById(R.id.web_engine);  
        engine.loadUrl("http://10.0.2.2/html/scilab/scilab/"); 

    }
}