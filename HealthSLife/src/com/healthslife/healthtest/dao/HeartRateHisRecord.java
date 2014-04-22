package com.healthslife.healthtest.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class HeartRateHisRecord {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private String date;
	@DatabaseField
	private int state;// 1代表静息状态即平常态 2运动后…………
	@DatabaseField
	private int heartRate;

	public HeartRateHisRecord() {
	}

	public HeartRateHisRecord(String date, int state, int heartRate) {
		this.date = date;
		this.state = state;
		this.heartRate = heartRate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getHeartRate() {
		return heartRate;
	}

	public void setHeartRate(int heartRate) {
		this.heartRate = heartRate;
	}

}
