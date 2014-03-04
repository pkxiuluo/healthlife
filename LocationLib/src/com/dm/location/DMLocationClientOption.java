package com.dm.location;

import android.R.integer;

public class DMLocationClientOption {
	int interval;
	public static final int GPS_FIRST = 0;
	public static final int NETWORK_FIRST = 1;

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		if (priority != GPS_FIRST && priority != NETWORK_FIRST) {
			throw new IllegalArgumentException(
					"必须是DMLocationClientOption.GPS_FIRST或DMLocationClientOption.NETWORK_FIRST其中之一");
		}
		this.priority = priority;
	}

	int priority;

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

}
