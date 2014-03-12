package com.healthslife.activitys;

import android.app.ActionBar;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.healthslife.BaseFragmentActivity;
import com.healthslife.R;

public class NormalRunActivity extends BaseFragmentActivity {
	public static final String RUN_SETTING = "runSetting";
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle arg0) {
		setContentView(R.layout.activity_run_normal);
		super.onCreate(arg0);
	}

	private void setActionBar() {
		actionBar = getActionBar();
		actionBar.setTitle(R.string.run_normal);
		actionBar.setDisplayHomeAsUpEnabled(true);
		Drawable drawable = null;
//		actionBar.setIcon(drawable);
	}

}
