package com.healthslife.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.healthslife.R;

public class MyAlertDialog extends Dialog implements android.view.View.OnClickListener {

	private View titleLayoutView;
	private TextView titleTxt;
	private TextView contentTxt;
	private Button positiveBtn;
	private Button negativeBtn;
	private OnClickListener positiveListener;
	private OnClickListener negativeListener;
	private ViewGroup contentViewGroup;

	public MyAlertDialog(Context context) {
		this(context, R.style.fullScreenDialog);
	}

	private MyAlertDialog(Context context, int theme) {
		super(context, theme);
		init();
	}

	private void init() {
		setContentView(R.layout.dialog_alert);
		titleLayoutView = findViewById(R.id.alert_title_layout);
		titleTxt = (TextView) findViewById(R.id.alert_title);
		contentTxt = (TextView) findViewById(R.id.alert_content);
		contentViewGroup = (ViewGroup) findViewById(R.id.alert_content_layout);
		positiveBtn = (Button) findViewById(R.id.alert_positive_btn);
		negativeBtn = (Button) findViewById(R.id.alert_negative_btn);
		positiveBtn.setOnClickListener(this);
		negativeBtn.setOnClickListener(this);
		setCancelable(false);
	}

	@Override
	public void setTitle(CharSequence title) {
		titleTxt.setText(title);
	}



	public void setContent(CharSequence content) {
		contentTxt.setText(content);
	}

	public void setNoTitle(boolean isNoTitle) {
		int visibility = isNoTitle ? View.GONE : View.VISIBLE;
		titleLayoutView.setVisibility(visibility);
	}

	public void setPositiveButtonVisibility(int visibility) {
		positiveBtn.setVisibility(visibility);
	}

	public void setNegativeButtonVisibility(int visibility) {
		negativeBtn.setVisibility(visibility);
	}

	public void setIcon(Drawable drawable) {
		titleTxt.setCompoundDrawables(drawable, null, null, null);
	}

	public void setIcon(int resId) {
		Drawable drawable = getContext().getResources().getDrawable(resId);
		titleTxt.setCompoundDrawables(drawable, null, null, null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.alert_positive_btn:
			if (this.positiveListener != null) {
				positiveListener.onClick(this, DialogInterface.BUTTON_POSITIVE);
			}
			break;
		case R.id.alert_negative_btn:
			if (this.negativeListener != null) {
				negativeListener.onClick(this, DialogInterface.BUTTON_NEGATIVE);
			}
			break;
		default:
			break;
		}
		this.dismiss();
	}

	public void setPositiveClickListener(OnClickListener listener) {
		this.positiveListener = listener;
	}

	public void setNegativeClickListener(OnClickListener listener) {
		this.negativeListener = listener;
	}

}
