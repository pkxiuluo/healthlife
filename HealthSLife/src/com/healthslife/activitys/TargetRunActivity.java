package com.healthslife.activitys;

import java.text.DecimalFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.dm.location.DMLocation;
import com.dm.location.DMLocationClient.OnLocationChangeListener;
import com.healthslife.BaseFragmentActivity;
import com.healthslife.R;
import com.healthslife.dialog.CountDownDialog;
import com.healthslife.run.TargetedRunClient;
import com.healthslife.run.TargetedRunClient.OnStatusChangedListener;
import com.healthslife.run.dao.RunResult;
import com.healthslife.run.dao.RunSetting;
import com.healthslife.run.widget.CircleProgressBar;

public class TargetRunActivity extends BaseFragmentActivity implements OnClickListener {
	private ActionBar actionBar;
	public static final String EXTRA_RUN_SETTING = "runSetting";
	private RunSetting mRunSetting;
	private TargetedRunClient mClient;
	private CountDownDialog dialog;
	private View panelLayout;
	private View btnLayout;
	private TextView durationTxt;
	private ImageView stopBtn;
	private TextView speedTxt;
	private CircleProgressBar progressBar;

	private DecimalFormat speedFormat = new DecimalFormat("##0.0");
	private DecimalFormat distanceFormat = new DecimalFormat("##0");

	private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
	private int duration;
	private Future<?> future;

	@Override
	protected void onCreate(Bundle arg0) {
		mRunSetting = (RunSetting) getIntent().getSerializableExtra(EXTRA_RUN_SETTING);
		if (mRunSetting == null) {
			this.finish();
			return;
		}
		super.onCreate(arg0);

		initView();
		mClient = new TargetedRunClient(this, mRunSetting);
		mClient.init();
		mClient.setOnLocationChangeListener(new OnLocationChangeListener() {

			@Override
			public void onLocationChanged(DMLocation loation) {
				speedTxt.setText(speedFormat.format(loation.getSpeed()) + "m/s");
				progressBar.setProgress((int) (mClient.getCompleteness() * 100));
			}
		});
		mClient.setOnStatusChangedListener(new OnStatusChangedListener() {
			@Override
			public void onCompletenessChanged(float completeness) {
				progressBar.setProgress((int) (completeness * 100));
			}

			@Override
			public void onTargetFinish(RunResult result) {
				mClient.stop();
				Intent intent = new Intent(TargetRunActivity.this, RunResultActivity.class);
				intent.putExtra(RunResultActivity.EXTRA_RUN_RESULT, result);
				startActivity(intent);
			}
		});
	}

	private void initView() {
		setActionBar();
		setContentView(R.layout.activity_run_target);
		panelLayout = findViewById(R.id.run_target_panel_layout);
		btnLayout = findViewById(R.id.run_target_btn_layout);
		durationTxt = (TextView) findViewById(R.id.run_target_duration_txt);
		stopBtn = (ImageView) findViewById(R.id.run_stop_btn);
		speedTxt = (TextView) findViewById(R.id.run_target_speed_txt);
		progressBar = (CircleProgressBar) findViewById(R.id.run_target_progress_bar);
		stopBtn.setOnClickListener(this);
		setViewVisibility(View.INVISIBLE);
		initDialog();
	}

	private void initDialog() {
		dialog = new CountDownDialog(this);
		dialog.setDuratoin(1);
		dialog.show();
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				setViewVisibility(View.VISIBLE);
				future = service.scheduleAtFixedRate(new Runnable() {

					@Override
					public void run() {
						mHandler.sendEmptyMessage(duration);
						duration++;
					}
				}, 0, 1000, TimeUnit.MILLISECONDS);
				mClient.start();
			}
		});
	}

	private void setViewVisibility(int visibility) {
		panelLayout.setVisibility(visibility);
		btnLayout.setVisibility(visibility);
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

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String text = DateUtils.formatElapsedTime(msg.what);
			durationTxt.setText(text);
		};
	};

	@Override
	public void onClick(View v) {
		if (v == stopBtn) {
			mClient.stop();
			RunResult result = new RunResult();
			result.setRunSetting(mRunSetting);
			result.setDistance(mClient.getDistance());
			result.setDuration(mClient.getDuration());
			Intent intent = new Intent(this, RunResultActivity.class);
			intent.putExtra(RunResultActivity.EXTRA_RUN_RESULT, result);
			startActivity(intent);
			this.finish();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			mClient.stop();
			this.finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			mClient.stop();
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onStop() {
		mClient.stop();
		super.onStop();
	}

	@Override
	public void finish() {
		super.finish();
		if (future != null && future.isCancelled() == false) {
			future.cancel(true);
		}
	};
}
