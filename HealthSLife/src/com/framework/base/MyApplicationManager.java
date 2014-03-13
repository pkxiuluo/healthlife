package com.framework.base;

import com.framework.common.crash.CrashManager;

import android.content.Context;

public class MyApplicationManager {
	private Context mContext;
	private static boolean isInitCrash =false;


	public MyApplicationManager(Context context) {
		mContext = context;
		initBase();
		initOptional();
	}

	private void initBase() {
		initAppInfo();
	}

	private void initOptional() {
		if(isInitCrash){
			initCrashManager();
		}
	}

	private void initAppInfo() {
		MyApplicationInfo info = MyApplicationInfo.getInstance();
		info.init(mContext);
	}

	private void initCrashManager() {
		CrashManager.registerHandler();
	}

}
