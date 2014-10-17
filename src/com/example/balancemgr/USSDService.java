
package com.example.balancemgr;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.os.PatternMatcher;
import android.os.RemoteException;
import android.util.Log;

import com.android.internal.telephony.IExtendedNetworkService;
import com.android.internal.telephony.R;

/**
 * @author tushar (from github.com/alaasalman/ussdinterceptor/)
 *
 */
public class USSDService extends Service{

    private String TAG = USSDService.class.getSimpleName();
    
    private boolean serviceActive = false;
    
    BroadcastReceiver recv = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_INSERT)) {
				// activate listener service
				serviceActive = true;
				Log.d(TAG, "activate ussd listener");
			} else if (intent.getAction().equals(Intent.ACTION_DELETE)) {
				// deactivate listener service
				serviceActive = false;
				Log.d(TAG, "deactivate ussd listener");
			}
		}
    	
    };
    
    private final IExtendedNetworkService.Stub mBinder = new IExtendedNetworkService.Stub() {

		@Override
		public void setMmiString(String number) throws RemoteException {
			Log.d(TAG, "Set MmiString: " + number);
		}

		@Override
		public CharSequence getMmiRunningText() throws RemoteException {
			if (serviceActive == true) {
				return null;
			}
			
			return "USSD running";
		}

		@Override
		public CharSequence getUserMessage(CharSequence text)
				throws RemoteException {
			Log.d(TAG, "Get user message " + text);
			
			if (serviceActive == false) {
				Log.d(TAG, "inactive " + text);
				return text;
			}
			
			// listener is active, broadcast data
			
			// build data to send with intent for activity , URI RFC 2396
			Uri ussdDataUri = new Uri.Builder()
			.scheme(getBaseContext().getString(R.string.uri_scheme))
			.authority(getBaseContext().getString(R.string.uri_authority))
			.path(getBaseContext().getString(R.string.uri_path))
			.appendQueryParameter(getBaseContext().getString(R.string.uri_param_name), text.toString())
			.build();
			
			sendBroadcast(new Intent(Intent.ACTION_GET_CONTENT, ussdDataUri));
			
			serviceActive = false;
			return null;
			
		}

		@Override
		public void clearMmiString() throws RemoteException {
			Log.d(TAG, "Called clear");
		}
    	
    };
    
	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "called onbind");
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_INSERT);
		filter.addAction(Intent.ACTION_DELETE);
	    filter.addDataScheme(getBaseContext().getString(R.string.uri_scheme));
	    filter.addDataAuthority(getBaseContext().getString(R.string.uri_authority), null);
	    filter.addDataPath(getBaseContext().getString(R.string.uri_path), PatternMatcher.PATTERN_LITERAL);
		
		registerReceiver(recv, filter);
		
		return mBinder;
	}

	
}
