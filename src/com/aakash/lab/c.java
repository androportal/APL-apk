package com.aakash.lab;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.R.anim;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.TextView;

// Initialise the parameters for open implementation

public class c extends ActivityGroup {
	private String[] mFileList;
	private File mPath;
	private String mChosenFile;
	private static final String FTYPE = ".c";
	private static final int DIALOG_LOAD_FILE = 1000;
	private String oe_path;
	// added for example and open
	private String write_path;
	private String ex_flag = "open";
    
	/** Called when the activity is first created. */
	@Override
	// menu creation
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu1, menu);
		return true;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main1);
		// web view for c class
		WebView engine = (WebView) findViewById(R.id.webView1);
		WebSettings webSettings = engine.getSettings();
		// java script enabled
		webSettings.setJavaScriptEnabled(true);
		// js interface for reload
		engine.addJavascriptInterface(new JsInterface(), "android");
		// cache problem removed
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setAppCacheEnabled(false);
		// scroll bars disabled
		engine.setVerticalScrollBarEnabled(false);
		engine.setHorizontalScrollBarEnabled(false);

		// focused the web page
		engine.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_UP:
					if (!v.hasFocus()) {
						v.requestFocus();
					}
					break;
				}
				return false;
			}

			public boolean onTouch1(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		engine.loadUrl("http://127.0.0.1/html/c/index.html");

		engine.setWebChromeClient(new WebChromeClient()

		{
			@Override
			public void onConsoleMessage(String message, int lineNumber,
					String sourceID) {
				Log.d("MyApplication", message + " -- From line " + lineNumber
						+ " of " + sourceID);
				super.onConsoleMessage(message, lineNumber, sourceID);
			}

		});

		// web view for shell in a box
		WebView engine1 = (WebView) findViewById(R.id.webView2);

		WebSettings webSettings1 = engine1.getSettings();
		// java script enabled
		webSettings1.setJavaScriptEnabled(true);
		// scroll bars disabled

		engine1.setVerticalScrollBarEnabled(false);
		engine1.setHorizontalScrollBarEnabled(false);
		// web page focused

		engine1.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_UP:
					if (!v.hasFocus()) {
						v.requestFocus();
					}
					break;
				}
				return false;
			}

			public boolean onTouch1(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		// address of page for shell in a box

		engine1.loadUrl("http://127.0.0.1:4200");
		engine1 = new WebView(this);
		engine1.reload();

		engine1.setWebChromeClient(new WebChromeClient()

		{
			@Override
			public void onConsoleMessage(String message, int lineNumber,
					String sourceID) {
				Log.d("MyApplication", message + " -- From line " + lineNumber
						+ " of " + sourceID);
				super.onConsoleMessage(message, lineNumber, sourceID);
			}

		});

	}

	// function for save
	public void test() {

		// TODO Auto-generated method stub
		WebView engine = (WebView) findViewById(R.id.webView1);

		WebSettings webSettings = engine.getSettings();
		webSettings.setJavaScriptEnabled(true);
		engine.getSettings().setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setAppCacheEnabled(false);
		engine.setWebChromeClient(new MyWebChromeClient());

		engine.loadUrl("javascript:savecode()");

	}

	// function for open
	public void openFile() {

		// TODO Auto-generated method stub
		WebView engine = (WebView) findViewById(R.id.webView1);
		WebSettings webSettings = engine.getSettings();
		webSettings.setJavaScriptEnabled(true);
		engine.getSettings().setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setAppCacheEnabled(false);
		engine.setWebChromeClient(new MyWebChromeClient());
		if (ex_flag == "open")
			engine.loadUrl("javascript:submit_file()");
		else if (ex_flag == "example")
			engine.loadUrl("javascript:example_file()");
	}

	// android file explorer
	private void loadFileList() {

		try {
			mPath.mkdirs();
		} catch (SecurityException e) {
			System.out.println("unable to write on the sd card ");
		}
		if (mPath.exists()) {

			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File dir, String filename) {
					File sel = new File(dir, filename);

					return filename.contains(FTYPE) || sel.isDirectory();
				}
			};
			mFileList = mPath.list(filter);

			onCreateDialog(DIALOG_LOAD_FILE);

		} else {
			mFileList = new String[0];
		}
	}

	protected Dialog onCreateDialog(int id) {

		Dialog dialog = null;
		AlertDialog.Builder builder = new Builder(this);

		switch (id) {
		case DIALOG_LOAD_FILE:

			builder.setTitle("Choose your file");
			if (mFileList == null) {
				System.out
						.println("Showing file picker before loading the file list ");
				dialog = builder.create();
				return dialog;
			}
			builder.setItems(mFileList, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					mChosenFile = mFileList[which];

					InputStream inStream = null;
					OutputStream outStream = null;

					try {
						File bfile = new File(write_path);
						inStream = new FileInputStream(oe_path + mChosenFile);
						outStream = new FileOutputStream(bfile);

						byte[] buffer = new byte[1024];

						int length;
						// copy the file content in bytes
						while ((length = inStream.read(buffer)) > 0) {

							outStream.write(buffer, 0, length);

						}

						inStream.close();
						outStream.close();
						openFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			break;
		}
		dialog = builder.show();
		return dialog;
	}

	// menu options by switch case
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.open:
			ex_flag = "open";
			write_path = "/data/local/linux/var/www/html/c/code/.open_file.c";
			oe_path = Environment.getExternalStorageDirectory()
					+ "/APL/c/code/";
			mPath = new File(oe_path);
			loadFileList();
			return true;
		case R.id.savecode1:
			test();
			return true;
		case R.id.example:
			ex_flag = "example";
			write_path = "/data/example/c/.open_file.c";
			oe_path = "/data/local/linux/var/www/html/c/example/";
			mPath = new File(oe_path);
			loadFileList();
			return true;
		case R.id.help:
			Intent myIntent = new Intent(c.this, chelp.class);
			startActivityForResult(myIntent, 0);

			return true;
		case R.id.about:
			AlertDialog about_dialog;
			final SpannableString s = new SpannableString(c.this.getText(R.string.about_para1));
			Linkify.addLinks(s, Linkify.WEB_URLS);
						
	        // Building DatepPcker dialog
	        AlertDialog.Builder builder = new AlertDialog.Builder(
	                c.this);
	        builder.setTitle(R.string.about_heading);
	        builder.setIcon(R.drawable.apl);
	        builder.setMessage(s);
	        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	                // TODO Auto-generated method stub
	                
	            }
	          
	        });
	      
	        about_dialog = builder.create();
	        about_dialog.show();
	        ((TextView)about_dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
	        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	        // customizing the width and location of the dialog on screen
	        lp.copyFrom(about_dialog.getWindow().getAttributes());
	        lp.width = 600;
	        
	        
	        about_dialog.getWindow().setAttributes(lp);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// web chrome client for the web view feature
	final class MyWebChromeClient extends WebChromeClient

	{

		c jb = new c();

		@Override
		public void onConsoleMessage(String message, int lineNumber,
				String sourceID) {
			Log.d("MyApplication", message + " -- From line " + lineNumber
					+ " of " + sourceID);
			super.onConsoleMessage(message, lineNumber, sourceID);

		}

		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			Log.d("LogTag", message);

			result.confirm();
			return true;
		}

	}

	// js interface to establish communication between the android and
	// javascript for reloading the webpage for shell in a box
	private class JsInterface {
		public void reloadConsole() {
			/* below put id of second webview which has shell in a box */
			WebView engine1 = (WebView) findViewById(R.id.webView2);
			WebSettings webSettings1 = engine1.getSettings();
			webSettings1.setJavaScriptEnabled(true);
			try{
			engine1.reload();
			}catch(Exception e){
				e.printStackTrace();
				engine1.reload();
			}  
			
		}   
	}

}
