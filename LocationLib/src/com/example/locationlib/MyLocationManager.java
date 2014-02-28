package com.example.locationlib;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.content.Context;
import android.location.LocationManager;

public class MyLocationManager {
	private LocationManager mLocationManager;
	private Context mContext;

	public MyLocationManager(Context context) {
		mContext = context;
		mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	}

	

}
