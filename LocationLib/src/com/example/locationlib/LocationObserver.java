package com.example.locationlib;

public abstract class LocationObserver {
	private long startTime;
	private long lastTime;
	private long interval;

	public LocationObserver() {
		startTime = System.currentTimeMillis();
	}

	void observer(DMLoation loation) {
		long currentTime = System.currentTimeMillis();
		//lastTIme==0，第一次请求 调用callback
		//currentTime - lastTime >= interval最小间隔满足也调用callback
		if (lastTime==0||currentTime - lastTime >= interval) {
			lastTime = currentTime;
			callBack(loation);
		}
	};

	public abstract void callBack(DMLoation loation);
}