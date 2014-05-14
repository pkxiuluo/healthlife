package com.healthslife.healthtest.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class ECGAnalysisRecord {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private String date;
	@DatabaseField
	private String content;
	@DatabaseField
	private String filePath;

	public ECGAnalysisRecord() {
	}

	public ECGAnalysisRecord(String date, String content, String filePath) {
		this.date = date;
		this.content = content;
		this.filePath = filePath;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
