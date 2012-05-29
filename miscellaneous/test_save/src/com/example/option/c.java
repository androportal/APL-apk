package com.example.option;
 import java.io.*;
import android.app.ActivityGroup;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.example.option.R;

public class c extends ActivityGroup {
    /** Called when the activity is first created. */
   
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main1);
        Button buttonSave = (Button)findViewById(R.id.button1);
        
        WebView engine = (WebView) findViewById(R.id.webView1); 
        
        WebSettings webSettings = engine.getSettings();
        webSettings.setJavaScriptEnabled(true);
        engine.loadUrl("file:///sdcard/mydir/c/index.html"); 
       // engine.loadUrl("localhost/html/c/index.html"); 
        //engine.loadUrl("http://10.101.201.172/html/c/index.html"); 
        //engine.loadUrl("http://10.101.201.192/html/c/index.html"); 
        //engine.loadUrl("http://127.0.0.1/html/c/index.html"); 
        engine.setWebChromeClient(new WebChromeClient()

        {
            @Override
            public void onConsoleMessage(String message, int lineNumber,String sourceID) {
                Log.d("MyApplication", message + " -- From line "+ lineNumber + " of " + sourceID);
                super.onConsoleMessage(message, lineNumber, sourceID);
            }

        });
        buttonSave.setOnClickListener(buttonSaveOnClickListener);         
    }
    
    Button.OnClickListener buttonSaveOnClickListener
    = new Button.OnClickListener(){

     @Override
     public void onClick(View arg0) {
      // TODO Auto-generated method stub
    	 WebView engine = (WebView) findViewById(R.id.webView1); 
    	 WebSettings webSettings = engine.getSettings();
         webSettings.setJavaScriptEnabled(true);
        // engine.addJavascriptInterface(new MyJSJavaBridge(), "api");

      engine.loadUrl("javascript:savecode()");
      engine.setWebChromeClient(new WebChromeClient()

      {
          @Override
          public void onConsoleMessage(String message, int lineNumber,String sourceID) {
              Log.d("MyApplication", message + " -- From line "+ lineNumber + " of " + sourceID);
              super.onConsoleMessage(message, lineNumber, sourceID);
          }

      });
     
      

     }

   };


    
}
