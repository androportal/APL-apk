package multiple.language.imagebuttons;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ImagebuttonsActivity extends Activity {

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
   	 
    
    }
    public void onclickhandler(View v) {

    	  switch(v.getId()){

    	  case R.id.image_c: /** Start a new Activity MyCards.java */
    	       Intent intent = new Intent(this, c.class);
    	       this.startActivity(intent);
    	       break;

    	  case R.id.image_cplus: /** AlerDialog when click on Exit */
    		  Intent intent1 = new Intent(this, Cplus.class);
   	       this.startActivity(intent1);
    	       
    	       break;
    	  case R.id.image_python: /** AlerDialog when click on Exit */
    		  Intent intent2 = new Intent(this, Python.class);
      	       this.startActivity(intent2);
       	       
   	       break;
    	 
    	  case R.id.image_scilab: /** AlerDialog when click on Exit */
    		  Intent intent3 = new Intent(this, Scilab.class);
      	       this.startActivity(intent3);
       	       
   	       break;
    	  }


    }}