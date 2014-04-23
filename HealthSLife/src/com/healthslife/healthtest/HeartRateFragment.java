package com.healthslife.healthtest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.healthslife.R;
import com.healthslife.widget.CircleProgress;

public class HeartRateFragment extends Fragment implements View.OnClickListener {
	private static final int BEFORTEST = 0;
	private static final int TESTING = 1;
	private static final int AFTERTEST = 2;

	private static final int NORMAL = 1;
	private static final int AFTERSPORT = 2;

	private Button normalStateBtn;
	private Button afterSportBtn;
	private CircleProgress mCircleProgress;
	private TextView heartRateTxt;
	private TextView tipsTxt;
	private TextView lastTestState;
	private TextView lastTestTime;
	private TextView lastTestRate;
	private int state = NORMAL;// 1代表静息状态即平常态 2运动后…………
	private int testState = BEFORTEST;// 0测试开始前 1测试中 2测试结束

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_heart_rate, container,
				false);
		normalStateBtn = (Button) root.findViewById(R.id.normal_state_btn);
		normalStateBtn.setOnClickListener(this);
		afterSportBtn = (Button) root.findViewById(R.id.after_sports_state);
		afterSportBtn.setOnClickListener(this);
		mCircleProgress = (CircleProgress) root
				.findViewById(R.id.heart_rate_porbar);
		mCircleProgress.setOnClickListener(this);
		heartRateTxt = (TextView) root.findViewById(R.id.heart_rate_txt);
		tipsTxt = (TextView) root.findViewById(R.id.heart_rate_tips);
		lastTestState = (TextView) root
				.findViewById(R.id.heart_rate_last_state);
		lastTestTime = (TextView) root.findViewById(R.id.heart_rate_last_time);
		lastTestRate = (TextView) root.findViewById(R.id.heart_rate_last_rate);
		return root;
	}

	@Override
	public void onClick(View v) {
		Log.v("heart rate", "click");
		switch (v.getId()) {
		case R.id.normal_state_btn:
			normalStateBtnClick();
			break;
		case R.id.after_sports_state:
			afterSportBtnClick();
			break;
		case R.id.heart_rate_porbar:
			startTest();
			break;
		default:
			;
			break;
		}
	}

	private void startTest() {
		if (testState != BEFORTEST)
			return;
		//开始测量心率
		
	}

	private void afterSportBtnClick() {
		if (testState != BEFORTEST)
			return;
		state = AFTERSPORT;
		afterSportBtn
				.setBackgroundResource(R.drawable.heart_rate_top_right_btn_hover);
		normalStateBtn
				.setBackgroundResource(R.drawable.heart_rate_top_left_btn);
	}

	private void normalStateBtnClick() {
		if (testState != BEFORTEST)
			return;
		state = NORMAL;
		afterSportBtn
				.setBackgroundResource(R.drawable.heart_rate_top_right_btn);
		normalStateBtn
				.setBackgroundResource(R.drawable.heart_rate_top_left_btn_hover);
	}

}
