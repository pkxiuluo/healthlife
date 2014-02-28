package com.example.locationlib;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class DMLocationClient {
	private Context mContext;
	private DMLocationManager mLocationManager;
	private volatile boolean hasBindService = false;
	private MyServiceConnection conn;

	public DMLocationClient(Context context) {
		mContext = context;
		conn = new MyServiceConnection();
	}

	public void start() {
		Intent intent = new Intent(mContext, DMLocationService.class);
		mContext.bindService(intent, conn, Context.BIND_AUTO_CREATE);
	}

	public void stop() {
		mContext.unbindService(conn);
	}

	private class MyServiceConnection implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			DMLocationService.LocationBinder binder = (DMLocationService.LocationBinder) service;
			mLocationManager = binder.getLocationManager();
			hasBindService = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			hasBindService = false;
		}

	}
}
