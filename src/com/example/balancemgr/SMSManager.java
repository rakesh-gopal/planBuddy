package com.example.balancemgr;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
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
	
	public ArrayList<String> getInboxSMS(Context context) {
		
		// Create Inbox box URI
		Uri inboxURI = Uri.parse("content://sms/inbox");
		 
		// List required columns
		String[] reqCols = new String[] { "_id", "address", "body" };
		 
		// Get Content Resolver object, which will deal with Content Provider
		ContentResolver cr = context.getContentResolver();
		 
		// Fetch Inbox SMS Message from Built-in Content Provider
		Cursor cursor = cr.query(inboxURI, reqCols, null, null, null);
		
		int count = 0;
		
		while (cursor.moveToNext()) {
            String strphNumber = cursor.getString(cursor.getColumnIndex("_id"));
            String strAddress = cursor.getString(cursor.getColumnIndex("address"));
            String strBody = cursor.getString(cursor.getColumnIndex("body"));
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
            Log.w ("Praveen getInboxSMS - ", strphNumber + "," + strAddress + ", " + strBody);
            count ++;
            
		}
		
		Log.w ("Praveen" , " getInboxSMS No of Msg's: " + count);
		
		return null;
		
	}

	
	public ArrayList getDraftSMS(Context context) {
		
		// Create Inbox box URI
		Uri inboxURI = Uri.parse("content://sms/draft");
		 
		// List required columns
		String[] reqCols = new String[] { "_id", "address", "body" };
		 
		// Get Content Resolver object, which will deal with Content Provider
		ContentResolver cr = context.getContentResolver();
		 
		// Fetch Inbox SMS Message from Built-in Content Provider
		Cursor cursor = cr.query(inboxURI, reqCols, null, null, null);
		
		int count = 0;
		
		while (cursor.moveToNext()) {
            String strphNumber = cursor.getString(cursor.getColumnIndex("_id"));
            String strAddress = cursor.getString(cursor.getColumnIndex("address"));
            String strBody = cursor.getString(cursor.getColumnIndex("body"));
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
            Log.w ("Praveen getDraftSMS - ", strphNumber + "," + strAddress + ", " + strBody);
            count ++;
		}
		
		Log.w ("Praveen", " getInboxSMS - " + count);
        count ++;
		
		return null;
		
	}
	
	public String saveDraftSMS (Context context, String address, String messagebody) {
		
		//Store the message in the draft folder so that it shows in Messaging apps.
		ContentValues values = new ContentValues();
		// Message address.
		values.put("address", address); 
		// Message body.
		values.put("body", messagebody);
		// Date of the draft message.
		values.put("date", String.valueOf(System.currentTimeMillis())); 
		values.put("type", "3");
		// Put the actual thread id here. 0 if there is no thread yet.
		values.put("thread_id", "0"); 
		
		// Get Content Resolver object, which will deal with Content Provider
		ContentResolver cr = context.getContentResolver();
		cr.insert(Uri.parse("content://sms/draft"), values);
		
		String str = cr.toString();
		
		return str;
		
	}
	
	public String saveInboxSMS (Context context, String address, String messagebody) {
		
		//Store the message in the draft folder so that it shows in Messaging apps.
		ContentValues values = new ContentValues();
		// Message address.
		values.put("address", address); 
		// Message body.
		values.put("body", messagebody);
		// Date of the draft message.
		values.put("date", String.valueOf(System.currentTimeMillis())); 
		values.put("type", "3");
		// Put the actual thread id here. 0 if there is no thread yet.
		values.put("thread_id", "0"); 
		
		// Get Content Resolver object, which will deal with Content Provider
		ContentResolver cr = context.getContentResolver();
		cr.insert(Uri.parse("content://sms/inbox"), values);
		
		String str = cr.toString();
		
		return str;
		
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

}
