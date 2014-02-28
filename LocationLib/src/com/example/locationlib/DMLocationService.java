package com.example.locationlib;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * 保证多个activity使用一个DMLocationManager
 * @author yp
 *
 */
public  class DMLocationService extends Service {

	private DMLocationManager manager;

	@Override
	public void onCreate() {
		manager = new DMLocationManager(this);
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return new LocationBinder();
	}

	public class LocationBinder extends Binder {
		public DMLocationManager getLocationManager() {
			return manager;
		}
	}

}
