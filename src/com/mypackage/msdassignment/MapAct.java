/* **********************************************************************
 * 
 * The map class provides the user with an interactive 
 * Google Map that they can use to view where they have been.
 * There are locations services so that the user can view where
 * they are in relation to these countries.
 * 
 * Author: Colm Mulhall
 * 
 * ********************************************************************/

/*package com.mypackage.msdassignment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.mypackage.travellog.R;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MapAct extends FragmentActivity //implements OnMapClickListener, OnMapLongClickListener
{
	 private GoogleMap myMap;
	 LocationManager myLocationManager = null;
	 Location myLocation;
	 OnLocationChangedListener myLocationListener = null;
	 Criteria myCriteria;
	 public String passedValue, countryName;
	 public int id;
	 
	 @Override
	 protected void onCreate(Bundle savedInstanceState) 
	 {
		 super.onCreate(savedInstanceState);
	     setContentView(R.layout.map_layout);
	  
		 //map stuff
	     FragmentManager myFragmentManager = getSupportFragmentManager();
	     SupportMapFragment mySupportMapFragment 
	         = (SupportMapFragment)myFragmentManager.findFragmentById(R.id.map);
	     myMap = mySupportMapFragment.getMap();
	
   	     myMap.setMyLocationEnabled(true);
	   	     
	   	 myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
	      
	     myCriteria = new Criteria();
	     myCriteria.setAccuracy(Criteria.ACCURACY_FINE);
	     myLocationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
	 }
			  
	 public void onLocationChanged(Location location) 
	 {
		 if (myLocationListener != null) 
		 {
			 myLocationListener.onLocationChanged(location);
	   
			 LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
			 myMap.animateCamera(CameraUpdateFactory.newLatLng(latlng)); 
		 }
	 }
	 
	 //action bar
	 @Override
	 public boolean onCreateOptionsMenu(Menu menu)
	 {
		 //Inflate the menu. This adds items to the action bar if it is present.
    	 MenuInflater inflater = getMenuInflater();
         inflater.inflate(R.menu.map_menu, menu);
         return true;
	 }
	    
	//action bar listener
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
	    {
		    case R.id.exit_action:
				finish();
				break;
			      
		    default:
		    	break;
		 }
	     return true;
	 }
	
	@Override
	protected void onPause()
	{
		super.onPause();
		finish();
	}
}*/