package com.healthslife.activitys;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;

import com.healthslife.BaseFragmentActivity;
import com.healthslife.R;
import com.healthslife.run.dao.RunSetting;

public class RunHistoryActivity extends BaseFragmentActivity {
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setActionBar();
		initView();
	}

	private void initView() {
		setContentView(R.layout.activity_run_history);
	}

	private void setActionBar() {
		actionBar = getActionBar();
		actionBar.setTitle(R.string.run_history_action_bar_title);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
