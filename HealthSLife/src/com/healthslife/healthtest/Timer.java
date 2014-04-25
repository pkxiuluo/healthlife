package com.healthslife.healthtest;

public class Timer {
	private long startPoint;
	
	public Timer() {
		startPoint = 0;
	}

	public void startTimer() {
		startPoint = System.currentTimeMillis();
	}

	public long getMillis() {
		return System.currentTimeMillis()-startPoint;
	}

	public long getSecond() {
		return (System.currentTimeMillis()-startPoint)/1000;
	}
	public void resetAndStop(){
		startPoint = 0;
	}
}
