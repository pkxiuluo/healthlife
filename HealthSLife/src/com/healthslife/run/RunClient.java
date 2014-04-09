package com.healthslife.run;

import android.content.Context;
import android.location.Location;
import android.os.SystemClock;

import com.dm.location.DMLocation;
import com.dm.location.DMLocationClient;
import com.dm.location.DMLocationClient.OnLocationChangeListener;
import com.dm.location.DMLocationClientOption;
import com.dm.location.DMLocationUtils;
import com.healthslife.run.dao.RunSetting;

public class RunClient {
	private Context mContext;
	private DMLocationClient mClient;
	// private ArrayList<DMLocation> locationList;
	private RunSetting mSetting;
	private long startTime;
	private long endTime;
	private boolean isStop = false;
	private OnLocationChangeListener outListener;
	private DMLocation startLocation;
	private DMLocation currentLocation;

	private float distance;

	public RunClient(Context context) {
		mClient = new DMLocationClient(context);
		DMLocationClientOption option = new DMLocationClientOption();
		option.setInterval(1000);
		option.setPriority(DMLocationClientOption.GPS_FIRST);
		mClient.setOption(option);
	}

	public void init() {
		mClient.start();
	}

	public void start() {
		startTime = SystemClock.elapsedRealtime();
		mClient.setLocationListener(mListener);
	}

	public float getDistance() {
		if (startLocation == null || currentLocation == null) {
			return 0;
		}
		return distance;
	}

	/**
	 * 
	 * @return 毫秒
	 */
	public long getDuration() {
		long duration = 0;
		if (isStop) {
			duration = endTime - startTime;
		} else {
			duration = SystemClock.elapsedRealtime() - startTime;
		}
		return duration;
	}

	protected void onLocationChanged(DMLocation loation) {
		long millis = this.getDuration();
		float meter = this.getDistance();

		System.out.println("dur  " + millis + "dis " + meter);
	}

	public void stop() {
		isStop = true;
		endTime = SystemClock.elapsedRealtime();
		mClient.stop();
	}

	public void setOnLocationChangeListener(OnLocationChangeListener listener) {
		outListener = listener;
	}

	private OnLocationChangeListener mListener = new OnLocationChangeListener() {

		@Override
		public void onLocationChanged(DMLocation location) {
			if (startLocation == null) {
				startLocation = location;
			}
			float meters = 0;
			if (currentLocation != null && location != null) {
				meters = DMLocationUtils.distanceBetween(location, currentLocation);
			}else{
				currentLocation =location;
			}
			if (meters > currentLocation.getAccuracy()) {
				distance += meters;
				currentLocation = location;
			}

			RunClient.this.onLocationChanged(location);
			if (outListener != null) {
				outListener.onLocationChanged(location);
			}
		}
	};
}
