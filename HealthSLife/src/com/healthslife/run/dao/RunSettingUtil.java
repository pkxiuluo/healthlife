package com.healthslife.run.dao;

import com.healthslife.activitys.NormalRunActivity;

import android.content.Context;
import android.content.Intent;

public class RunSettingUtil {
	public static void startActivity(Context context, RunSetting setting) {
		int kind = setting.getKind();
		Intent intent = null;
		switch (kind) {
		case RunSetting.NORMAL:
			intent = new Intent(context, NormalRunActivity.class);
			intent.putExtra(NormalRunActivity.RUN_SETTING, setting);
			context.startActivity(intent);
			break;
		default:
			break;
		}
	
	}
}
