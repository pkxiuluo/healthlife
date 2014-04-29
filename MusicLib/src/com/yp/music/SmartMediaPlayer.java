package com.yp.music;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.Intent;

public class SmartMediaPlayer extends ListMediaPlayer {

	public static final String ACTION_REPEAT_MODE_CHANGED = "ACTION_REPEAT_MODE_CHANGED";
	public static final String ACTION_SHUFFLE_MODE_CHANGED = "ACTION_SHUFFLE_MODE_CHANGED";

	public static final String STATE_REPEAT_MODE = "STATE_REPEAT_MODE";
	public static final String STATE_SHUFFLE_MODE = "STATE_SHUFFLE_MODE";

	public static final String CONTROL_REPEAT_NOCE = "repeat_noce";// 单曲一次
	public static final String CONTROL_REPEAT_CURRENT = "repeat_current ";// 单曲循环
	public static final String CONTROL_REPEAT_ALL_ONCE = "repeat_all_once";// 列表一次
	public static final String CONTROL_REPEAT_ALL ="repeat_all";//列表循环
	public static final String CONTROL_SHUFFLE_NONE = "shuffle_none";// 不随机
	public static final String CONTROL_SHUFFLE_NORMAL = "shuffle_normal";// 随机

	private String mRepeatMode = CONTROL_REPEAT_ALL_ONCE;
	private String mShuffleMode = CONTROL_SHUFFLE_NONE;

	private int[] mShufflePosition;

	public SmartMediaPlayer(Context context) {
		super(context);
	}

	public void start(String repeat, String shuffle) {
		setPlayMode(repeat, shuffle);
		super.start();
	}

	public void setPlayMode(String repeat, String shuffle) {
		if (setRepeatMode(repeat) || setShuffleMode(shuffle)) {
			clearHistoryPosList();
			if (isPlaying()) {
				addCurrentToHistoryList();
				prepareNext();
			}
		}
	}

	private boolean setRepeatMode(String repeat) {
		if (mRepeatMode.equals(repeat)) {
			return false;
		} else {
			mRepeatMode = repeat;
			notifyChanged(ACTION_REPEAT_MODE_CHANGED);
			return true;
		}
	}

	private boolean setShuffleMode(String shuffle) {
		if (mShuffleMode.equals(shuffle)) {
			return false;
		} else {
			// 初始化随机数组
			mShuffleMode = shuffle;
			List<Integer> shuffleList = new ArrayList<Integer>();
			for (int i = 0; i < getPlayListLength(); i++) {
				shuffleList.add(i);
			}
			Collections.shuffle(shuffleList);
			mShufflePosition = new int[getPlayListLength()];
			int i = 0;
			for (Integer pos : shuffleList) {
				mShufflePosition[i] = pos.intValue();
				i++;
			}
			notifyChanged(ACTION_REPEAT_MODE_CHANGED);
			return true;
		}
	}

	@Override
	protected int getNextPosition() {
		if (mRepeatMode.equals(CONTROL_REPEAT_NOCE)) {
			return getCurrentPosition();
		} else if (mRepeatMode.equals(CONTROL_REPEAT_CURRENT)) {
			return getCurrentPosition();
		} else if (mRepeatMode.equals(CONTROL_REPEAT_ALL_ONCE)||mRepeatMode.equals(CONTROL_REPEAT_ALL)) {
			if (mShuffleMode.equals(CONTROL_SHUFFLE_NONE)) {
				return super.getNextPosition();
			} else if (mShuffleMode.equals(CONTROL_SHUFFLE_NORMAL)) {
				int pos = 0;
				for (int i = 0; i < mShufflePosition.length; i++) {
					if (mShufflePosition[i] == getCurrentPosition()) {
						if (i + 1 < mShufflePosition.length) {
							pos = mShufflePosition[i + 1];
						} else {
							pos = mShufflePosition[0];
						}
						return pos;
					}
				}
				return pos;
			}
		}
		return super.getNextPosition();
	}

	@Override
	protected boolean isExpectToBeContinue() {
		if (mRepeatMode.equals(CONTROL_REPEAT_NOCE)) {
			if (getPlayHistoryPosList().size() >= 1)
				return false;
		} else if (mRepeatMode.equals(CONTROL_REPEAT_CURRENT)) {
			return true;
		} else if (mRepeatMode.equals(CONTROL_REPEAT_ALL_ONCE)) {
			if (getPlayHistoryPosList().size() >= getPlayListLength()) {
				return false;
			} else {
				return true;
			}
		}else if(mRepeatMode.equals(CONTROL_REPEAT_ALL)){
			return true;
		}
		return super.isExpectToBeContinue();
	}

	@Override
	protected Intent onCreateNotifyInfo(String action) {
		Intent intent = super.onCreateNotifyInfo(action);
		intent.putExtra(STATE_REPEAT_MODE, mRepeatMode);
		intent.putExtra(STATE_SHUFFLE_MODE, mShuffleMode);
		return intent;
	}

}
