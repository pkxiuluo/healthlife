package com.healthslife.run;

import java.util.ArrayList;

import android.content.Context;
import android.os.SystemClock;

import com.dm.location.DMLocation;
import com.dm.location.DMLocationClient;
import com.dm.location.DMLocationClient.OnLocationChangeListener;
import com.dm.location.DMLocationClientOption;
import com.healthslife.run.dao.RunSetting;

public class RunClient {
	private Context mContext;
	private DMLocationClient mClient;
//	private ArrayList<DMLocation> locationList;
	private RunSetting mSetting;
	private long startTime;
	private long endTime;
	private boolean isStop;
	private OnLocationChangeListener outListener;

	public RunClient(Context context) {
		mClient = new DMLocationClient(context);
		DMLocationClientOption option = new DMLocationClientOption();
		option.setInterval(2000);
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

	public long getDuration() {
		long duration = 0;
		if (isStop) {
			duration = endTime - startTime;
		} else {
			duration = SystemClock.elapsedRealtime() - startTime;

		}
		return duration;
	}

	private void onLocationChanged(DMLocation loation) {

	}
	
	
	public void stop(){
		isStop=true;
		endTime = SystemClock.elapsedRealtime();
		mClient.stop();
	}
	public void setOnLocationChangeListener(OnLocationChangeListener listener){
		outListener = listener;
	}

	private OnLocationChangeListener mListener = new OnLocationChangeListener() {

		@Override
		public void onLocationChanged(DMLocation loation) {
			RunClient.this.onLocationChanged(loation);
			if (outListener != null) {
				outListener.onLocationChanged(loation);
			}
		}
	};
}
