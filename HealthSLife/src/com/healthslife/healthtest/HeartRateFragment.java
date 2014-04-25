package com.healthslife.healthtest;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.healthslife.R;
import com.healthslife.healthtest.ImgAnalysis.ImgCaptureListener;
import com.healthslife.widget.CircleProgress;

public class HeartRateFragment extends Fragment implements View.OnClickListener {
	private static final int BEFORTEST = 0;
	private static final int TESTING = 1;
	private static final int AFTERTEST = 2;

	private static final int NORMAL = 1;
	private static final int AFTERSPORT = 2;
	private static final int UPPERBOUND = 99999999;
	private static final int LOWERBOUND = 0;

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
	private ImgAnalysis mImgAnalysis;
	private Timer mTimer;
	private ArrayList<dataAndTime> dataList = new ArrayList<dataAndTime>();
	private int wrongDataNum = 0;
	private int peakPairs[][] = new int[15][2];// 峰值对 每对表示相邻的两个峰值在dataList中的位置
	private int peakPairsIndex = 0;
	// private long timeMill = 0;//计时器 毫秒
	private int lastPeakIndex = 0;
	private int averageHeartRate = 0;

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
		mImgAnalysis = new ImgAnalysis(this.getActivity());
		mImgAnalysis.setImgCaptureListener(mImgCaptureListener);
		mTimer = new Timer();
		return root;
	}

	private ImgCaptureListener mImgCaptureListener = new ImgCaptureListener() {

		@Override
		public void onCapture(int[] rgb, int height, int width) {
			int total = 0;
			for (int i : rgb) {
				total += i;
			}
			// 判断灰度值是否在合理的范围内，如果在加入dataList，如果不在舍弃数据并且连续n组数据不符合进度置零
			if (total > UPPERBOUND || total < LOWERBOUND) {
				wrongDataNum++;
				if (wrongDataNum > 20)
					mCircleProgress.setProgress(0);
				return;
			}
			wrongDataNum = 0;
			dataList.add(new dataAndTime(total, mTimer.getMillis()));
			analysisData();
		}
	};

	private void analysisData() {
		// 找出peakpairs……
		if (mTimer.getSecond() < 2) {
			return;
		}
		int pairFirstPeak = findPeaks(lastPeakIndex);
		if (pairFirstPeak == -1) {
			return;
		}
		int pairSecondPeak = findPeaks(pairFirstPeak);
		if (pairSecondPeak == -1) {
			return;
		}
		lastPeakIndex = pairSecondPeak;
		peakPairs[peakPairsIndex][0] = pairFirstPeak;
		peakPairs[peakPairsIndex][1] = pairSecondPeak;
		peakPairsIndex++;
		computeHeartRate();
		mCircleProgress.slideToProgress(peakPairsIndex * 100);
		if (peakPairsIndex >= 10) {
			mImgAnalysis.stopCaptureImg();
			Toast.makeText(getActivity(), "test complete", Toast.LENGTH_SHORT).show();
		}
		Log.v("heart rate", peakPairsIndex + "");
	}
	private void computeHeartRate() {
		long totalTime = 0;
		for (int i = 0; i < peakPairsIndex; i++) {
			totalTime += dataList.get(peakPairs[i][1]).time
					- dataList.get(peakPairs[i][0]).time;
		}
		averageHeartRate = (int) ((60000 / totalTime) * peakPairsIndex);
		heartRateTxt.setText(averageHeartRate + "");
	}

	public int findPeaks(int start) {
		int N = 1;// 峰值间距
		for (int i = start + N; i < dataList.size() - N; i++) {
			boolean isPeak = true;
			for (int j = 0; j < N; j++) {
				if (dataList.get(i - j).data < dataList.get(i - j - 1).data
						|| dataList.get(i + j).data < dataList.get(i + j + 1).data) {
					isPeak = false;
				}
			}
			if (isPeak) {
				return i;
			}
		}
		return -1;
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
		// 开始测量心率
		testState = TESTING;
		mImgAnalysis.startCaptureImg();
		mTimer.startTimer();
	}

	private void afterSportBtnClick() {
		if (testState != BEFORTEST) {
			Toast.makeText(getActivity(), "请在心率识别前选择状态", Toast.LENGTH_SHORT);
			return;
		}
		state = AFTERSPORT;
		afterSportBtn
				.setBackgroundResource(R.drawable.heart_rate_top_right_btn_hover);
		normalStateBtn
				.setBackgroundResource(R.drawable.heart_rate_top_left_btn);
	}

	private void normalStateBtnClick() {
		if (testState != BEFORTEST) {
			Toast.makeText(getActivity(), "请在心率识别前选择状态", Toast.LENGTH_SHORT);
			return;
		}
		state = NORMAL;
		afterSportBtn
				.setBackgroundResource(R.drawable.heart_rate_top_right_btn);
		normalStateBtn
				.setBackgroundResource(R.drawable.heart_rate_top_left_btn_hover);
	}

	public class dataAndTime {
		public int data;
		public long time;

		public dataAndTime(int data, long time) {
			this.data = data;
			this.time = time;
		}

	}
}
