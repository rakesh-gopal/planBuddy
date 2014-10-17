package com.example.balancemgr;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;


public class SMSManager extends Activity implements OnClickListener {
	
	public ArrayList<String> getInboxSMS() {
		
		// Create Inbox box URI
		Uri inboxURI = Uri.parse("content://sms/inbox");
		 
		// List required columns
		String[] reqCols = new String[] { "_id", "address", "body" };
		 
		// Get Content Resolver object, which will deal with Content Provider
		ContentResolver cr = getContentResolver();
		 
		// Fetch Inbox SMS Message from Built-in Content Provider
		Cursor cursor = cr.query(inboxURI, reqCols, null, null, null);
		
		while (cursor.moveToNext()) {
            String phNumber = cursor.getString(0);
            /*
            String callType = cursor.getString(type);
            String callDate = cursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = cursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            
            Log.w("Praveen", " Phone Number: " + phNumber + ", CallType" + callType + 
            			", CallDate: " + callDate + ", Call Duration: " + callDuration + 
            			", Dircode: " + dircode);
            */
            Log.w ("Praveen", phNumber);
            
		}
		
		return null;
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

}
