package com.framework.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class MyApplicationInfo {
	public static Context APPLICATION_CONTEXT;
	public static String APP_VERSION = null;
	public static String APP_PACKAGE = null;
	public static String ANDROID_VERSION = null;
	public static String PHONE_MODEL = null;
	public static String PHONE_MANUFACTURER = null;
	
	private static volatile MyApplicationInfo mSingleInstance;
	private MyApplicationInfo(){
	}
	public static MyApplicationInfo getInstance() {
		if(mSingleInstance==null){
			synchronized (mSingleInstance) {
				mSingleInstance =	new MyApplicationInfo();
			}
		}
		return mSingleInstance;
	}
	public void init(Context context){
		APPLICATION_CONTEXT = context;
		MyApplicationInfo.ANDROID_VERSION = android.os.Build.VERSION.RELEASE;
		MyApplicationInfo.PHONE_MODEL = android.os.Build.MODEL;
		MyApplicationInfo.PHONE_MANUFACTURER = android.os.Build.MANUFACTURER;

		PackageManager packageManager = context.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			MyApplicationInfo.APP_VERSION = "" + packageInfo.versionCode;
			MyApplicationInfo.APP_PACKAGE = packageInfo.packageName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
	}
}
