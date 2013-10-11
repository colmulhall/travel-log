/* **********************************************************************
 * 
 * This class gathers stats about what the user has entered by 
 * querying the database. These stats are displayed to the user.
 * 
 * Author: Colm Mulhall
 * 
 * *********************************************************************/

package com.mypackage.msdassignment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.mypackage.travellog.R;

public class Stats extends Activity
{
	private DBManager db;
	TextView header, stats;
	Button back, delete;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats_layout);
		
		header = (TextView)findViewById(R.id.custom_font);
		//set a custom font to the header textview
		//REFERENCE: This font was downloaded from http://www.fontspace.com/category/android
        Typeface header_font = Typeface.createFromAsset(getAssets(), "Days.otf");  
        header.setTypeface(header_font);
        
		stats = (TextView)findViewById(R.id.stat_list);
		stats.setMovementMethod(new ScrollingMovementMethod());  //allow the textview to be scrollable
		//set a custom font to the stats textview
		//REFERENCE: This font was downloaded from http://www.fontspace.com/category/android
        Typeface stat_font = Typeface.createFromAsset(getAssets(), "KTF-Roadbrush.ttf");  
        stats.setTypeface(stat_font);
        
        //buttons
		back = (Button)findViewById(R.id.back);
		back.getBackground().setColorFilter(0xff5a573e, PorterDuff.Mode.MULTIPLY);
		delete = (Button)findViewById(R.id.delete);
		delete.getBackground().setColorFilter(0xff5a573e, PorterDuff.Mode.MULTIPLY);
		
		//open database to read and begin selecting its information
		db = new DBManager(this);
		db.openToRead();
		
		//trip count stat
		long trip_count = db.TripCount();
		//this long is then converted to a string so it can be displayed in a textview
		String str = "Trips: " + String.valueOf(trip_count);
		
		//number of countries stat
		long country_count = db.CountryCount();
		String c = "Countries: " + String.valueOf(country_count);
		
		//flight stat
		long flight_count = db.FlightCount();
		String str2 = String.valueOf(flight_count) + " by plane";
		
		//ferry stat
		long ferry_count = db.FerryCount();
		String str3 = String.valueOf(ferry_count) + " by ferry";
		
		//bus stat
		long bus_count = db.BusCount();
		String str4 = String.valueOf(bus_count) + " by bus";
		
		//car stat
		long car_count = db.CarCount();
		String str5 = String.valueOf(car_count) + " by car";
		
		//train stat
		long train_count = db.TrainCount();
		String str6 = String.valueOf(train_count) + " by train";
		
		//number of trips in the teenies stat
		long teenies_count = db.teeniesCount();
		String str7 = String.valueOf(teenies_count) + " in the 2010s";
				
		//number of trips in the noughties stat
		long noughties_count = db.noughtiesCount();
		String str8 = String.valueOf(noughties_count) + " in the 2000s";
		
		//number of trips in the nineties stat
		long nineties_count = db.ninetiesCount();
		String str9 = String.valueOf(nineties_count) + " in the 1990s";
		
		//number of trips in the eighties stat
		long eighties_count = db.eightiesCount();
		String str10 = String.valueOf(eighties_count) + " in the 1980s";
		
		//number of trips in the seventies stat
 		long seventies_count = db.seventiesCount();
		String str11 = String.valueOf(seventies_count) + " in the 1970s";
		
		//number of trips in the nineties stat
		long sixties_count = db.sixtiesCount();
		String str12 = String.valueOf(sixties_count) + " in the 1960s";
		
		//percentage of the world seen
		long world_percent = db.CountryCount();
		world_percent = world_percent * 100;
		world_percent = world_percent / 196;
		String str13 = String.valueOf("Been to " + world_percent + "% of the worlds countries");
		
		//put all stats into a scrollable textview
		stats.setText(str + "\n\n" 
						+ c + "\n\n"
						+ str2 + "\n\n" 
						+ str3 + "\n\n" 
						+ str4 + "\n\n" 
						+ str5 + "\n\n" 
						+ str6 + "\n\n" 
						+ str7 + "\n\n"
						+ str8 + "\n\n"
						+ str9 + "\n\n"
						+ str10 + "\n\n"
						+ str11 + "\n\n"
						+ str12 + "\n\n"
						+ str13 + "\n\n");
						
		//handle switching back to main screen
        back.setOnClickListener(new View.OnClickListener()
        {
			public void onClick(View v) 
			{
				db.close();
				
				finish();
			}
        });
        
        //handle deleting the users list
        delete.setOnClickListener(new View.OnClickListener() 
        {
        	public void onClick(View arg0) 
        	{
        		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() 
		        {
					//"Are you sure?" dialog options
		            @Override
		            public void onClick(DialogInterface dialog, int which) 
		            {
		                switch(which)
		                {
			                case DialogInterface.BUTTON_POSITIVE:
			                    //Yes button clicked
			                	Toast.makeText(getApplicationContext(), "Your list has been deleted", Toast.LENGTH_LONG).show();
			        		    db.deleteDatabase();  //database deleted so that row IDs are reset to 0
			    				finish();
			                    break;
			
			                case DialogInterface.BUTTON_NEGATIVE:
			                    //No button clicked
			                    break;
		                }
		            }
		        };

		        AlertDialog.Builder builder = new AlertDialog.Builder(Stats.this);
		        builder.setTitle("Delete All Places");
		        builder.setMessage("Are you sure? This will delete your entire list.")
		        .setPositiveButton("Yes", dialogClickListener)
		        .setNegativeButton("No", dialogClickListener)
		        .show();
        	}
        });
	}
	
	//action bar
    @Override
	 public boolean onCreateOptionsMenu(Menu menu) 
	 {
		  // Inflate the menu; this adds items to the action bar if it is present.
		  getMenuInflater().inflate(R.menu.menu, menu);
		  return true;
	 }
    
    //action bar listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	switch (item.getItemId()) 
        {
	      case R.id.map_action:
				Intent intent = new Intent(Stats.this, MapAct.class);
				startActivity(intent);
				break;
				
	      case R.id.about_action:
	        	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() 
		        {
		            @Override
		            public void onClick(DialogInterface dialog, int which) 
		            {
		                switch(which)
		                {
			                case DialogInterface.BUTTON_POSITIVE:
			                    //OK button clicked. exits dialog
			                    break;
			                    
			                case DialogInterface.BUTTON_NEUTRAL:
			                	//Feedback button clicked. option to send email to developer
			                	final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			                	emailIntent.setType("plain/text");
			                	emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"colmmul92@yahoo.co.uk"});
			                	emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Travel Log Feedback");
			                	
			                	//get information about the device in which feedback is coming from
			                	String Device = Build.MANUFACTURER;
			                	String Model = Build.MODEL;
			                	@SuppressWarnings("deprecation")
								String APIlevel = android.os.Build.VERSION.SDK;
			                	emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, 
			                			"Device: " + Device + 
			                			"\n" + "Model: " + Model + 
			                			"\n" + "API Level: " + APIlevel + "\n\n");
			                	startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
		                }
		            }
		        };
		        
		        AlertDialog.Builder builder = new AlertDialog.Builder(Stats.this);
		        builder.setTitle("Travel Log");
		        builder.setMessage("Travel Log provides an easy way of logging countries that you have been to."
		        		+ "\n\n" + "Author: Colm Mulhall" + "\n"
		        				 +  
		        		"Version: 1.2")
		        .setPositiveButton("OK", dialogClickListener)
		        .setNeutralButton("Feedback", dialogClickListener)
		        .show();
	        	break;
	
	      default:
	    	  	break;
        }
        return true;
    }
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		db.close();
		finish();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		finish();
	}
}