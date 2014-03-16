package com.healthslife.activitys;

import javax.xml.datatype.Duration;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.TextureView;
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
	private RunClient mClient;
	private TextView speedTxt;

	@Override
	protected void onCreate(Bundle arg0) {
		root = LayoutInflater.from(this).inflate(R.layout.activity_run_normal, null);
		panelLayout = root.findViewById(R.id.run_normal_panel);
		btnLayout = root.findViewById(R.id.run_normal_btn_layout);
		stopBtn = (ImageView) root.findViewById(R.id.run_stop_btn);
		speedTxt = (TextView) root.findViewById(R.id.run_speed_txt);
		stopBtn.setOnClickListener(this);

		setViewVisibility(View.INVISIBLE);
		setContentView(root);
		setActionBar();
		initDialog();

		mClient = new RunClient(this);
		mClient.init();
		mClient.setOnLocationChangeListener(new OnLocationChangeListener() {

			@Override
			public void onLocationChanged(DMLocation loation) {
				// System.out.println(loation.getLatitude());
				speedTxt.setText(loation.getSpeed() + "m/s");
			}
		});
		super.onCreate(arg0);
	}

	@Override
	public void onClick(View v) {
		if (v == stopBtn) {
			mClient.stop();
		}
	}

	private void initDialog() {
		dialog = new CountDownDialog(this);
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
