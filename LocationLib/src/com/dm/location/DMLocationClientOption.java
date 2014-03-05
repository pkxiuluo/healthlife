package com.dm.location;

public class DMLocationClientOption {

	public static final int GPS_FIRST = 0;
	public static final int NETWORK_FIRST = 1;

	private int interval = 1000;
	private int priority = 0;

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

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public static DMLocationClientOption getDefaultOption() {
		DMLocationClientOption option = new DMLocationClientOption();
		return option;
	}
}
