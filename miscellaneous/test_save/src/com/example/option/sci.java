package com.example.option;

import android.app.ActivityGroup;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.option.R;

public class sci extends ActivityGroup {
    /** Called when the activity is first created. */
   
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main1);
        WebView engine = (WebView) findViewById(R.id.webView1); 
        
        WebSettings webSettings = engine.getSettings();
        webSettings.setJavaScriptEnabled(true);
        engine.loadUrl("file:///sdcard/mydir/html/scilab/index.html"); 
        //engine.loadUrl("localhost/html/scilab/index.html");
        //engine.loadUrl("http://10.101.201.172/html/scilab/index.html"); 
        //engine.loadUrl("http://10.101.201.192/html/scilab/index.html"); 
        //engine.loadUrl("http://127.0.0.1/html/scilab/index.html"); 
        engine.setWebChromeClient(new WebChromeClient()

        {
            @Override
            public void onConsoleMessage(String message, int lineNumber,String sourceID) {
                Log.d("MyApplication", message + " -- From line "+ lineNumber + " of " + sourceID);
                super.onConsoleMessage(message, lineNumber, sourceID);
            }

        });

        
      

    }
}
