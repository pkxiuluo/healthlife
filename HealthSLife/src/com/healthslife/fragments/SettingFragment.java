package com.healthslife.fragments;

import com.healthslife.R;
import com.healthslife.dialog.MyAlertDailog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public class SettingFragment extends Fragment {

	private View aboutUsView;
	private View checkUpdateView;
	private View countDownTimeView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting, null);
		aboutUsView = view.findViewById(R.id.setting_about_us_layout);
		checkUpdateView = view.findViewById(R.id.setting_check_update_layout);
		countDownTimeView = view.findViewById(R.id.setting_count_down_layout);
		setView();
		return view;
	}
	private void setView(){
		aboutUsView.setOnClickListener(myOncClickListener);
		checkUpdateView.setOnClickListener(myOncClickListener);
		countDownTimeView.setOnClickListener(myOncClickListener);
	}
	
	public OnClickListener myOncClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(checkUpdateView==v){
				MyAlertDailog dailog = new MyAlertDailog(SettingFragment.this.getActivity());
				dailog.setTitle("检查版本更新");
				dailog.setContent("该版本已经是最新版本,无需更新");
				dailog.setCancelable(true);
//				dailog.setPositiveButtonVisibility(View.GONE);
				dailog.setNegativeButtonVisibility(View.GONE);
				dailog.show();
			}else if(aboutUsView ==v){
				MyAlertDailog dailog = new MyAlertDailog(SettingFragment.this.getActivity());
				dailog.setTitle("关于我们");
				dailog.setContent("来自hust的，王旭、李晗、闫鹏、李舜民、吕剑、高雪沁");
				dailog.setCancelable(true);
				dailog.setPositiveButtonVisibility(View.GONE);
				dailog.setNegativeButtonVisibility(View.GONE);
				dailog.show();
			}else if(countDownTimeView ==v){
				MyAlertDailog dailog = new MyAlertDailog(SettingFragment.this.getActivity());
				dailog.setNoTitle(true);
				dailog.setCancelable(true);
				View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_set_count_down, null);
				dailog.setContentView(view);
				dailog.setPositiveButtonVisibility(View.GONE);
				dailog.setNegativeButtonVisibility(View.GONE);
				dailog.show();
			}
			
		}
	};

}
