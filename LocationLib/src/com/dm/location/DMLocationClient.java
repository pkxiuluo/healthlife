package com.dm.location;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.os.IBinder;

public class DMLocationClient {
	private Context mContext;
	private DMLocationManager mLocationManager;
	private volatile boolean hasBindService = false;
	private MyServiceConnection mConn;
	private OnLocationChangeListener mListener;
	private DMLocation oldLocation;
	private DMLocationClientOption option;

	public DMLocationClient(Context context) {
		mContext = context;
		mConn = new MyServiceConnection();
	}

	public void setLocationListener(OnLocationChangeListener listener) {
		mListener = listener;
	}

	public void start() {
		if (!hasBindService || mLocationManager == null) {
			Intent intent = new Intent(mContext, DMLocationService.class);
			mContext.bindService(intent, mConn, Context.BIND_AUTO_CREATE);
		}
	}
/**
 * 获取最近的Location
 * @return  为空时，表示没有最近获取的位置
 */
	public DMLocation getLastKnownLocation() {
		DMLocation result = null ;
		if(mLocationManager==null){
			result = oldLocation;
		}else{
			DMLocation netLocatoin = mLocationManager.getLastKonwnLocation(LocationManager.NETWORK_PROVIDER);
			DMLocation gpsLocatoin = mLocationManager.getLastKonwnLocation(LocationManager.GPS_PROVIDER);
			if(	DMLocationUtils.batter(netLocatoin, gpsLocatoin)){
				result = netLocatoin;
			}else{
				result =gpsLocatoin;
			}
			if(DMLocationUtils.batter(oldLocation, result)){
				result = oldLocation;
			}
		}
		return result;
	}

	public void stop() {
		mContext.unbindService(mConn);
	}

	private class MyServiceConnection implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			if (service != null) {
				DMLocationService.LocationBinder binder = (DMLocationService.LocationBinder) service;
				mLocationManager = binder.getLocationManager();
				hasBindService = true;
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			hasBindService = false;
			mLocationManager.clearLocationObserver();
			mLocationManager = null;
		}
	}

	public static interface OnLocationChangeListener {
		public void onLocationChanged(DMLocation loation);
	}
}
