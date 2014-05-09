package com.healthslife.fragments;

import com.healthslife.R;
import com.healthslife.dialog.MyAlertDialog;
import com.healthslife.setting.AppSetting;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SettingFragment extends Fragment {

	private View aboutUsView;
	private View checkUpdateView;
	private View countDownTimeView;

	private MyAlertDialog checkUpdatetDialog;
	private MyAlertDialog aboutUsDialog;
	private Dialog countDownTimeDialog;
	private View countDownBtn;
	private View countUpBtn;
	private TextView countDownTxt;
	private AppSetting mAppSetting;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting, null);
		aboutUsView = view.findViewById(R.id.setting_about_us_layout);
		checkUpdateView = view.findViewById(R.id.setting_check_update_layout);
		countDownTimeView = view.findViewById(R.id.setting_count_down_layout);
		mAppSetting = new AppSetting(getActivity());
		setView();
		initDialog();
		return view;
	}

	private void setView() {
		aboutUsView.setOnClickListener(myOnClickListener);
		checkUpdateView.setOnClickListener(myOnClickListener);
		countDownTimeView.setOnClickListener(myOnClickListener);
	}

	private void initDialog() {
		checkUpdatetDialog = new MyAlertDialog(SettingFragment.this.getActivity());
		checkUpdatetDialog.setTitle("检查版本更新");
		checkUpdatetDialog.setContent("该版本已经是最新版本,无需更新");
		checkUpdatetDialog.setCancelable(true);
		checkUpdatetDialog.setNegativeButtonVisibility(View.GONE);

		aboutUsDialog = new MyAlertDialog(SettingFragment.this.getActivity());
		aboutUsDialog.setTitle("关于我们");
		aboutUsDialog.setContent("来自hust的，王旭、李晗、闫鹏、李舜民、吕剑、高雪沁");
		aboutUsDialog.setCancelable(true);
		aboutUsDialog.setPositiveButtonVisibility(View.GONE);
		aboutUsDialog.setNegativeButtonVisibility(View.GONE);

		countDownTimeDialog = new Dialog(SettingFragment.this.getActivity(), R.style.fullScreenDialog);
		countDownTimeDialog.setCancelable(true);
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_set_count_down, null);
		countDownTimeDialog.setContentView(view);
		countDownBtn = view.findViewById(R.id.set_count_down_img);
		countUpBtn = view.findViewById(R.id.set_count_up_img);
		countDownBtn.setOnClickListener(countDownTimeBtnClick);
		countUpBtn.setOnClickListener(countDownTimeBtnClick);
		countDownTxt = (TextView) view.findViewById(R.id.set_count_down_time_txt);
	}

	public OnClickListener myOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (checkUpdateView == v) {
				checkUpdatetDialog.show();
			} else if (aboutUsView == v) {
				aboutUsDialog.show();
			} else if (countDownTimeView == v) {
				countDownTxt.setText(String.valueOf(mAppSetting.getCountDown()));
				countDownTimeDialog.show();
			}

		}
	};
	public OnClickListener countDownTimeBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String valueString = countDownTxt.getText().toString();
			int value;
			try {
				value = Integer.valueOf(valueString);
			} catch (NumberFormatException e) {
				value = 5;
			}
			if (v == countDownBtn) {
				if (value >= 2) {
					value--;
					countDownTxt.setText(String.valueOf(value));
					mAppSetting.setCountDown(value);
				}
			} else if (v == countUpBtn) {
				if (value <= 4) {
					value++;
					countDownTxt.setText(String.valueOf(value));
					mAppSetting.setCountDown(value);
				}
			}

		}
	};

}
