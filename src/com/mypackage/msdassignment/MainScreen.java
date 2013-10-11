/* **********************************************************************
 * 
 * The main class of the app is where all of the countries that 
 * the user has entered are displayed in a list ordered by the year 
 * that they travelled there. 
 * 
 * Author: Colm Mulhall
 * 
 * *********************************************************************/

package com.mypackage.msdassignment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.graphics.PorterDuff;
import com.mypackage.travellog.R;

public class MainScreen extends Activity implements AdapterView.OnItemClickListener
{
	private DBManager db;
	private Cursor cursor;
	private TextView header;
	private Button goEdit, goStats;
	private ListView listContent;
	public final static String ID_EXTRA = "com.mypackage.msdassignment._ID";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen_layout);
        
        listContent = (ListView)findViewById(R.id.list);
        listContent.setDrawingCacheBackgroundColor(getResources().getColor(R.color.dark_blue));
        goEdit = (Button)findViewById(R.id.goedit);
        header = (TextView) findViewById(R.id.header_text); 
        
        //set a custom font to the textview
        //REFERENCE: This font was downloaded from http://www.fontspace.com/category/android
        Typeface font = Typeface.createFromAsset(getAssets(), "Days.otf");  
        header.setTypeface(font);
    
        //Open database to read
        db = new DBManager(this);
        db.openToRead();
        
        cursor = db.orderList();
        
        String[] from = new String[]{
        		DBManager.KEY_ID, 
        		DBManager.KEY_COUNTRY, 
        		DBManager.KEY_YEAR,
        		DBManager.KEY_MONTH,
        		DBManager.KEY_TRANSPORT,
        		DBManager.KEY_DESC};
        int[] to = new int[]{R.id.editcountry, R.id.countrytext};

        @SuppressWarnings("deprecation")
		SimpleCursorAdapter cursorAdapter =  new SimpleCursorAdapter(this, R.layout.row, cursor, from, to);
        
        listContent.setAdapter(cursorAdapter);
        listContent.setOnItemClickListener(this);

        //go to add country screen
        goEdit = (Button)findViewById(R.id.goedit);
        goEdit.getBackground().setColorFilter(0xff5a573e, PorterDuff.Mode.MULTIPLY);
        goEdit.setOnClickListener(new View.OnClickListener() 
        {
			public void onClick(View v) 
			{
				Intent intent = new Intent(MainScreen.this, AddCountry.class);
				startActivity(intent);
			}
        });
        
        //go to stats screen
        goStats = (Button)findViewById(R.id.gostats);
        goStats.getBackground().setColorFilter(0xff5a573e, PorterDuff.Mode.MULTIPLY);  //set colour of button
        goStats.setOnClickListener(new View.OnClickListener() 
        {
			public void onClick(View v) 
			{
				Intent intent = new Intent(MainScreen.this, Stats.class);
				startActivity(intent);
			}
        });
    }
    
    
    
    //action bar
    @Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		//Inflate the menu. This adds items to the action bar if it is present.
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
	}
    
    //action bar listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	switch (item.getItemId()) 
        {
	      case R.id.map_action:
				Intent intent = new Intent(MainScreen.this, MapAct.class);
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
			                			"\n\n\n\n\nDevice: " + Device + 
			                			"\n" + "Model: " + Model + 
			                			"\n" + "API Level: " + APIlevel + "\n\n");
			                	startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
		                }
		            }
		        };
		        
		        AlertDialog.Builder builder = new AlertDialog.Builder(MainScreen.this);
		        builder.setTitle("Travel Log");
		        builder.setMessage("Travel Log provides an easy way of logging countries that you have been to."
		        		+ "\n\n" + "Author: Colm Mulhall" + "\n"
		        				 +  
		        		"Version: 1.2")
		        .setPositiveButton("OK", dialogClickListener)
		        .setNeutralButton("Feedback", dialogClickListener)
		        .show();
	
	      default:
	    	  	break;
        }
        return true;
    }
    
    //handle click on a list item
    @Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
    {
    	Intent i = new Intent(MainScreen.this, CountryInfo.class);
    	i.putExtra(ID_EXTRA, String.valueOf(id));  //pass the id of the selected item with the intent
    	startActivity(i);
    	overridePendingTransition(R.anim.slide_in, R.anim.slide_out);  //animation
	}
    
    //life cycles
    @Override
    protected void onPause()
    {
	    super.onPause();
	    db.close();
    }
    
    @Override
    protected void onDestroy()
    {
    	super.onDestroy();
    	db.close();
    	finish();
    }
    
	@Override
	protected void onResume()
	{
		super.onResume();
		this.onCreate(null); //refresh the activity once it resumes 
	}
}