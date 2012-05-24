package multiple.language.imagebuttons;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

public class c extends Activity {
	
	private Button myButton;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	final Context context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c);

		 WebView engine = (WebView) findViewById(R.id.web_engine);  

       WebSettings webSettings = engine.getSettings();
        webSettings.setJavaScriptEnabled(true);
    	engine.loadUrl("file:///sdcard/proglang/c/index.html"); 
     
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





