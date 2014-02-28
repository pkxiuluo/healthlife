package com.example.locationlib;

import java.util.Date;

import android.location.Location;

public class DMLoation {

	double latitude;
	double longitude;
	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public long getTime() {
		return time;
	}

	double altitude;
	long time;

	public DMLoation() {
	}

	public DMLoation(Location location) {
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		altitude = location.getAltitude();
		time = location.getTime();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("time:");
		Date date = new Date(time);
		sb.append(date.toLocaleString());
		sb.append("latitude:");
		sb.append(latitude);
		sb.append("longitude:");
		sb.append(longitude);
		sb.append("altitude:");
		sb.append(altitude);

		return sb.toString();
	}
}
