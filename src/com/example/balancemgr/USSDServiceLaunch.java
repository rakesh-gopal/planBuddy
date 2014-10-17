package com.example.balancemgr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.balancemgr.USSDService;

public class USSDServiceLaunch extends BroadcastReceiver {
	private String TAG = USSDServiceLaunch.class.getSimpleName();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "recicvd boot event, launching service");
		
		Intent srvIntent = new Intent(context, USSDService.class);
		context.startService(srvIntent);
	}

}
