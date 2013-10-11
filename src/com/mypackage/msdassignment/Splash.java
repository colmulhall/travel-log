/* **********************************************************************
 * 
 * The splash activity is the first activity to start when 
 * the app opens. It displays the app logo for a few seconds 
 * and then moves onto the main screen.
 * 
 * Author: Colm Mulhall
 * 
 * *********************************************************************/

package com.mypackage.msdassignment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import com.mypackage.travellog.R;

/*The purpose of the "splash" class is to display the logo for my app
 * for a few seconds before the app begins.*/

public class Splash extends Activity
{
	/** Called when the activity is first created. */
    @Override
	 public void onCreate(Bundle savedInstanceState) 
	 {
		 super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); 
	     setContentView(R.layout.splash_layout);
	      
	     setProgressBarIndeterminateVisibility(true); 
	     
	     Thread timer = new Thread()
	     {
	    	 public void run()
	    	 {
	    		 try
	    		 {
	    			 sleep(2500);  //wait for 2.5 seconds
	    		 } 
	    		 catch(InterruptedException e)
	    		 {
	    			 e.printStackTrace();  //if error is found, send a message for debugging
	    		 } 
	    		 finally
	    		 {
	    			 Intent openMainScreen = new Intent(Splash.this, MainScreen.class);
	    			 startActivity(openMainScreen);
	    		 }
	    	 }
	     };
	     timer.start();
	 }

	 //kill the splash activity once the main activity starts
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		finish();
	}

	@Override
	protected void onPause() 
	{
		super.onPause();
		finish();
	}
}