package com.healthslife.dialog;

import android.app.Dialog;
import android.content.Context;

import com.healthslife.R;

public class HttpLoadingDialog extends Dialog {
	public HttpLoadingDialog(Context context) {
		this(context, 0);

	}

	private HttpLoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	private HttpLoadingDialog(Context context, int theme) {
		super(context, theme);
		init();
	}

	private void init() {
		setContentView(R.layout.dialog_http_loading);
		setCanceledOnTouchOutside(false);
	}

}
