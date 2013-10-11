/* **********************************************************************
 * 
 * The database manager class provides all of the functionality 
 * of the SQLite database. It can be opened, closed, updated and
 * queried. All queries in the app are here.
 * 
 * Author: Colm Mulhall
 * 
 * *********************************************************************/

package com.mypackage.msdassignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteStatement;

public class DBManager 
{
	public static final String KEY_ID = "_id";
	public static final String KEY_COUNTRY = "country";
	public static final String KEY_YEAR = "year";
	public static final String KEY_MONTH = "month";
	public static final String KEY_TRANSPORT = "transport";
	public static final String KEY_DESC = "description";
	
	public static final String DATABASE_NAME = "Countries database";
	public static final String DATABASE_TABLE = "Country";
	public static final int DATABASE_VERSION = 5;
	
	//create database table
	private static final String SCRIPT_CREATE_DATABASE =
			"create table " + DATABASE_TABLE + " ("
			+ KEY_ID + " integer primary key autoincrement, "
			+ KEY_COUNTRY + " text not null,"
			+ KEY_YEAR + " integer ,"
			+ KEY_MONTH + " text ,"
			+ KEY_TRANSPORT + " text ,"
			+ KEY_DESC + " text);";
	
	private Context context;
	private DBHelper DBHelper;
	private SQLiteDatabase db;

	public DBManager(Context ctx)
	{
		context = ctx;
	}
	
	//beginning of helper class **************************************************************************
	public class DBHelper extends SQLiteOpenHelper 
	{
		public DBHelper(Context context, String name, CursorFactory factory, int version)
		{
			super(context, name, factory, version);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			 db.execSQL(SCRIPT_CREATE_DATABASE);
		}
		
		//this will be called if I make a change to the database and give it a new version number
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
		{
			onCreate(db);
		}
	}
	//end of helper class ***********************************************************************************
	
	public DBManager openToRead() throws SQLException 
	{
		DBHelper = new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		db = DBHelper.getReadableDatabase();
		return this;
	}
	
	public DBManager openToWrite() throws SQLException 
	{
		DBHelper = new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		db = DBHelper.getWritableDatabase();
		return this;
	}
	
	//close the database
	public void close()
	{
		DBHelper.close();
	}
	
