package com.aakash.lab;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.aakash.lab.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
//import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
//import android.widget.TableLayout;
//import android.widget.TableRow;

import android.widget.TextView;

public class APLActivity extends Activity implements OnClickListener {
	private static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	final Context context = this;
	// Initialization parameters for image buttons
	ImageButton rd1, rd2, rd3, rd4;
	// parameters for text view
	TextView tv1, tv2;
	AlertDialog help_dialog;
	private ProgressDialog mProgressDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		rd1 = (ImageButton) findViewById(R.id.imageButton1);
		rd2 = (ImageButton) findViewById(R.id.imageButton2);
		rd3 = (ImageButton) findViewById(R.id.imageButton3);
		rd4 = (ImageButton) findViewById(R.id.imageButton4);
		tv1 = (TextView) findViewById(R.id.textView1);
		tv2 = (TextView) findViewById(R.id.textView2);
		rd1.setOnClickListener(this);
		rd2.setOnClickListener(this);
		rd3.setOnClickListener(this);
		rd4.setOnClickListener(this);
		
		help_popup();
	}
	
	private void help_popup() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.help_popup, (ViewGroup) findViewById(R.id.layout_root));
		//Building DatepPcker dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(APLActivity.this);
		builder.setView(layout);
		builder.setTitle("How To Use");
		builder.setPositiveButton("OK",new  DialogInterface.OnClickListener(){

			public void onClick(DialogInterface dialog, int which) {
				File path = new File("/data/local/linux");
				if(!path.exists()){
					LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
					final View layout = inflater.inflate(R.layout.download_source, (ViewGroup) findViewById(R.id.layout_root));
					
					
					//Building DatepPcker dialog
					AlertDialog.Builder builder = new AlertDialog.Builder(APLActivity.this);
					builder.setView(layout);
					builder.setTitle("Notice");
					
					Button yes = (Button)layout.findViewById(R.id.btnyes);
					yes.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							 startDownload();
							 
							 mProgressDialog = new ProgressDialog(context);
								mProgressDialog.setMessage("Downloading file..");
								mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
								mProgressDialog.setCancelable(false);
								mProgressDialog.show();
						 
						}

						private void startDownload() {
							 String url = "http://10.102.152.27/tar/linux.tar.gz";
						        new DownloadFileAsync().execute(url);
						}
					});		
				
					builder.setPositiveButton("OK",new  DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int which) {
							
						}
					});
					help_dialog=builder.create();
					help_dialog.show();
					WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
					//customizing the width and location of the dialog on screen 
					lp.copyFrom(help_dialog.getWindow().getAttributes());
					lp.width = 500;
					help_dialog.getWindow().setAttributes(lp);
					
				}
					
			}
		});
		
		
		help_dialog=builder.create();
		help_dialog.show();
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		//customizing the width and location of the dialog on screen 
		lp.copyFrom(help_dialog.getWindow().getAttributes());
		lp.width = 700;
		help_dialog.getWindow().setAttributes(lp);
		
		
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
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
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
	
	
class DownloadFileAsync extends AsyncTask<String, String, String> {
   
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
	}

	@Override
	protected String doInBackground(String... aurl) {
		int count;

	try {

	URL url = new URL(aurl[0]);
	URLConnection conexion = url.openConnection();
	conexion.connect();

	int lenghtOfFile = conexion.getContentLength();
	

	InputStream input = new BufferedInputStream(url.openStream());
	OutputStream output = new FileOutputStream("/sdcard/linux.tar.gz");

	byte data[] = new byte[1024];

	long total = 0;

		while ((count = input.read(data)) != -1) {
			total += count;
			publishProgress(""+(int)((total*100)/lenghtOfFile));
			output.write(data, 0, count);
		}

		output.flush();
		output.close();
		input.close();
	} catch (Exception e) {}
	return null;

	}
	protected void onProgressUpdate(String... progress) {
		
		 mProgressDialog.setProgress(Integer.parseInt(progress[0]));
	}

	@Override
	protected void onPostExecute(String unused) {
		
		mProgressDialog.dismiss();
	}
}


/*public static void extract(String tgzFile, String outputDirectory)
	    throws Exception {

	// Create the Tar input stream.
	FileInputStream fin = new FileInputStream(tgzFile);
	GZIPInputStream gin = new GZIPInputStream(fin);
	TarInputStream tin = new TarInputStream(gin);

	// Create the destination directory.
	File outputDir = new File(outputDirectory);
	outputDir.mkdir();

	// Extract files.
	TarEntry tarEntry = tin.getNextEntry();
	while (tarEntry != null) {
	    File destPath = new File(outputDirectory + File.separator + tarEntry.getName());

	    if (tarEntry.isDirectory()) {
	        destPath.mkdirs();
	    } else {
	        // If the parent directory of a file doesn't exist, create it.
	        if (!destPath.getParentFile().exists())
	            destPath.getParentFile().mkdirs();

	        FileOutputStream fout = new FileOutputStream(destPath);
	        tin.copyEntryContents(fout);
	        fout.close();
	    // Presserve the last modified date of the tar'd files.
	        destPath.setLastModified(tarEntry.getModTime().getTime());
	    }
	    tarEntry = tin.getNextEntry();
	}
	tin.close();
	}
*/

public class Decompress { 
	  private String _zipFile; 
	  private String _location; 
	 
	  public Decompress(String zipFile, String location) { 
	    _zipFile = zipFile; 
	    _location = location; 
	 
	    _dirChecker(""); 
	  } 
	 
	  public void unzip() { 
	    try  { 
	      FileInputStream fin = new FileInputStream(_zipFile); 
	      ZipInputStream zin = new ZipInputStream(fin); 
	      ZipEntry ze = null; 
	      while ((ze = zin.getNextEntry()) != null) { 
	       // Log.v("Decompress", "Unzipping " + ze.getName()); 
	 
	        if(ze.isDirectory()) { 
	          _dirChecker(ze.getName()); 
	        } else { 
	          FileOutputStream fout = new FileOutputStream(_location + ze.getName()); 
	          for (int c = zin.read(); c != -1; c = zin.read()) { 
	            fout.write(c); 
	          } 
	 
	          zin.closeEntry(); 
	          fout.close(); 
	        } 
	         
	      } 
	      zin.close(); 
	    } catch(Exception e) { 
	      //Log.e("Decompress", "unzip", e); 
	    } 
	 
	  } 
	 
	  private void _dirChecker(String dir) { 
	    File f = new File(_location + dir); 
	 
	    if(!f.isDirectory()) { 
	      f.mkdirs(); 
	    } 
	  } 
	} 


}