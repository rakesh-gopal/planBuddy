
package com.example.balancemgr;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.android.internal.telephony.IExtendedNetworkService;

/**
 * @author tushar
 *
 */
public class USSDService extends Service{

    private String TAG = USSDService.class.getSimpleName();
    
    private final IExtendedNetworkService.Stub mBinder = new IExtendedNetworkService.Stub() {

		@Override
		public void setMmiString(String number) throws RemoteException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public CharSequence getMmiRunningText() throws RemoteException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CharSequence getUserMessage(CharSequence text)
				throws RemoteException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void clearMmiString() throws RemoteException {
			// TODO Auto-generated method stub
			
		}
    	
    };
    
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		
		return null;
	}

	
}
