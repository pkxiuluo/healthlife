package com.dm.location;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;

public class DMLocationClient {
	private Context mContext;
	private DMLocationManager mLocationManager;
	private volatile boolean hasBindService = false;
	private MyServiceConnection mConn;
	private OnLocationChangeListener mListener;
	private volatile DMLocation oldLocation;
	private DMLocationClientOption mOption;
	private List<DMLocationObserver> observerList = new ArrayList<DMLocationObserver>();
	private static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	private Handler handler = new Handler();
	private boolean isStartd =false;
	
	private ScheduledFuture<?> future;

	public DMLocationClient(Context context) {
		mContext = context;
		mConn = new MyServiceConnection();
	}

	public void setOption(DMLocationClientOption option) {
		mOption = option;
	}

	public void setLocationListener(OnLocationChangeListener listener) {
		mListener = listener;
	}

	public void start() {
		if (!hasBindService) {
			Intent intent = new Intent(mContext, DMLocationService.class);
			mContext.bindService(intent, mConn, Context.BIND_AUTO_CREATE);
		}else{
			if (mOption == null) {
				mOption = DMLocationClientOption.getDefaultOption();
			}
			startRequest(mOption);
			isStartd =true;
		}
	}

	private void startRequest(DMLocationClientOption option) {
		future =	executor.scheduleAtFixedRate(new LocationResponse(), option.getInterval(), option.getInterval(),
				TimeUnit.MILLISECONDS);
		observerList.clear();
		observerList.addAll(genObserverList(option));
		for (DMLocationObserver observer : observerList) {
			mLocationManager.addLocatoinObserver(observer);
		}
	}

	private List<DMLocationObserver> genObserverList(DMLocationClientOption option) {
		List<DMLocationObserver> observerList = new ArrayList<DMLocationObserver>();
		int priority = option.getPriority();
		int interval = Math.max(1000, option.getInterval());
		if (priority == DMLocationClientOption.GPS_FIRST) {
			DMLocationObserver gps = new MyLocationOberver(LocationManager.GPS_PROVIDER, interval);
			DMLocationObserver net = new MyLocationOberver(LocationManager.NETWORK_PROVIDER, interval);
			observerList.add(gps);
			observerList.add(net);
		} else if (priority == DMLocationClientOption.NETWORK_FIRST) {
			DMLocationObserver net = new MyLocationOberver(LocationManager.NETWORK_PROVIDER, interval);
			observerList.add(net);
		}
		return observerList;
	}

	/**
	 * 获取最近的Location
	 * 
	 * @return 为空时，表示没有最近获取的位置
	 */
	public DMLocation getLastKnownLocation() {
		DMLocation result = null;
		if (mLocationManager == null) {
			result = oldLocation;
		} else {
			DMLocation netLocatoin = mLocationManager.getLastKonwnLocation(LocationManager.NETWORK_PROVIDER);
			DMLocation gpsLocatoin = mLocationManager.getLastKonwnLocation(LocationManager.GPS_PROVIDER);
			if (DMLocationUtils.batter(netLocatoin, gpsLocatoin)) {
				result = netLocatoin;
			} else {
				result = gpsLocatoin;
			}
			if (DMLocationUtils.batter(oldLocation, result)) {
				result = oldLocation;
			}
		}
		return result;
	}

	public void stop() {
		if(isStartd){
			future.cancel(true);
			if (mLocationManager != null) {
				for (DMLocationObserver observer : observerList) {
					mLocationManager.removeLocatoinObserver(observer);
				}
			}
			isStartd =false;
		}
	}
	public void unBind(){
		mContext.unbindService(mConn);
	}

	private class MyServiceConnection implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			if (service != null) {
				DMLocationService.LocationBinder binder = (DMLocationService.LocationBinder) service;
				mLocationManager = binder.getLocationManager();
				hasBindService = true;
				if (mOption == null) {
					mOption = DMLocationClientOption.getDefaultOption();
				}
				startRequest(mOption);
				isStartd =true;
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			executor.shutdown();
			if (mLocationManager != null) {
				for (DMLocationObserver observer : observerList) {
					mLocationManager.removeLocatoinObserver(observer);
				}
			}
			hasBindService = false;
			mLocationManager = null;
			isStartd =false;
		}
	}

	private class LocationResponse implements Runnable {
		@Override
		public void run() {
			handler.post(new Runnable() {
				@Override
				public void run() {
					if (oldLocation != null && mListener != null) {
						mListener.onLocationChanged(oldLocation);
					}

				}
			});
		}
	}

	private class MyLocationOberver extends DMLocationObserver {
		public MyLocationOberver(String provider, long interval) {
			super(provider, interval);
		}

		@Override
		public void callBack(DMLocation loation) {
			if (DMLocationUtils.batter(loation, oldLocation)) {
				oldLocation = loation;
			}
		}
	}

	public static interface OnLocationChangeListener {
		public void onLocationChanged(DMLocation loation);
	}
}
