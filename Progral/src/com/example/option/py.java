

package com.example.option;

import android.app.ActivityGroup;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.option.R;

public class py extends ActivityGroup {
    /** Called when the activity is first created. */
   
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main1);
        WebView engine = (WebView) findViewById(R.id.web_engine); 
        
        WebSettings webSettings = engine.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //engine.loadUrl("file:///sdcard/mydir/html/python/index.html"); 
        //engine.loadUrl("http://10.101.201.172/html/python/index.html"); 
        //engine.loadUrl("http://10.101.201.192/html/python/index.html"); 
        engine.loadUrl("localhost/html/python/index.html");
        
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

