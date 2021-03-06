package com.healthslife.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppSetting {
	private static final String PREFERENCE_NAME = "health";
	private static final String COUNT_DOWN = "COUNT_DOWN";
	private static final String RUN_MUSIC = "RUN_MUSIC";
	private static final String RUN_ALARM = "RUN_ALARM";
	private Context mContext;

	public AppSetting(Context context) {
		mContext = context.getApplicationContext();
	}

	public int getCountDown() {
		SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCE_NAME, 0);
		return preferences.getInt(COUNT_DOWN, 5);
	}

	public void setCountDown(int time) {
		SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCE_NAME, 0);
		Editor editor = preferences.edit();
		editor.putInt(COUNT_DOWN, time);
		editor.commit();
	}

	public boolean getRunMusicState() {
		SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCE_NAME, 0);
		return preferences.getBoolean(RUN_MUSIC, true);
	}

	public void setRunMusicState(boolean isOn) {
		SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCE_NAME, 0);
		Editor editor = preferences.edit();
		editor.putBoolean(RUN_MUSIC, isOn);
		editor.commit();
	}

	public boolean getRunAlarmState() {
		SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCE_NAME, 0);
		return preferences.getBoolean(RUN_ALARM, true);
	}

	public void setRunAlarmState(boolean isOn) {
		SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCE_NAME, 0);
		Editor editor = preferences.edit();
		editor.putBoolean(RUN_ALARM, isOn);
		editor.commit();
	}

}
