package com.healthslife.run.dao;

import java.util.Date;

import com.dm.location.DMLocation;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class RunRecord {

	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField(canBeNull = false)
	private int kind;
	@DatabaseField(canBeNull = false)
	private Date createTime;

	@DatabaseField
	private double targetLat;
	@DatabaseField
	private double targetLng;
	@DatabaseField
	private int targetDistance;

	@DatabaseField
	private double startLat;
	@DatabaseField
	private double startlng;
	@DatabaseField
	private double endLat;
	@DatabaseField
	private double endLng;
	@DatabaseField
	private float runDistance;
	@DatabaseField
	private long runDuration;
	@DatabaseField
	private long runCalorie;
	@DatabaseField
	private float averageSpeed;

	public float getAverageSpeed() {
		return averageSpeed;
	}

	public void setAverageSpeed(float averageSpeed) {
		this.averageSpeed = averageSpeed;
	}

	@DatabaseField
	private float completeness;

	public RunRecord() {
		createTime = new Date(System.currentTimeMillis());
	}

	public static RunRecord newRunRecord(RunResult runResult) {
		RunSetting runSetting = runResult.getRunSetting();
		if (runResult == null || runSetting == null) {
			throw new IllegalArgumentException();
		}
		RunRecord record = new RunRecord();
		int kind = runSetting.getKind();
		record.setKind(kind);
		record.setRunCalorie(runResult.getCalorie());
		record.setRunDistance(runResult.getDistance());
		record.setRunDuration(runResult.getDuration());
		record.setAverageSpeed(runResult.getDistance() / (runResult.getDuration() / 1000));
		if (kind == RunSetting.NORMAL) {
			record.setCompleteness(1);
		} else {
			record.setCompleteness(runResult.getCompleteness());

			if (kind == RunSetting.DISTANCE) {
				record.setTargetDistance(runSetting.getDistance());
			} else if (kind == RunSetting.DESTINATION) {
				DMLocation location = runSetting.getDest();
				if (location != null) {
					record.setTargetLat(location.getLatitude());
					record.setTargetLng(location.getLatitude());
				}
			}
		}
		return record;
	}

	public static RunResult createRunResult(RunRecord runRecord) {
		if (runRecord == null) {
			throw new IllegalArgumentException();
		}
		int kind = runRecord.getKind();
		RunSetting setting = new RunSetting(kind);
		RunResult result = new RunResult();
		result.setCalorie(runRecord.getRunCalorie());
		result.setDistance(runRecord.getRunDistance());
		result.setDuration(runRecord.getRunDuration());
		if (kind == RunSetting.NORMAL) {

		} else {
			result.setCompleteness(runRecord.getCompleteness());
			if (kind == RunSetting.DISTANCE) {
				setting.setDistance(runRecord.getTargetDistance());
			} else if (kind == RunSetting.DESTINATION) {
				setting.setDest(new DMLocation(runRecord.getTargetLat(), runRecord.getTargetLng()));
			}
		}
		result.setRunSetting(setting);
		return result;
	}

	public int getId() {
		return id;
	}

	public int getKind() {
		return kind;
	}

	public void setKind(int kind) {
		this.kind = kind;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public double getTargetLat() {
		return targetLat;
	}

	public void setTargetLat(double targetLat) {
		this.targetLat = targetLat;
	}

	public double getTargetLng() {
		return targetLng;
	}

	public void setTargetLng(double targetLng) {
		this.targetLng = targetLng;
	}

	public int getTargetDistance() {
		return targetDistance;
	}

	public void setTargetDistance(int targetDistance) {
		this.targetDistance = targetDistance;
	}

	public double getStartLat() {
		return startLat;
	}

	public void setStartLat(double startLat) {
		this.startLat = startLat;
	}

	public double getStartlng() {
		return startlng;
	}

	public void setStartlng(double startlng) {
		this.startlng = startlng;
	}

	public double getEndLat() {
		return endLat;
	}

	public void setEndLat(double endLat) {
		this.endLat = endLat;
	}

	public double getEndLng() {
		return endLng;
	}

	public void setEndLng(double endLng) {
		this.endLng = endLng;
	}

	public float getRunDistance() {
		return runDistance;
	}

	public void setRunDistance(float runDistance) {
		this.runDistance = runDistance;
	}

	public long getRunDuration() {
		return runDuration;
	}

	public void setRunDuration(long runDuration) {
		this.runDuration = runDuration;
	}

	public long getRunCalorie() {
		return runCalorie;
	}

	public void setRunCalorie(long runCalorie) {
		this.runCalorie = runCalorie;
	}

	public float getCompleteness() {
		return completeness;
	}

	public void setCompleteness(float completeness) {
		this.completeness = completeness;
	}

}
