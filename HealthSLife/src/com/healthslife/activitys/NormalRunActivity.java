package com.healthslife.activitys;

import java.text.DecimalFormat;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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

public class NormalRunActivity extends BaseFragmentActivity implements OnClickListener {
	public static final String RUN_SETTING = "runSetting";
	private ActionBar actionBar;
	private CountDownDialog dialog;
	private View root;
	private View panelLayout;

	private View btnLayout;
	private ImageView stopBtn;
	private TextView speedTxt;
	private TextView durationTxt;
	private TextView distanceTxt;

	private RunClient mClient;

	private DecimalFormat speedFormat = new DecimalFormat("##0.0");
	private DecimalFormat distanceFormat = new DecimalFormat("##0");;

	@Override
	protected void onCreate(Bundle arg0) {
		initView();

		mClient = new RunClient(this);
		mClient.init();
		mClient.setOnLocationChangeListener(new OnLocationChangeListener() {

			@Override
			public void onLocationChanged(DMLocation loation) {
				distanceTxt.setText(distanceFormat.format(mClient.getDistance()) + "m");
				String text = DateUtils.formatElapsedTime(mClient.getDuration() / 1000);
				durationTxt.setText(text);
				speedTxt.setText(speedFormat.format(loation.getSpeed()) + "m/s");
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
		}
	}

	private void initDialog() {
		dialog = new CountDownDialog(this);
		dialog.setDuratoin(3);
		dialog.show();
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				setViewVisibility(View.VISIBLE);
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
		}
		return super.onKeyDown(keyCode, event);
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

}
