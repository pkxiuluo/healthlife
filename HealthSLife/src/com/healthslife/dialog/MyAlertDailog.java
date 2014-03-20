package com.healthslife.dialog;

import android.app.Dialog;
import android.content.Context;

import com.healthslife.R;

public class MyAlertDailog extends Dialog {

	public MyAlertDailog(Context context) {
		this(context, R.style.fullScreenDialog);
	}

	private MyAlertDailog(Context context, int theme) {
		super(context, theme);
		init();
	}

	private void init() {
		setContentView(R.layout.dialog_count_down);
		setCancelable(false);
	}
	
	@Override
	public void setTitle(CharSequence title) {
//		super.setTitle(title);
	}
	
	
	
}
