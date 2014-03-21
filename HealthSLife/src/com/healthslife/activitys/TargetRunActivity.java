package com.healthslife.activitys;

import android.app.ActionBar;
import android.os.Bundle;

import com.healthslife.BaseFragmentActivity;
import com.healthslife.R;
import com.healthslife.run.dao.RunSetting;

public class TargetRunActivity extends BaseFragmentActivity {
	private ActionBar actionBar;
	public static final String EXTRA_RUN_SETTING = "runSetting";
	private RunSetting mRunSetting;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		mRunSetting = (RunSetting) getIntent().getSerializableExtra(EXTRA_RUN_SETTING);
		if (mRunSetting == null) {
			this.finish();
			return;
		}
		setActionBar();
	}

	private void setActionBar() {
		actionBar = getActionBar();
		if (mRunSetting.getKind() == RunSetting.DISTANCE) {
			actionBar.setTitle(R.string.run_distance);
		} else if (mRunSetting.getKind() == RunSetting.DESTINATION) {
			actionBar.setTitle(R.string.run_destination);
		}
		actionBar.setDisplayHomeAsUpEnabled(true);
		// actionBar.setLogo(R.drawable.navi_run_);
	}
}
