package com.framework.base;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;

import com.healthslife.db.DataBaseHelper;
import com.healthslife.music.MusicUtil;
import com.j256.ormlite.android.apptools.OpenHelperManager;
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
		
		MusicUtil.initPlayer(this);
		super.onCreate();
	}
	public BaseApplication getInstnce() {
		return mInstance;
	}
	
}
