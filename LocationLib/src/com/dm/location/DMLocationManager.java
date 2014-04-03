package com.dm.location;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class DMLocationManager {

	private LocationManager mLocationManager;
	private Context mContext;
	private ArrayList<DMLocationObserver> observerList;

	private GPSListener mGpsListener;

	private NetWorkListener mNetWorkListener;
	private boolean isGPSListened = false;
	private long gpsInterval = 0;
	private boolean isNetWorkListened = false;
	private long netWorkInterval = 0;
	private static final long MIN_INTERVAL = 1000;

	public DMLocationManager(Context context) {
		mContext = context;
		mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		observerList = new ArrayList<DMLocationObserver>();
		mGpsListener = new GPSListener();
		mNetWorkListener = new NetWorkListener();
	}
	
	public DMLocation getLastKonwnLocation(String provider){
		Location location =mLocationManager.getLastKnownLocation(provider);
		return new DMLocation(location);
	}

	public void notifiObserverChanged() {
		if (isConfigChanged(LocationManager.GPS_PROVIDER)) {
			reset(LocationManager.GPS_PROVIDER);
		}
		if (isConfigChanged(LocationManager.NETWORK_PROVIDER)) {
			reset(LocationManager.NETWORK_PROVIDER);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		clearLocationObserver();
		super.finalize();
	}

	public void addLocatoinObserver(DMLocationObserver observer) {
		observerList.add(observer);
		if (isConfigChanged(observer.getProvider())) {
			reset(observer.getProvider());
		}
	}

	public void removeLocatoinObserver(DMLocationObserver observer) {
		observerList.remove(observer);
		if (isConfigChanged(observer.getProvider())) {
			reset(observer.getProvider());
		}
	}

	public void clearLocationObserver() {
		observerList.clear();
		if (isConfigChanged(LocationManager.GPS_PROVIDER)) {
			reset(LocationManager.GPS_PROVIDER);
		}
		if (isConfigChanged(LocationManager.NETWORK_PROVIDER)) {
			reset(LocationManager.NETWORK_PROVIDER);
		}
	}

	private void reset(String provider) {
		if (provider.equals(LocationManager.GPS_PROVIDER)) {
			mLocationManager.removeUpdates(mGpsListener);
			if (isContainProvider(provider)) {
				gpsInterval = getMinInterval(LocationManager.GPS_PROVIDER);
				mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, gpsInterval, 0, mGpsListener);
				isGPSListened = true;
			} else {
				isGPSListened = false;
			}
		} else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
			mLocationManager.removeUpdates(mNetWorkListener);
			if (isContainProvider(provider)) {
				netWorkInterval = getMinInterval(provider);
				// netWorkInterval = 0;
				mLocationManager
						.requestLocationUpdates(provider, netWorkInterval, 0, mNetWorkListener);
				isNetWorkListened = true;
			} else {
				isNetWorkListened = false;
			}
		}

	}

	private boolean isConfigChanged(String provider) {
		boolean isChanged = false;
		boolean openStateChanged = false;
		boolean intervalChanged = false;
		if (provider.equals(LocationManager.GPS_PROVIDER)) {
			if (isContainProvider(LocationManager.GPS_PROVIDER) != isGPSListened) {
				openStateChanged = true;
			}
			if (getMinInterval(provider) != gpsInterval) {
				intervalChanged = true;
			}
		} else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
			if (isContainProvider(LocationManager.NETWORK_PROVIDER) != isNetWorkListened) {
				openStateChanged = true;
			}
			if (getMinInterval(provider) != netWorkInterval) {
				intervalChanged = true;
			}
		}
		if (openStateChanged || intervalChanged) {
			isChanged = true;
		}
		return isChanged;
	}

	private boolean isContainProvider(String provider) {
		boolean contain = false;
		for (DMLocationObserver observer : observerList) {
			if (observer.getProvider().equals(provider)) {
				contain = true;
				break;
			}
		}
		return contain;
	}

	private long getMinInterval(String provider) {
		long max = MIN_INTERVAL;
		for (DMLocationObserver observer : observerList) {
			if (provider.equals(observer.getProvider()) && observer.getInterval() > max) {
				max = observer.getInterval();
			}
		}
		return max;
	}

	private class GPSListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
//			System.out.println("gpslisten"+location.getAccuracy());
			for (DMLocationObserver observer : observerList) {
				if (observer.getProvider().equals(LocationManager.GPS_PROVIDER)) {
					observer.observe(new DMLocation(location));
				}

			}
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}
	}

	private class NetWorkListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
//			System.out.println("netlisten"+location.getAccuracy());
			for (DMLocationObserver observer : observerList) {
				if (observer.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
					observer.observe(new DMLocation(location));
				}
			}
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

	}

}
