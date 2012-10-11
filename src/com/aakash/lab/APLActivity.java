package com.aakash.lab;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import com.aakash.lab.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;


public class APLActivity extends Activity implements OnClickListener {

    final Context context = this;
    // Initialization parameters for image buttons
    ImageButton rd1, rd2, rd3, rd4;
    // parameters for text view
    TextView tv1, tv2;
    AlertDialog help_dialog;
    private ProgressDialog mProgressDialog, progressBar;
    String checkFlag;
    int help_option_menu_flag = 0;
    private int group1Id = 1;
    int Help = Menu.FIRST;
    String result;
    boolean fstab_flag = true;
    File checkImg;
    File checkImgextsd;
    File checkTar;
    int rebootFlag = 0;
    
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
        
        // copy 'aakash.sh and 'preinstall.sh to their respective paths'
        File path = new File("/data/data/com.aakash.lab/files/copyFilesFlag.txt");
        if(!path.exists()){
        	loadDataFromAsset();
        }
        
        help_popup();
    }

   
	private void loadDataFromAsset() {
        try {

            // get input stream for text

            InputStream is = getAssets().open("aakash.sh");
            InputStream is2 = getAssets().open("preinstall.sh");

            // check size

            int size = is.available();
            int size2 = is2.available();
            // create buffer for IO

            byte[] buffer = new byte[size];
            byte[] buffer2 = new byte[size2];
 
            // get data to buffer

            is.read(buffer);
            is2.read(buffer2);

            try {
                FileOutputStream output = openFileOutput("aakash.sh", Context.MODE_PRIVATE);
                File f = getFileStreamPath("aakash.sh");
                
                output.write(buffer);
                output.flush();
                output.close();
                is.close();
              
                OutputStream output2 = new FileOutputStream("/mnt/sdcard/preinstall.sh");
                output2.write(buffer2);
                output2.flush();
                output2.close();
                is2.close();
              
              
            } catch (Exception e) {
                e.printStackTrace();
            }
          
            String[] command = {"busybox mv /mnt/sdcard/preinstall.sh /system/bin/","chown root.root /system/bin/preinstall.sh","chmod 555 /system/bin/preinstall.sh"};
            RunAsRoot(command);
            FileOutputStream output = openFileOutput("copyFilesFlag.txt", Context.MODE_PRIVATE);
            output.flush();
            output.close();
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
          
        }catch (IOException e) {
	           e.printStackTrace();
	   }
    }
    
    
    private void help_popup() {
    	/**
         * checks existance of:
         * 1) /data/local/linux/etc/fstab
         * 2) /mnt/sdcard/apl.img
         * 3) /mnt/sdcard/apl.tar.gz
         * 4) /data/data/com.aakash.lab/files/help_flag.txt
         **/
        File fstab = new File("/data/local/linux/etc/fstab");
        checkImg = new File("/mnt/sdcard/apl.img");
        checkImgextsd = new File("/mnt/extsd/apl.img");
        checkTar = new File("/mnt/sdcard/apl.tar.gz");
        File help_flag = new File("/data/data/com.aakash.lab/files/help_flag.txt");
        
        if(!fstab.exists()){
        	                 
        	/**
        	 * check existance of an image in both '/mnt/sdcard' and '/mnt/extsd'
        	 **/
            if(!checkImg.exists() || !checkImgextsd.exists()){
            	
            	if(!checkTar.exists()){
            		
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                final View layout = inflater.inflate(R.layout.download_source,
                        (ViewGroup) findViewById(R.id.layout_root));
  
                // Building DatepPcker dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        APLActivity.this);
                builder.setView(layout);
                builder.setTitle("Notice");
                builder.setCancelable(false);
                Button btnNO = (Button) layout.findViewById(R.id.btnNo);
                btnNO.setOnClickListener(new OnClickListener() {
                  
                    public void onClick(View v) {
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
                        mProgressDialog.setCancelable(false);
                      
                        mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"Cancel",new DialogInterface.OnClickListener() {
                          
                            public void onClick(DialogInterface dialog, int which) {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(APLActivity.this);
                                builder.setMessage("Are you sure you want cancel Downloading?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.dismiss();
                                                        help_dialog.dismiss();
                                                        String[] command = {"rm /data/local/apl.img"};
                                                        RunAsRoot(command);  
                                                        finish();
                                                        android.os.Process
                                                                .killProcess(android.os.Process.myPid());
                                                    }
                                                })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                mProgressDialog.show();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                              
                            }
                        });
                        mProgressDialog.show();
                    }
  
                    private void startDownload() {
                    	if(isInternetOn()) {
                            // INTERNET IS AVAILABLE, DO STUFF..
                                Toast.makeText(context, "Connected To Network", Toast.LENGTH_SHORT).show();
                            }else{
                            // NO INTERNET AVAILABLE, DO STUFF..
                                Toast.makeText(context, "Network Disconnected", Toast.LENGTH_SHORT).show();
                                rebootFlag = 1;
                                AlertDialog.Builder builder = new AlertDialog.Builder(APLActivity.this);
                                builder.setMessage("No Connection Found, Please Check Your Network Settings!")
                                        .setCancelable(false)
                                        .setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        finish();
                                                        android.os.Process
                                                                .killProcess(android.os.Process.myPid());
                                                    }
                                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                              
                            }  
                    	/**
                    	 * for internal use only :P 
                    	 **/
                        String url = "http://10.102.152.27/installer/apl.tar.gz";
                        new DownloadFileAsync().execute(url);
                    }
                  
                    public final boolean isInternetOn() {
                        ConnectivityManager connec =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                        // ARE WE CONNECTED TO THE NET
                        NetworkInfo mwifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                        if ( connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                        connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                        connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
                        connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED ) {
                        // MESSAGE TO SCREEN FOR TESTING (IF REQ)
                        //Toast.makeText(this, connectionType + ” connected”, Toast.LENGTH_SHORT).show();
                        return true;
                        } else if ( connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||  connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED  ) {
                        //System.out.println(“Not Connected”);
                        return false;
                        }
                        return false;
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
            	else {
					Toast.makeText(context, "TAR EXISTS", Toast.LENGTH_SHORT).show();
					progressBar = new ProgressDialog(context);
			        progressBar.setCancelable(false);
			        progressBar.setMessage("Extracting files, please wait...");
			        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			        progressBar.show();
			
			        new Extract_TAR_GZ_FILE().execute();
            	}
            }else if((!help_flag.exists() || help_option_menu_flag == 1) && fstab.exists()){
            	/*
            	 * if /data/data/com.aakash.lab/files/help_flag.txt NOT exist OR (is user have checked 'do not
            	 * show me this dialog box(initial help page which pop's up at start up)') AND /data/local/linux
            	 * /etc/fstab exists  
            	 * 
            	 * then
            	 * 
            	 * start the application  
            	 * */
            	startApp();
            }else{
            	fstab_flag = false;
            	reboot();
            }	
        }else{
        	// if '/data/local/linux/etc/fstab' exist THEN start the application  
        	startApp();
        }
    }

    private void startApp() {
		/**
		 * start the application and show initial help pop-up
		 **/ 
    	
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
                        FileOutputStream output = openFileOutput("help_flag.txt", Context.MODE_PRIVATE);
                        output.flush();
                        output.close();
                       
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(("false").equals(checkFlag)){
                    String[] command = {"rm -r /data/data/com.aakash.lab/files/help_flag.txt"};
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
    	/**
    	 * download tar.gz from URL and write in '/mnt/sdcard'
    	 **/
        @Override
        public void onPreExecute() {
            super.onPreExecute();

        }

        public String doInBackground(String... aurl) {
            int count;

            try {
                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(
                        "/mnt/sdcard/apl.tar.gz");

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

        public void onProgressUpdate(String... progress) {

            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }
        public void onPostExecute(String unused) {
     
        mProgressDialog.dismiss();
        help_dialog.dismiss();
        if(checkTar.exists() && rebootFlag == 0){
	        /**
	         * if tar file exists THEN
	         * start file extraction spinner
	         **/
	        progressBar = new ProgressDialog(context);
	        progressBar.setCancelable(false);
	        progressBar.setMessage("Extracting files, please wait...");
	        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        progressBar.show();
	
	        new Extract_TAR_GZ_FILE().execute();
        }
    }
        
        
       
        //delete internal files during uninstallation
        public boolean deleteFile (String name){
            name = "aakash.sh";
            name = "help_flag";
            name = "copyFilesFlag.txt";
            return false;
           
        }
}
    
public class Extract_TAR_GZ_FILE extends AsyncTask<String, String, String>{
        /**
         * extract an image asynchronously to '/mnt/sdcard'
         **/
		@Override
        public void onPreExecute() {
            super.onPreExecute();

        }
		
        public InputStream getInputStream(String tarFileName) throws Exception{
        
          if(tarFileName.substring(tarFileName.lastIndexOf(".") + 1, 
        		  tarFileName.lastIndexOf(".") + 3).equalsIgnoreCase("gz")){
             System.out.println("Creating an GZIPInputStream for the file");
             return new GZIPInputStream(new FileInputStream(new File(tarFileName)));
        
          }else{
             System.out.println("Creating an InputStream for the file");
             return new FileInputStream(new File(tarFileName));
          }
       }
        
        public void untar(InputStream in, String untarDir) throws IOException {
            
          System.out.println("Reading TarInputStream... ");
          System.out.println(in);
          TarInputStream tin = new TarInputStream(in);
          System.out.println("Reading TarInputStream... 1");
          TarEntry tarEntry = tin.getNextEntry();
          if(new File(untarDir).exists()){
              while (tarEntry != null){
            	  
                 File destPath = new File(untarDir + File.separatorChar + tarEntry.getName());
                 System.out.println("Processing " + destPath.getAbsoluteFile());
                 if(!tarEntry.isDirectory()){
                	                 	 
                    FileOutputStream fout = new FileOutputStream(destPath);
                    tin.copyEntryContents(fout);
                    fout.close();
                 }else{
                    destPath.mkdir();
                 }
                 tarEntry = tin.getNextEntry();
              }
              tin.close();
          }else{
             System.out.println("That destination directory doesn't exist! " + untarDir);
          }
            
        }
        

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {         
                String strSourceFile = "/mnt/sdcard/apl.tar.gz";
                String strDest = "/mnt/sdcard/";
                InputStream in = getInputStream(strSourceFile);
                untar(in, strDest);
                    
            }catch(Exception e) {
            
                e.printStackTrace();      
                System.out.println(e.getMessage());
            }
			
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressBar.dismiss();
			if (checkImg.exists()){
			reboot();
			}else{
				AlertDialog.Builder builder = new AlertDialog.Builder(APLActivity.this);
		        builder.setMessage("Failed to download apl.img, exiting the application")
		                .setCancelable(false)
		                .setPositiveButton("Ok",
		                        new DialogInterface.OnClickListener() {
		                            public void onClick(DialogInterface dialog, int id) {
		                                finish();
		                                android.os.Process
		                                        .killProcess(android.os.Process.myPid());
		                            }
		                        });
		                
		                
		        AlertDialog alert = builder.create();
		        alert.show();
			}
		}
    }

	public void reboot() {
		// reboot the device
		AlertDialog.Builder builder = new AlertDialog.Builder(APLActivity.this);
		if (fstab_flag == true){
		builder.setMessage("To apply your changes, please reboot")
	            .setCancelable(false)
	            .setPositiveButton("OK",
	                    new DialogInterface.OnClickListener() {
	                        public void onClick(DialogInterface dialog, int id) {
	                            String[] rebootCommand = {"reboot"};
	                            RunAsRoot(rebootCommand);
	                        }
	                   
	                    });
		}else{
			builder.setMessage("Filesystem not mounted, device requires a reboot")
            .setCancelable(false)
            .setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String[] rebootCommand = {"reboot"};
                            RunAsRoot(rebootCommand);
                        }
                   
                    });
			
		}
	    AlertDialog alert1 = builder.create();
	    alert1.show();
} 
}