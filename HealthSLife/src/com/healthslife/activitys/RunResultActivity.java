package com.healthslife.activitys;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.healthslife.BaseFragmentActivity;
import com.healthslife.R;
import com.healthslife.adapters.RunDataAdapter;
import com.healthslife.run.dao.RunResult;
import com.healthslife.run.dao.RunSetting;

public class RunResultActivity extends BaseFragmentActivity {
	public static final String EXTRA_RUN_RESULT = "runResult";
	private RunResult mRunResult;
	private ListView dataListView;
	private ActionBar actionBar;
	private View targetLayout;
	private ImageView targetImg;
	private TextView targetTitleTxt;
	private TextView targetContentTxt;

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
		initTargetView();
		dataListView = (ListView) findViewById(R.id.run_result_data_list);
		View v = LayoutInflater.from(this).inflate(R.layout.list_header_run_data, null);
		dataListView.addHeaderView(v);
		dataListView.setAdapter(new RunDataAdapter(this, mRunResult));
	}

	private void initTargetView() {
		targetLayout = findViewById(R.id.run_result_target_layout);
		targetImg = (ImageView) findViewById(R.id.run_result_target_icon);
		targetTitleTxt = (TextView) findViewById(R.id.run_result_target_title_txt);
		targetContentTxt = (TextView) findViewById(R.id.run_result_target_content_txt);
		RunSetting setting = mRunResult.getRunSetting();
		if (setting == null || setting.getKind() == RunSetting.NORMAL) {
			targetLayout.setVisibility(View.GONE);
			return;
		} else {
			targetLayout.setVisibility(View.VISIBLE);
		}
		if (mRunResult.getCompleteness() >= 1) {
			targetImg.setImageResource(R.drawable.ic_correct);
		} else {
			targetImg.setImageResource(R.drawable.ic_wrong);
		}
		switch (setting.getKind()) {
		case RunSetting.DISTANCE:
			 targetTitleTxt.setText(R.string.run_result_target_distance);
			 targetContentTxt.setText(setting.getDistance()+"m");
			break;
		case RunSetting.DESTINATION:
			 targetTitleTxt.setText(R.string.run_result_target_destination);
			 targetContentTxt.setText("");
			break;
		default:
			break;
		}
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
