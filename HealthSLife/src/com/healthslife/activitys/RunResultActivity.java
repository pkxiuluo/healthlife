package com.healthslife.activitys;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.healthslife.BaseFragmentActivity;
import com.healthslife.R;
import com.healthslife.adapters.RunDataAdapter;
import com.healthslife.run.dao.RunResult;
import com.healthslife.run.dao.RunSetting;

public class RunResultActivity extends BaseFragmentActivity implements OnClickListener {
	public static final String EXTRA_RUN_RESULT = "runResult";
	private RunResult mRunResult;
	private ListView dataListView;
	private ActionBar actionBar;
	private View targetLayout;
	private ImageView targetImg;
	private TextView targetTitleTxt;
	private TextView targetContentTxt;
	private TextView runResultTxt;
	private ImageView runResultImg;

	private View toShareBtn;
	private View toHeartBtn;
	private View toMainBtn;

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
		toShareBtn = findViewById(R.id.run_result_share_btn);
		toHeartBtn = findViewById(R.id.run_result_heart_btn);
		toMainBtn = findViewById(R.id.run_result_main_btn);
		toShareBtn.setOnClickListener(this);
		toHeartBtn.setOnClickListener(this);
		toMainBtn.setOnClickListener(this);
	}

	private void initTargetView() {
		targetLayout = findViewById(R.id.run_result_target_layout);
		targetImg = (ImageView) findViewById(R.id.run_result_target_icon);
		targetTitleTxt = (TextView) findViewById(R.id.run_result_target_title_txt);
		targetContentTxt = (TextView) findViewById(R.id.run_result_target_content_txt);
		runResultTxt = (TextView) findViewById(R.id.run_result_info_txt);
		runResultImg = (ImageView) findViewById(R.id.run_result_info_img);
		RunSetting setting = mRunResult.getRunSetting();
		if (setting == null || setting.getKind() == RunSetting.NORMAL) {
			targetLayout.setVisibility(View.GONE);
			return;
		} else {
			targetLayout.setVisibility(View.VISIBLE);
			if (mRunResult.getCompleteness() >= 1) {
				targetImg.setImageResource(R.drawable.ic_correct);
				runResultTxt.setText(R.string.run_result_success);
				runResultImg.setImageResource(R.drawable.run_result_success);
			} else {
				targetImg.setImageResource(R.drawable.ic_wrong);
				runResultTxt.setText(R.string.run_result_fail);
				runResultImg.setImageResource(R.drawable.run_result_fail);
			}
		}
		switch (setting.getKind()) {
		case RunSetting.DISTANCE:
			targetTitleTxt.setText(R.string.run_result_target_distance);
			targetContentTxt.setText(setting.getDistance() + "m");
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

	@Override
	public void onClick(View v) {
		if (v == toShareBtn) {
			this.finish();
		} else if (v == toHeartBtn) {
			this.finish();
		} else if (v == toMainBtn) {
			this.finish();
		}

	}

}
