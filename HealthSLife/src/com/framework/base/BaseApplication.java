package com.framework.base;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class BaseApplication extends Application {
	private static BaseApplication mInstance;
	private MyApplicationManager manager;

	@Override
	public void onCreate() {
		mInstance = this;
		manager = new MyApplicationManager(this);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.build();
		ImageLoader.getInstance().init(config);
		super.onCreate();
	}

	public BaseApplication getInstnce() {
		return mInstance;
	}
}
