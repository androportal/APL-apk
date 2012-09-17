package com.aakash.lab;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import com.aakash.lab.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;



public class APLActivity extends Activity implements OnClickListener {

	final Context context = this;
	// Initialization parameters for image buttons
	ImageButton rd1, rd2, rd3, rd4;
	// parameters for text view
	TextView tv1, tv2;
	AlertDialog help_dialog;
	private ProgressDialog mProgressDialog;
	String checkFlag;
	int help_option_menu_flag = 0;
	private int group1Id = 1;
	int Help = Menu.FIRST;
 
	//adding options to the options menu
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(group1Id, Help, Help, "Help");
    return super.onCreateOptionsMenu(menu); 
    }

	//code for the actions to be performed on clicking options menu goes here ...
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case 1:
	    	help_option_menu_flag = 1;
	    	help_popup(); 
	    }
	    return super.onOptionsItemSelected(item);
	}
			
			
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
        loadDataFromAsset();
        
	}

	
	private void loadDataFromAsset() {
		try {

            // get input stream for text

            InputStream is = getAssets().open("aakash.sh");

            // check size

            int size = is.available();
            // create buffer for IO

            byte[] buffer = new byte[size];

            // get data to buffer

            is.read(buffer);

            try {
            	OutputStream output = new FileOutputStream("/mnt/sdcard/aakash.sh");
                output.write(buffer);
                output.flush();
                output.close();
                is.close();
                
                
			} catch (Exception e) {
				e.printStackTrace();
			}
            
            String[] command = {"busybox mv /mnt/sdcard/aakash.sh /data/local/"};
            RunAsRoot(command);
        }

        catch (IOException ex) {

            return;
        }

	}


	private void RunAsRoot(String[] command2) {
		// TODO Auto-generated method stub
		try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            for (String tmpmd : command2){
                    os.writeBytes(tmpmd +"\n" );
            }                
            os.writeBytes("exit\n");
            os.flush();
            
   } catch (IOException e) {
           e.printStackTrace();
   }
	}


	private void help_popup() {
	
		File path = new File("/data/local/linux");
		File paths = new File("/data/local/linux");
		File checkFileLinux = new File("/mnt/sdcard/apl.img");
		File help_flag = new File("/mnt/sdcard/.help_flag.txt");
		
		if (!path.exists() || paths.exists()) {
			
			if(!checkFileLinux.exists()){
				
				LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				final View layout = inflater.inflate(R.layout.download_source,
						(ViewGroup) findViewById(R.id.layout_root));
	
				// Building DatepPcker dialog
				AlertDialog.Builder builder = new AlertDialog.Builder(
						APLActivity.this);
				builder.setView(layout);
				builder.setTitle("Notice");
	
				Button btnNO = (Button) layout.findViewById(R.id.btnNo);
				btnNO.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						// TODO Auto-generated method stub
						finish();
						android.os.Process
								.killProcess(android.os.Process.myPid());
					}
				});
				
				Button btnyes = (Button) layout.findViewById(R.id.btnyes);
				btnyes.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						startDownload();
	
						mProgressDialog = new ProgressDialog(context);
						mProgressDialog.setMessage("Downloading file..");
						mProgressDialog
								.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
						mProgressDialog.setCancelable(true);
						
						mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"Cancel",new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								help_dialog.dismiss();
								String[] command = {"rm /mnt/sdcard/apl.img"};
						        RunAsRoot1(command);    
							}

							private void RunAsRoot1(String[] command2) {
								// TODO Auto-generated method stub
								try {
					                Process process = Runtime.getRuntime().exec("su");
					                DataOutputStream os = new DataOutputStream(process.getOutputStream());
					                for (String tmpmd : command2){
					                        os.writeBytes(tmpmd +"\n" );
					                }                
					                os.writeBytes("exit\n");
					                os.flush();
					                
					       } catch (IOException e) {
					               e.printStackTrace();
					       }
							}
						});
						mProgressDialog.show();
					}
	
					private void startDownload() {
						// String url = "http://10.102.152.27/tar/linux.tar.gz";
	
						String url = "http://10.102.152.27/tar/apl.img";
						new DownloadFileAsync().execute(url);
					}
				});
				help_dialog = builder.create();
				help_dialog.show();
				WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
				// customizing the width and location of the dialog on screen
				lp.copyFrom(help_dialog.getWindow().getAttributes());
				lp.width = 500;
				help_dialog.getWindow().setAttributes(lp);
			}
			else if(!help_flag.exists() || help_option_menu_flag == 1){
				
				LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				final View layout = inflater.inflate(R.layout.help_popup,
						(ViewGroup) findViewById(R.id.layout_root));
	
				// Building DatepPcker dialog
				AlertDialog.Builder builder = new AlertDialog.Builder(
						APLActivity.this);
				builder.setView(layout);
				builder.setTitle("Help");
				
				CheckBox cbHelp = (CheckBox)layout.findViewById(R.id.cbHelp);
				
				if(help_option_menu_flag == 1){
					cbHelp.setChecked(true);
				}
				
				cbHelp.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
				
				//for setting the visibility of EditText:'etProject' depending upon the condition
					if (((CheckBox) v).isChecked()) {
						checkFlag = "true";
					}
					else {
						checkFlag = "false";
					}
				}
				});
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if(("true").equals(checkFlag)){
							try {
								FileWriter ryt=new FileWriter("/mnt/sdcard/.help_flag.txt");
								BufferedWriter out=new BufferedWriter(ryt);
								out.write("help dialog flag");
								out.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						if(("false").equals(checkFlag)){
							String[] command = {"rm -r /mnt/sdcard/.help_flag.txt"};
					        RunAsRoot(command);  
						}
					}
					
				});
				
				help_dialog = builder.create();
				help_dialog.show();
				WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
				// customizing the width and location of the dialog on screen
				lp.copyFrom(help_dialog.getWindow().getAttributes());
				lp.width = 700;
				help_dialog.getWindow().setAttributes(lp);
			}
		}

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
								android.os.Process
										.killProcess(android.os.Process.myPid());
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

		protected String doInBackground(String... aurl) {
			int count;

			try {

				URL url = new URL(aurl[0]);
				URLConnection conexion = url.openConnection();
				conexion.connect();

				int lenghtOfFile = conexion.getContentLength();

				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(
						"/mnt/sdcard/apl.img");

				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));
					output.write(data, 0, count);
				}

				output.flush();
				output.close();
				input.close();
			} catch (Exception e) {
			}
			return null;

		}

		protected void onProgressUpdate(String... progress) {

			mProgressDialog.setProgress(Integer.parseInt(progress[0]));
		}

		protected void onPostExecute(String unused) {
       
        mProgressDialog.dismiss();
        help_dialog.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(APLActivity.this);
		builder.setMessage("To Apply Changes Please Reboot Your System")
				.setCancelable(false)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								String[] rebootCommand = {"reboot"};
						        RunAsRoot(rebootCommand);
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
    }

		
}
}