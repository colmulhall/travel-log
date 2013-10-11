/* **********************************************************************
 * 
 * This class provides a way of displaying information about
 * a country once the user clicks on one from the list. 
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.mypackage.travellog.R;

public class CountryInfo extends Activity
{
	private DBManager db;
	private String passedValue;
	private TextView c_name, c_month_year, c_times_here, c_desc;
	private Button back, delete;
	public int id;
	public final static String ID_EXTRA = "com.mypackage.msdassignment._ID";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.countryinfo_layout);
		
		//intent passed data
		passedValue = getIntent().getStringExtra(MainScreen.ID_EXTRA);
		id = Integer.parseInt(passedValue);
		
		c_name = (TextView)findViewById(R.id.country);
		c_month_year = (TextView)findViewById(R.id.cmonthyear);
		c_times_here = (TextView)findViewById(R.id.ctimesbeen);
		c_desc = (TextView)findViewById(R.id.cdesc);
		c_desc.setMovementMethod(new ScrollingMovementMethod());
		
		//set a custom font to the textviews
        Typeface font = Typeface.createFromAsset(getAssets(), "Corbert-Regular.otf");  
        c_name.setTypeface(font);
        c_month_year.setTypeface(font);
        c_times_here.setTypeface(font);
        c_desc.setTypeface(font);
        
		back = (Button)findViewById(R.id.backmain);
		back.getBackground().setColorFilter(0xff5a573e, PorterDuff.Mode.MULTIPLY); //change color
		delete = (Button)findViewById(R.id.deletecountry);
		delete.getBackground().setColorFilter(0xff5a573e, PorterDuff.Mode.MULTIPLY);  //change color
		
		//open database to read and begin selecting its information
		db = new DBManager(this);
		db.openToRead();
		
		//set the text of each TextView to the information in the database
		c_name.setText(db.getCountry(id));
		c_month_year.setText(db.getMonth(id) + ", " + db.getYear(id) + "\n");
		
		if(db.getTimesBeen(id, db.getCountry(id)) > 1)
			c_times_here.setText("Been here " + db.getTimesBeen(id, db.getCountry(id)) + " times");
		else
			c_times_here.setText("Been here once");
		
		c_desc.setText(db.getDescription(id));
		
		//handle switching back to main screen
        back.setOnClickListener(new View.OnClickListener() 
        {
			public void onClick(View v) 
			{
				db.close();
				
				overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
				finish();
			}
        });
        
        //handle deleting the item
        delete.setOnClickListener(new View.OnClickListener() 
        {
			public void onClick(View v) 
			{
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() 
		        {
					//"Are you sure?" dialog options
		            @Override
		            public void onClick(DialogInterface dialog, int which) 
		            {
		                switch (which)
		                {
			                case DialogInterface.BUTTON_POSITIVE:
			                    //Yes button clicked
			                	Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
			    				db.deleteCountry(id);
			    				finish();
			                    break;
			
			                case DialogInterface.BUTTON_NEGATIVE:
			                    //No button clicked
			                    break;
		                }
		            }
		        };

		        AlertDialog.Builder builder = new AlertDialog.Builder(CountryInfo.this);
		        builder.setTitle("Delete");
		        builder.setMessage("Are you sure?")
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
    	//Inflate the menu. This adds items to the action bar if it is present.
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.country_menu, menu);
        return true;
	 }
    
    //action bar listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	switch (item.getItemId()) 
        {
    	  case R.id.edit_action:
    		    Intent i = new Intent(CountryInfo.this, EditCountry.class);
    		    i.putExtra("Country ID", id);
    		    startActivity(i);
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
		        
		        AlertDialog.Builder builder = new AlertDialog.Builder(CountryInfo.this);
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


    public void click(View v) 
    {
        switch(v.getId()) 
        {
        	case R.id.ctimesbeen:
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
		                }
		            }
		        };
		        
		        AlertDialog.Builder builder = new AlertDialog.Builder(CountryInfo.this);
		        builder.setTitle("You were here in..." + "\n")
		        .setMessage(db.yearsHere(db.getCountry(id)))
		        .setPositiveButton("OK", dialogClickListener)
		        .show();
	        	break;
	
	      default:
	    	  	break;
        }
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
		db.close();
		finish();
	}
}