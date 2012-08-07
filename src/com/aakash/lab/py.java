package com.aakash.lab;

import java.io.*;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.aakash.lab.R;
import com.aakash.lab.c.MyWebChromeClient;

import android.webkit.JsResult;

public class py extends ActivityGroup {

	// Initialise the parameters for open implementation

	private String[] mFileList;
	private File mPath;
	private String mChosenFile;
	private static final String FTYPE = ".py";
	private static final int DIALOG_LOAD_FILE = 1000;
	private String oe_path;
	//added for example and open	
	private String write_path;
	private String ex_flag="open";

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

		// web view for the code mirror part

		WebView engine = (WebView) findViewById(R.id.webView1);

		WebSettings webSettings = engine.getSettings();
		// java script enabled
		webSettings.setJavaScriptEnabled(true);
		// js interface for the webview
		engine.addJavascriptInterface(new JsInterface(), "android");
		// Cache problem sort
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setAppCacheEnabled(false);
		// scroll bars disabled to avoid the shifting of screen
		engine.setVerticalScrollBarEnabled(false);
		engine.setHorizontalScrollBarEnabled(false);
		// proper focus on the webpage
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
		// url for the code mirror appliction for python
		engine.loadUrl("http://127.0.0.1/html/python/index.html");
		// to enable the pop ups in the web view
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
		// web view for the shell in a box

		WebView engine1 = (WebView) findViewById(R.id.webView2);

		WebSettings webSettings1 = engine1.getSettings();
		// java script enabled
		webSettings1.setJavaScriptEnabled(true);
		// scroll bars disabled for the shifting of the screen
		engine1.setVerticalScrollBarEnabled(false);
		engine1.setHorizontalScrollBarEnabled(false);

		// url for the shell in a box, run on loacl host
		engine1.loadUrl("http://127.0.0.1:4200");
		// proper focus on the webpage of shell in a box
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
		// enabled pop ups for the web page
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

	// function for saving the code
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

	// function for the open
	public void openFile() {

		// TODO Auto-generated method stub
		WebView engine = (WebView) findViewById(R.id.webView1);
		WebSettings webSettings = engine.getSettings();
		webSettings.setJavaScriptEnabled(true);
		engine.getSettings().setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setAppCacheEnabled(false);
		engine.setWebChromeClient(new MyWebChromeClient());
		if(ex_flag=="open")
	        	 engine.loadUrl("javascript:submit_file()");
	        else if(ex_flag=="example")	        
	        	 engine.loadUrl("javascript:example_file()");
	}

	// implemented android file explorer for listing the files
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

	// menu for selecting the different options
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.open:
			ex_flag="open";
	            	write_path="/data/local/linux/var/www/html/python/code/.open_file.py";
			oe_path = Environment.getExternalStorageDirectory()
					+ "/APL/python/code/";
			mPath = new File(oe_path);
			loadFileList();
			return true;
		case R.id.savecode1:
			test();
			return true;
		case R.id.example:
			ex_flag="example";
	            	write_path="/data/example/python/.open_file.py";
			oe_path = "/data/local/linux/var/www/html/python/example/";
			mPath = new File(oe_path);
			loadFileList();
			return true;
		case R.id.help:

			Intent myIntent = new Intent(py.this, pyhelp.class);
			startActivityForResult(myIntent, 0);

			return true;
		case R.id.about:

			Intent myIntent1 = new Intent(py.this, about.class);
			startActivityForResult(myIntent1, 0);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// webchrome client for the webpage feature
	final class MyWebChromeClient extends WebChromeClient

	{

		py jb = new py();

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

	// js interface for the reload of the web page of the shell in a box
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
