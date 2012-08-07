package com.aakash.lab;

import java.io.*;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.Dialog;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;

import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.aakash.lab.R;
import com.aakash.lab.SimpleGestureFilter.SimpleGestureListener;
import com.aakash.lab.SimpleGestureFilter;

import android.webkit.JsResult;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;

// SimpleGesture for swipe feature
public class sci extends ActivityGroup implements SimpleGestureListener {
	// parameters for the load list of the files in open
	private String[] mFileList;
	private File mPath;
	private String mChosenFile;
	private static final String FTYPE = ".cde";
	private static final int DIALOG_LOAD_FILE = 1000;
	private String oe_path;
	//added for example and open	
	private String write_path;
	private String ex_flag="open";
	/** Called when the activity is first created. */

	@Override
	// menu creation
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);

		if (!flag) {
			menu.removeItem(R.id.savefigure);
		}
		return true;
	}

	// flag defined for the image file
	/** Called when the activity is first created. */
	static boolean flag = false;

	private SimpleGestureFilter detector;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main1);

		detector = new SimpleGestureFilter(this, this);

		// web view for the code mirror part
		WebView engine = (WebView) findViewById(R.id.webView1);

		WebSettings webSettings = engine.getSettings();
		// java script enabled
		webSettings.setJavaScriptEnabled(true);
		// js interface to implement the reload feature
		engine.addJavascriptInterface(new JsInterface(), "android");
		// cache problem sort
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setAppCacheEnabled(false);
		// scroll bars disabled to avoid shifting of the screen
		engine.setVerticalScrollBarEnabled(false);
		engine.setHorizontalScrollBarEnabled(false);
		// web page focused
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

		// url for the scilab to load in the application
		engine.loadUrl("http://127.0.0.1/html/scilab/index.html");
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
		// url for the shell in a box

		engine1.loadUrl("http://127.0.0.1:4200");
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

	// swipe feature implemented with direction and proper orientation
	@Override
	public boolean dispatchTouchEvent(MotionEvent me) {
		this.detector.onTouchEvent(me);
		return super.dispatchTouchEvent(me);
	}

	public void onSwipe(int direction) {

		switch (direction) {

		case SimpleGestureFilter.SWIPE_LEFT:
			// image plot activity called
			if (flag) {
				Intent myIntent = new Intent(sci.this, ImagePlotActivity.class);
				startActivityForResult(myIntent, 0);
			}
			break;
		}
	}

	public void onDoubleTap() {
	}

	// setting of flags, for enabling the swipe feature and the save figure
	// option
	public static void setFlag() {
		flag = true;
	}

	public static void clearFlag() {
		flag = false;
	}

	// js interface for the web page reload and show plot for the graphical
	// output on press of execute button in the webpage
	private class JsInterface {
		public void showPlot() {

			Intent myIntent = new Intent(sci.this, ImagePlotActivity.class);
			startActivityForResult(myIntent, 0);
			setFlag();

		}

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

	// function for save code
	public void test() {
		// TODO Auto-generated method stub
		WebView engine = (WebView) findViewById(R.id.webView1);
		// engine.setWebViewClient(new MyWebViewClient());
		WebSettings webSettings = engine.getSettings();
		webSettings.setJavaScriptEnabled(true);
		engine.getSettings().setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setAppCacheEnabled(false);
		engine.setWebChromeClient(new MyWebChromeClientsci());

		engine.loadUrl("javascript:savecode()");
	}

	// function for the save figure
	public void test1() {
		// TODO Auto-generated method stub
		WebView engine = (WebView) findViewById(R.id.webView1);
		WebSettings webSettings = engine.getSettings();
		webSettings.setJavaScriptEnabled(true);
		engine.getSettings().setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setAppCacheEnabled(false);
		engine.setWebChromeClient(new MyWebChromeClientsci());
		engine.loadUrl("javascript:saveImg()");
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
		engine.setWebChromeClient(new MyWebChromeClientsci());
		if(ex_flag=="open")
	        	 engine.loadUrl("javascript:submit_file()");
	        else if(ex_flag=="example")	        
	        	 engine.loadUrl("javascript:example_file()");
	}

	// load list to display all the files for open option
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
				// Log.e(TAG,
				// "Showing file picker before loading the file list");
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

	@Override
	// menu item selection
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection

		switch (item.getItemId()) {
		case R.id.open:
			ex_flag="open";
	            	write_path="/data/local/linux/var/www/html/scilab/code/.open_file.cde";
			oe_path = Environment.getExternalStorageDirectory()
					+ "/APL/scilab/code/";
			mPath = new File(oe_path);
			loadFileList();
			return true;
		case R.id.savecode1:
			test();
			return true;
		case R.id.savefigure:
			test1();
			return true;
		case R.id.example:
			ex_flag="example";
	            	write_path="/data/example/scilab/.open_file.cde";
			oe_path = "/data/local/linux/var/www/html/scilab/example/";
			mPath = new File(oe_path);
			loadFileList();
			return true;
		case R.id.help:

			Intent myIntent = new Intent(sci.this, scihelp.class);
			startActivityForResult(myIntent, 0);

			return true;
		case R.id.about:

			Intent myIntent1 = new Intent(sci.this, about.class);
			startActivityForResult(myIntent1, 0);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
}

// webchrome client for the web page feature
final class MyWebChromeClientsci extends WebChromeClient

{

	// IJavascriptHandler jb = new IJavascriptHandler();
	sci jb = new sci();

	@Override
	public void onConsoleMessage(String message, int lineNumber, String sourceID) {
		Log.d("MyApplication", message + " -- From line " + lineNumber + " of "
				+ sourceID);
		super.onConsoleMessage(message, lineNumber, sourceID);

	}

	@Override
	public boolean onJsAlert(WebView view, String url, String message,
			JsResult result) {

		Log.d("LogTag", message);

		if (message.charAt(0) == '0') {
			message = message.substring(1);

		} else {
			message = message.substring(1);

		}

		result.confirm();
		return true;
	}

}
