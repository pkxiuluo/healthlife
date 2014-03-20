package com.healthslife.activitys;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.healthslife.BaseFragmentActivity;
import com.healthslife.R;
import com.healthslife.adapters.RunDataAdapter;
import com.healthslife.run.dao.RunResult;

public class RunResultActivity extends BaseFragmentActivity {
	public static final String EXTRA_RUN_RESULT = "runResult";
	private RunResult mRunResult;
	private ListView dataListView;
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		mRunResult = (RunResult) getIntent().getSerializableExtra(EXTRA_RUN_RESULT);
		if (mRunResult == null) {
			this.finish();
			return;
		}
		setContentView(R.layout.activity_run_result);
		setActionBar();
		dataListView = (ListView) findViewById(R.id.run_result_data_list);
		dataListView.setAdapter(new RunDataAdapter(this, mRunResult));

	}

	private void setActionBar() {
		actionBar = getActionBar();
		actionBar.setTitle(R.string.run_result_title);
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
