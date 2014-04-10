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
import android.view.LayoutInflater;
import android.view.Menu;
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
import com.healthslife.run.RunClient;
import com.healthslife.run.dao.RunResult;
import com.healthslife.run.dao.RunSetting;

public class NormalRunActivity extends BaseFragmentActivity implements OnClickListener {

	public static final String EXTRA_RUN_SETTING = "runSetting";
	private ActionBar actionBar;
	private CountDownDialog dialog;
	private View root;
	private View panelLayout;

	private View btnLayout;
	private ImageView stopBtn;
	private TextView speedTxt;
	private TextView durationTxt;
	private TextView distanceTxt;
	private TextView calorieTxt;

	private RunClient mClient;

	private DecimalFormat speedFormat = new DecimalFormat("##0.0");
	private DecimalFormat distanceFormat = new DecimalFormat("##0");

	private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
	private int duration;
	private Future<?> future;

	@Override
	protected void onCreate(Bundle arg0) {
		initView();

		mClient = new RunClient(this);
		mClient.init();
		mClient.setOnLocationChangeListener(new OnLocationChangeListener() {

			@Override
			public void onLocationChanged(DMLocation loation) {
				distanceTxt.setText(distanceFormat.format(mClient.getDistance()) + "m");
				// String text =
				// DateUtils.formatElapsedTime(mClient.getDuration() / 1000);
				// durationTxt.setText(text);
				speedTxt.setText(speedFormat.format(loation.getSpeed()) + "m/s");
				calorieTxt.setText(mClient.getCalorie() + "cal");
			}
		});
		super.onCreate(arg0);
	}

	private void initView() {
		root = LayoutInflater.from(this).inflate(R.layout.activity_run_normal, null);
		panelLayout = root.findViewById(R.id.run_normal_panel);
		btnLayout = root.findViewById(R.id.run_normal_btn_layout);
		stopBtn = (ImageView) root.findViewById(R.id.run_stop_btn);
		speedTxt = (TextView) root.findViewById(R.id.run_speed_txt);
		durationTxt = (TextView) root.findViewById(R.id.run_duration_txt);
		distanceTxt = (TextView) root.findViewById(R.id.run_distance_txt);
		calorieTxt = (TextView) root.findViewById(R.id.run_cal_txt);
		stopBtn.setOnClickListener(this);

		setViewVisibility(View.INVISIBLE);
		setContentView(root);
		setActionBar();
		initDialog();
	}

	@Override
	public void onClick(View v) {
		if (v == stopBtn) {
			mClient.stop();
			RunResult result = new RunResult();
			result.setRunSetting(new RunSetting());
			result.setDistance(mClient.getDistance());
			result.setDuration(mClient.getDuration());
			result.setCalorie(mClient.getCalorie());
			Intent intent = new Intent(this, RunResultActivity.class);
			intent.putExtra(RunResultActivity.EXTRA_RUN_RESULT, result);
			startActivity(intent);
			this.finish();
		}
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
		actionBar.setTitle(R.string.run_normal);
		actionBar.setDisplayHomeAsUpEnabled(true);
		// actionBar.setLogo(R.drawable.navi_run_);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.run, menu);
		return super.onCreateOptionsMenu(menu);
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

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String text = DateUtils.formatElapsedTime(msg.what);
			durationTxt.setText(text);
		};
	};

	@Override
	public void finish() {
		super.finish();
		if (future != null && future.isCancelled() == false) {
			future.cancel(true);
		}
	};

}
