/* **********************************************************************
 * 
 * This class is where the user can enter a new country into the database. 
 * It includes textviews, edittexts, spinners and buttons for data entry.
 * 
 * Author: Colm Mulhall
 * 
 * *********************************************************************/

package com.mypackage.msdassignment;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup;
import com.mypackage.travellog.R;

public class AddCountry extends Activity
{
	private DBManager db;
	private TextView enterCountry, selectYear, selectMonth, selectTransport, enterDescription;
	private EditText editDesc;
	private Spinner spinYear, spinMonth, spinTransport;
	private Button add, back;
	private AutoCompleteTextView editCountry;
	private int inputBgColour = R.color.black;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcountry_layout);
        
        //set up a custom font for textviews to use
        //REFERENCE: This font was downloaded from http://www.fontspace.com/category/android
        Typeface font = Typeface.createFromAsset(getAssets(), "Days.otf");  
        
        //Enter country
        enterCountry = (TextView)findViewById(R.id.enter_country);
        enterCountry.setTypeface(font);
        editCountry = (AutoCompleteTextView)findViewById(R.id.editcountry);  //autocomplete countries
        editCountry.setThreshold(1);
        editCountry.setBackgroundColor(inputBgColour);
        editCountry.setGravity(Gravity.CENTER_HORIZONTAL);  //center text
        //array with all of the countries adapter
        String[] countries = getResources().getStringArray(R.array.all_the_countries);
        ArrayAdapter<String> adapter = 
		         new ArrayAdapter<String>
        			(this,android.R.layout.simple_dropdown_item_1line, countries);
        editCountry.setAdapter(adapter);
        
        //Enter year
        selectYear = (TextView)findViewById(R.id.enter_year);
        selectYear.setTypeface(font);
        spinYear = (Spinner)findViewById(R.id.yearspin);
        spinYear.setBackgroundColor(inputBgColour);
        
        //list of years for the spinner. gets current year and goes back to 1960
        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i >= 1960; i--) 
        {
            years.add("" + i);
        }
        //custom adapter for the spinner. this centers the text in the spinner
        //REFERENCE: Found this solution on Stack Overflow
        ArrayAdapter<String> year_adapter = new ArrayAdapter<String>(this, R.layout.spinner_style, years) 
        {
            public View getView(int position, View convertView, ViewGroup parent) 
            {
                View v = super.getView(position, convertView, parent);

                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setTextSize(16);

                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                View v = super.getDropDownView(position, convertView,parent);

                ((TextView) v).setGravity(Gravity.CENTER);

                return v;
            }
        };
        spinYear.setAdapter(year_adapter);
        
        //Enter month
        selectMonth = (TextView)findViewById(R.id.enter_month);
        selectMonth.setTypeface(font);
        spinMonth = (Spinner)findViewById(R.id.monthspin);
        spinMonth.setBackgroundColor(inputBgColour);
        //custom adapter for the spinner. this centers the text in the spinner
        //REFERENCE: Found this solution on Stack Overflow
        String[] months = getResources().getStringArray(R.array.months);
        ArrayAdapter<String> month_adapter = new ArrayAdapter<String>(this, R.layout.spinner_style, months) 
        {
            public View getView(int position, View convertView, ViewGroup parent) 
            {
                View v = super.getView(position, convertView, parent);

                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setTextSize(16);

                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                View v = super.getDropDownView(position, convertView,parent);

                ((TextView) v).setGravity(Gravity.CENTER);

                return v;
            }
        };
        spinMonth.setAdapter(month_adapter);
        
        //Enter transport mode
        selectTransport = (TextView)findViewById(R.id.enter_transport);
        selectTransport.setTypeface(font);
        spinTransport = (Spinner)findViewById(R.id.transportspin);
        spinTransport.setBackgroundColor(inputBgColour);
        
        //custom adapter for the spinner. this centers the text in the spinner
        //REFERENCE: Found this solution on Stack Overflow
        String[] transport = getResources().getStringArray(R.array.transport);
        ArrayAdapter<String> transport_adapter = new ArrayAdapter<String>(this, R.layout.spinner_style, transport) 
        {
            public View getView(int position, View convertView, ViewGroup parent) 
            {
                View v = super.getView(position, convertView, parent);

                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setTextSize(16);

                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                View v = super.getDropDownView(position, convertView,parent);

                ((TextView) v).setGravity(Gravity.CENTER);

                return v;
            }
        };
        spinTransport.setAdapter(transport_adapter);
        
        //Enter description
        enterDescription = (TextView)findViewById(R.id.enter_desc);
        enterDescription.setTypeface(font);
        editDesc = (EditText)findViewById(R.id.editdesc);
        editDesc.setBackgroundColor(inputBgColour);
        editDesc.setGravity(Gravity.CENTER_HORIZONTAL);
        
        //Buttons
        add = (Button)findViewById(R.id.add);
        add.getBackground().setColorFilter(0xff5a573e, PorterDuff.Mode.MULTIPLY);  //color set for both buttons
        back = (Button)findViewById(R.id.backmain);
        back.getBackground().setColorFilter(0xff5a573e, PorterDuff.Mode.MULTIPLY);
        
        //Open database to write
        db = new DBManager(this);
        db.openToWrite();
        
        add.setOnClickListener(buttonAddOnClickListener);  //listener for add country button
        
        //handle switching back to main screen
        back.setOnClickListener(new View.OnClickListener() 
        {
			public void onClick(View v) 
			{
				db.close();
				
				finish();
			}
        });
    }
    
    //insert new country button
    Button.OnClickListener buttonAddOnClickListener = new Button.OnClickListener()
    {  
    	@Override
	    public void onClick(View arg0) 
    	{
		    String country = editCountry.getText().toString();
		    int year = Integer.parseInt(spinYear.getSelectedItem().toString());
		    String month = String.valueOf(spinMonth.getSelectedItem());
		    String transport = String.valueOf(spinTransport.getSelectedItem());
		    String desc = editDesc.getText().toString();
		
		    //check if the user has entered in a country name. if not do not allow them to continue
		    if(country.equals(""))
		    {
		    	Toast.makeText(getApplicationContext(), "You have not entered a country", Toast.LENGTH_LONG).show();
		    }
		    else
		    {
		    	//if everything is ok then enter the info into the database
		    	db.insert(country, year, month, transport, desc);
			    
			    Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_LONG).show();
			    
			    //reset edit fields and spinners after insert
			    editCountry.setText(null);
			    spinYear.setSelection(0, true);
			    spinMonth.setSelection(0, true);
			    spinTransport.setSelection(0, true);
			    editDesc.setText(null);
		    }
    	}
    };
    
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
	      /*case R.id.map_action:
				Intent intent = new Intent(AddCountry.this, MapAct.class);
				startActivity(intent);
				break;*/
				
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
		        
		        AlertDialog.Builder builder = new AlertDialog.Builder(AddCountry.this);
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
    	db.close();
    	finish();
    }
}