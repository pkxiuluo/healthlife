package com.healthslife.activitys;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.healthslife.BaseFragmentActivity;
import com.healthslife.R;
import com.healthslife.dialog.CountDownDialog;

public class NormalRunActivity extends BaseFragmentActivity {
	public static final String RUN_SETTING = "runSetting";
	private ActionBar actionBar;
	private CountDownDialog dialog;
	private View root;
	private View panelLayout;
	private View btnLayout;

	@Override
	protected void onCreate(Bundle arg0) {
		root = LayoutInflater.from(this).inflate(R.layout.activity_run_normal, null);
		panelLayout = root.findViewById(R.id.run_normal_panel);
		btnLayout = root.findViewById(R.id.run_normal_btn_layout);
		setViewVisibility(View.INVISIBLE);
		setContentView(root);
		setActionBar();
		initDialog();
		super.onCreate(arg0);
	}

	private void initDialog() {
		dialog = new CountDownDialog(this);
		dialog.show();
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				setViewVisibility(View.VISIBLE);
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
