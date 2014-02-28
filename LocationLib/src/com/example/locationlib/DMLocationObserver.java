package com.example.locationlib;

import android.location.LocationManager;

public abstract class DMLocationObserver {
	private long startTime;
	private long lastTime;
	private long interval;
	private String provider;

	public DMLocationObserver(String provider, long interval) {
		if (provider == null || !(provider.equals(LocationManager.GPS_PROVIDER)
				|| provider.equals(LocationManager.NETWORK_PROVIDER))) {
			throw new IllegalArgumentException();
		}
		this.provider = provider;
		this.interval = interval;
		startTime = System.currentTimeMillis();
	}

	public long getInterval() {
		return interval;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

	final void observe(DMLoation loation) {
		long currentTime = System.currentTimeMillis();
		// lastTIme==0，第一次请求 调用callback
		// currentTime - lastTime >= interval最小间隔满足也调用callback
		if (lastTime == 0 || currentTime - lastTime >= interval) {
			lastTime = currentTime;
			callBack(loation);
		}
	};

	public abstract void callBack(DMLoation loation);

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		if (provider == null || !(provider.equals(LocationManager.GPS_PROVIDER)
				|| provider.equals(LocationManager.NETWORK_PROVIDER))) {
			throw new IllegalArgumentException();
		}
		this.provider = provider;
	}

}