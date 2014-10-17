package com.example.balancemgr;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.balancemgr.*;

import java.util.Locale;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v13.app.FragmentPagerAdapter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    
    static MainActivity thisActivity;
    static Context thisContext;
    
    static MainActivity getThisActivity(){
    	return thisActivity;
    }
    
    static Context getContext(){
    	return thisContext;
    }
    
    
    private String tblName = "out_calls";

    public void getCallLogs(View view){
    	Context context = getApplicationContext();
    	//Log.w("rakesh", getCallDetails(context));
    	
    	//addCallToDB("123456", "2014-10-15 12:00:00", 2000);
    	//getCallDetails(context);
    	//printCallFromDB();
    	//getDatabase();
    	//printTopCallers();
    	//getMyPhoneNum();
    	
    	Log.w("rakesh", "please wait...");
    	showText("Please wait...");
    	new NetTask().execute((Void)null);	
    }
    
    public void showText(final String str){
    	this.runOnUiThread(new Runnable() {
    	    public void run() {
    			TextView txt = (TextView) findViewById(R.id.txtHelloWorld);
    			txt.setText(str);
    		}
    	});
    }

    public String getMyPhoneNum(){
    	return "8951897798";
    }
    
    private void printTopCallers(){
    	SQLiteDatabase myDB = getDatabase();
    	String Data = "";
    	
		/*retrieve data from database */
		Cursor c = myDB.rawQuery("SELECT ph_num, count(*) as num_count FROM " + tblName 
				+ " GROUP BY ph_num ORDER BY num_count DESC LIMIT 5", null);

		int Column1 = c.getColumnIndex("ph_num");
		int Column2 = c.getColumnIndex("num_count");

		// Check if our result was valid.
		c.moveToFirst();
		if (c != null) {
			// Loop through all Results
			do {
				String ph = c.getString(Column1);
				String count = c.getString(Column2);
				Data =Data +ph+"/"+ count + "\n";
			}while(c.moveToNext());
		}
		Log.w("rakesh", Data);
    }
    
    public String getTopCaller(){
    	SQLiteDatabase myDB = getDatabase();
    	
		/*retrieve data from database */
		Cursor c = myDB.rawQuery("SELECT ph_num, count(*) as num_count FROM " + tblName 
				+ " GROUP BY ph_num ORDER BY num_count DESC LIMIT 1", null);

		int Column1 = c.getColumnIndex("ph_num");

		// Check if our result was valid.
		c.moveToFirst();
		{
			// Loop through all Results
			do {
				String ph = c.getString(Column1);
				return ph;
			}while(c.moveToNext());
		}
    }
    
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
          sb.append((char) cp);
        }
        return sb.toString();
      }

      public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
          BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
          String jsonText = readAll(rd);
          JSONObject json = new JSONObject(jsonText);
          return json;
        } finally {
          is.close();
        }
      }
    
      public static JSONArray readJsonArrayFromUrl(String url) throws IOException, JSONException {
          InputStream is = new URL(url).openStream();
          try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONArray json = new JSONArray(jsonText);
            return json;
          } finally {
            is.close();
          }
        }
      
    private String getCallDetails(Context context) {
        StringBuffer stringBuffer = new StringBuffer();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, CallLog.Calls.DATE + " DESC");
        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);       
        while (cursor.moveToNext()) {
            String phNumber = cursor.getString(number);
            String callType = cursor.getString(type);
            String callDate = cursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = cursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
            case CallLog.Calls.OUTGOING_TYPE:
                dir = "OUTGOING";
                addCallToDB(phNumber, dateFormat.format(callDayTime), Integer.parseInt(callDuration));
                break;
            case CallLog.Calls.INCOMING_TYPE:
                dir = "INCOMING";
                break;

            case CallLog.Calls.MISSED_TYPE:
                dir = "MISSED";
                break;
            }
            stringBuffer.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- "
                    + dir + " \nCall Date:--- " + callDayTime
                    + " \nCall duration in sec :--- " + callDuration);
            stringBuffer.append("\n----------------------------------");
        }
        cursor.close();
        return stringBuffer.toString();
    }
    
    private void addCallToDB(String phNumber, String time, int duration){
    	String tblName = "out_calls";
    	SQLiteDatabase db = getDatabase();
    	
    	db.execSQL("INSERT INTO " + tblName +
    			"(ph_num, call_time, call_dur)"
    			+ "VALUES ('" + phNumber + "', '" + time + "', '" + duration + "');" );
    	
    	db.close();
    }
    
    private void printCallFromDB(){
    	String tblName = "out_calls";
    	SQLiteDatabase myDB = getDatabase();
    	String Data = "";
    	
		/*retrieve data from database */
		Cursor c = myDB.rawQuery("SELECT * FROM " + tblName , null);

		int Column1 = c.getColumnIndex("ph_num");
		int Column2 = c.getColumnIndex("call_time");
		int Column3 = c.getColumnIndex("call_dur");

		// Check if our result was valid.
		c.moveToFirst();
		if (c != null) {
			// Loop through all Results
			do {
				String ph = c.getString(Column1);
				String time = c.getString(Column2);
				int dur = c.getInt(Column3);
				Data =Data +ph+"/"+ time + "/" + dur + "\n";
			}while(c.moveToNext());
		}
		Log.w("rakesh", Data);
    }
    
    private SQLiteDatabase getDatabase(){
    	SQLiteDatabase myDB= null;
    	String TableName = "out_calls";

    	String Data="";

    	/* Create a Database. */
    	try {
    		myDB = this.openOrCreateDatabase("balanceMgr", MODE_PRIVATE, null);

    		/* Create a Table in the Database. */
    		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
    				+ TableName
    				+ " (Field1 VARCHAR, Field2 INT(3));");
    		
//    		myDB.execSQL("DROP TABLE IF EXISTS "
//    				+ "out_calls");
    		/* Create a Table in the Database. */
    		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
    				+ "out_calls"
    				+ " (ph_num VARCHAR, call_time DATETIME, call_dur INT(8));");
    		return myDB;
    		
//    		/* Insert data to a Table */
//    		myDB.execSQL("INSERT INTO "
//    				+ TableName
//    				+ " (Field1, Field2)"
//    				+ " VALUES ('Saranga', 22);");
//    		
//    		/* Insert data to a Table*/
//    		myDB.execSQL("INSERT INTO "
//    				+ TableName
//    				+ " (Field1, Field2)"
//    				+ " VALUES ('Tushar', 24);");
//
//    		/*retrieve data from database */
//    		Cursor c = myDB.rawQuery("SELECT * FROM " + TableName , null);
//
//    		int Column1 = c.getColumnIndex("Field1");
//    		int Column2 = c.getColumnIndex("Field2");
//
//    		// Check if our result was valid.
//    		c.moveToFirst();
//    		if (c != null) {
//    			// Loop through all Results
//    			do {
//    				String Name = c.getString(Column1);
//    				int Age = c.getInt(Column2);
//    				Data =Data +Name+"/"+Age+"\n";
//    			}while(c.moveToNext());
//    		}
//    		Log.w("rakesh", Data);
//    		TextView tv = new TextView(this);
//    		tv.setText(Data);
//    		setContentView(tv);
    		
    	}
    	catch(Exception e) {
    		Log.e("Error", "Error", e);
    	} finally {
    		if (myDB != null)
    			return myDB;
    	}
		return myDB;
    }

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        thisActivity = this;
        thisContext = getApplicationContext();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