	//insert a new item into the database
	public long insert(String content, int year, String month, String transport, String description)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_COUNTRY, content);
		contentValues.put(KEY_YEAR, year);
		contentValues.put(KEY_MONTH, month);
		contentValues.put(KEY_TRANSPORT, transport);
		contentValues.put(KEY_DESC, description);
		
		return db.insert(DATABASE_TABLE, null, contentValues);
	}
	
	//edit an item in the database
	public boolean update(int id, String content, int year, String month, String transport, String description)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_COUNTRY, content);
		contentValues.put(KEY_YEAR, year);
		contentValues.put(KEY_MONTH, month);
		contentValues.put(KEY_TRANSPORT, transport);
		contentValues.put(KEY_DESC, description);
		
		return db.update(DATABASE_TABLE, contentValues, KEY_ID + " = " + id, null) > 0;
	}
	
	//delete everything from the database
	public int deleteAll()
	{
		return db.delete(DATABASE_TABLE, null, null);
	}
	
	//delete the database 
	public void deleteDatabase()
	{
		context.deleteDatabase(DATABASE_NAME);
	}
	
	//queue the items in the database
	public Cursor queueAll()
	{
		String[] columns = new String[]{
				KEY_ID, 
				KEY_COUNTRY,
				KEY_YEAR,
				KEY_MONTH,
				KEY_TRANSPORT,
				KEY_DESC};
		Cursor cursor = db.query(DATABASE_TABLE, columns,
		  null, null, null, null, null);
	
		return cursor;
	}
	
	//order the list by the year in descending order
	public Cursor orderList()
	{
		String[] columns = new String[]{
				KEY_ID, 
				KEY_COUNTRY,
				KEY_YEAR,
				KEY_MONTH,
				KEY_TRANSPORT,
				KEY_DESC};
		
		Cursor cursor = db.query(DATABASE_TABLE, columns, null, null, null, null, KEY_YEAR + " DESC");
		
		return cursor;
	}

	//getters for each item in the database *********************************************************
	public String getCountry(int num)
    {
		Cursor cursor = db.query(DATABASE_TABLE, new String[] {"country"}, 
				"_id like " + num, null, null, null, null);
		
		cursor.moveToFirst();
		return cursor.getString(0);
    }
	
	public String getYear(int num)
    {
		Cursor cursor = db.query(DATABASE_TABLE, new String[] {"year"}, 
				"_id like " + num, null, null, null, null);
		
		cursor.moveToFirst();
		return cursor.getString(0);
    }
	
	public String getMonth(int num)
    {
		Cursor cursor = db.query(DATABASE_TABLE, new String[] {"month"}, 
				"_id like " + num, null, null, null, null);
		
		cursor.moveToFirst();
		return cursor.getString(0);
    }
	
	public String getTransport(int num)
    {
		Cursor cursor = db.query(DATABASE_TABLE, new String[] {"transport"}, 
				"_id like " + num, null, null, null, null);
		
		cursor.moveToFirst();
		return cursor.getString(0);
    }
	
	public long getTimesBeen(int num, String country)
	{
		String sql = "SELECT COUNT(*) FROM " + DATABASE_TABLE + " WHERE country LIKE '%" + country + "%'" ;
	    SQLiteStatement statement = db.compileStatement(sql);
	    long count = statement.simpleQueryForLong();
	    
	    return count;
	}
	
	public String getDescription(int num)
    {
		Cursor cursor = db.query(DATABASE_TABLE, new String[] {"description"}, 
				"_id like " + num, null, null, null, null);
		
		cursor.moveToFirst();
		return cursor.getString(0);
    }
	//***************************************************************************************************
	
	//delete a particular country, based on its passed in ID
	public void deleteCountry(int num)
	{
		db.delete(DATABASE_TABLE, KEY_ID + " = " + num, null);
	}
	
	//return the years that the user has been to a country when clicked
	public String yearsHere(String thecountry)
	{
		Cursor cursor = db.query(DATABASE_TABLE, new String[] {"year"}, 
				"country like " + "'%" + thecountry + "%'", null, null, null, null);
		
		cursor.moveToFirst();
		
		//iterates through the country in question row by row, getting each year you have been there
		String years_there = "";
		do
		{
			years_there += "-" + cursor.getString(0) + "\n";
		}while(cursor.moveToNext());
		
		return years_there;
	}
	
	
	/*------------- The following methods are used to get stats for the user -------------*/
	public long CountryCount()
	{
		String sql = "SELECT COUNT(DISTINCT country) FROM " + DATABASE_TABLE;
		SQLiteStatement statement = db.compileStatement(sql);
		long count = statement.simpleQueryForLong();
		
		return count;
	}
	
	//counts the amount of trips you have been on based on the number of items in the database
	public long TripCount() 
	{
		String sql = "SELECT COUNT(*) FROM " + DATABASE_TABLE;
	    SQLiteStatement statement = db.compileStatement(sql);
	    long count = statement.simpleQueryForLong();
	    
	    return count;
	}
	
	//counts the amount of trips you have been on that you travelled by plane
	public long FlightCount() 
	{
		String sql = "SELECT COUNT(*) FROM " + DATABASE_TABLE + " WHERE " + KEY_TRANSPORT + " LIKE '%Plane%'";
		SQLiteStatement statement = db.compileStatement(sql);
		long count = statement.simpleQueryForLong();
		    
		return count;
	}
	
	//counts the amount of trips you have been on that you travelled by ferry
	public long FerryCount() 
	{
		String sql = "SELECT COUNT(*) FROM " + DATABASE_TABLE + " WHERE " + KEY_TRANSPORT + " LIKE '%Ferry%'";
		SQLiteStatement statement = db.compileStatement(sql);
		long count = statement.simpleQueryForLong();
			    
		return count;
	}
	
	//counts the amount of trips you have been on that you travelled by bus
	public long BusCount() 
	{
		String sql = "SELECT COUNT(*) FROM " + DATABASE_TABLE + " WHERE " + KEY_TRANSPORT + " LIKE '%Bus%'";
		SQLiteStatement statement = db.compileStatement(sql);
		long count = statement.simpleQueryForLong();
			    
		return count;
	}
	
	//counts the amount of trips you have been on that you travelled by car
	public long CarCount() 
	{
		String sql = "SELECT COUNT(*) FROM " + DATABASE_TABLE + " WHERE " + KEY_TRANSPORT + " LIKE '%Car%'";
		SQLiteStatement statement = db.compileStatement(sql);
		long count = statement.simpleQueryForLong();
			    
		return count;
	}
	
	//counts the amount of trips you have been on that you travelled by train
	public long TrainCount() 
	{
		String sql = "SELECT COUNT(*) FROM " + DATABASE_TABLE + " WHERE " + KEY_TRANSPORT + " LIKE '%Train%'";
		SQLiteStatement statement = db.compileStatement(sql);
		long count = statement.simpleQueryForLong();
			    
		return count;
	}
	
	//count the number of trips the user had in the teenies (2010-2019)
	public long teeniesCount() 
	{
		String sql = "SELECT COUNT(*) FROM " + DATABASE_TABLE + " WHERE " + KEY_YEAR + " BETWEEN 2010 AND 2019";
		SQLiteStatement statement = db.compileStatement(sql);
		long count = statement.simpleQueryForLong();
				    
		return count;
	}
		
	//count the number of trips the user had in the noughties (2000-2009)
	public long noughtiesCount() 
	{
		String sql = "SELECT COUNT(*) FROM " + DATABASE_TABLE + " WHERE " + KEY_YEAR + " BETWEEN 2000 AND 2009";
		SQLiteStatement statement = db.compileStatement(sql);
		long count = statement.simpleQueryForLong();
			    
		return count;
	}
	
	//count the number of trips the user had in the nineties (1990-1999)
	public long ninetiesCount() 
	{
		String sql = "SELECT COUNT(*) FROM " + DATABASE_TABLE + " WHERE " + KEY_YEAR + " BETWEEN 1990 AND 1999";
		SQLiteStatement statement = db.compileStatement(sql);
		long count = statement.simpleQueryForLong();
				    
		return count;
	}
	
	//count the number of trips the user had in the eighties (1980-1989)
	public long eightiesCount() 
	{
		String sql = "SELECT COUNT(*) FROM " + DATABASE_TABLE + " WHERE " + KEY_YEAR + " BETWEEN 1980 AND 1989";
		SQLiteStatement statement = db.compileStatement(sql);
		long count = statement.simpleQueryForLong();
					    
		return count;
	}
	
	//count the number of trips the user had in the seventies (1970-1979)
	public long seventiesCount() 
	{
		String sql = "SELECT COUNT(*) FROM " + DATABASE_TABLE + " WHERE " + KEY_YEAR + " BETWEEN 1970 AND 1979";
		SQLiteStatement statement = db.compileStatement(sql);
		long count = statement.simpleQueryForLong();
						    
		return count;
	}
	
	//count the number of trips the user had in the sixties (1960-1969)
	public long sixtiesCount() 
	{
		String sql = "SELECT COUNT(*) FROM " + DATABASE_TABLE + " WHERE " + KEY_YEAR + " BETWEEN 1960 AND 1969";
		SQLiteStatement statement = db.compileStatement(sql);
		long count = statement.simpleQueryForLong();
						    
		return count;
	}
}