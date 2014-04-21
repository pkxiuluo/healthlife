package com.healthslife.run.dao;

import java.io.Serializable;

import com.dm.location.DMLocation;

public class RunResult implements Serializable {
	private static final long serialVersionUID = -8767531667801570745L;

	private RunSetting runSetting;

	private float distance;// 米
	private long duration;// 毫秒
	private long calorie;// 卡

	private DMLocation startLocation;
	private DMLocation endLocation;
	private float completeness;

	public RunResult() {
	}

	public RunSetting getRunSetting() {
		return runSetting;
	}

	public void setRunSetting(RunSetting runSetting) {
		this.runSetting = runSetting;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public long getCalorie() {
		return calorie;
	}

	public void setCalorie(long calorie) {
		this.calorie = calorie;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public float getCompleteness() {
		return completeness;
	}

	public void setCompleteness(float completeness) {
		this.completeness = completeness;
	}

	public DMLocation getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(DMLocation startLocation) {
		this.startLocation = startLocation;
	}

	public DMLocation getEndLocation() {
		return endLocation;
	}

	public void setEndLocation(DMLocation endLocation) {
		this.endLocation = endLocation;
	}

}
