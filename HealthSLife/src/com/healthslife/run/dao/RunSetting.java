package com.healthslife.run.dao;

import java.io.Serializable;

import com.dm.location.DMLocation;

public class RunSetting implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -855180605831626423L;
	public final static int NORMAL = 0;
	public final static int DISTANCE = 1;
	public final static int DESTINATION = 2;

	private int kind;
	private DMLocation dest;
	private int distance;

	public RunSetting() {
		this(NORMAL);
	}

	public RunSetting(int kind) {
		this.setKind(kind);
	}

	public int getKind() {
		return kind;
	}

	public void setKind(int kind) {
		if (kind != NORMAL && kind != DISTANCE && kind != DESTINATION) {
			throw new IllegalArgumentException("参数必须为RunSetting.NORMAL或RunSetting.DISTANCE或RunSetting.DESTINATION");
		}
		this.kind = kind;
	}

	public DMLocation getDest() {
		return dest;
	}

	public void setDest(DMLocation dest) {
		this.dest = dest;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

}
