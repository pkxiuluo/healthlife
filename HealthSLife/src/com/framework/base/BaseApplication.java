package com.framework.base;

import android.app.Application;

public class BaseApplication extends Application {
	private static BaseApplication mInstance;
	private MyApplicationManager manager;

	@Override
	public void onCreate() {
		mInstance = this;
		manager= new MyApplicationManager(this);
		super.onCreate();
	}

	public BaseApplication getInstnce() {
		return mInstance;
	}
}
