package com.yp.music;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.yp.music.MultiMediaPlayer.StatusChangedListener;

import android.R.integer;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

public class ListMediaPlayer {
	private Context mContext;
	private MultiMediaPlayer mPlayer;
	private long[] mPlayList;
	private int mPlayListLen = 0;
	private LinkedList<Integer> mPlayHistoryPosList;
	private List<Integer> mFailPosList;
	private int mCurrentPosition;
	private int mNextPosition;

	private boolean isPlaying = false; // 是否正在播放，或者马上就要播放了（nextPlayer已经转换成currentPlayer，即将播放）

	public ListMediaPlayer(Context context) {
		mContext = context;
		mPlayer = new MultiMediaPlayer(context);
		mPlayer.setStatusChangedListener(statusChangedListener);
		Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE }, null, null, null);
		int count = cursor.getCount();
		if (count != 0) {
			mPlayList = new long[count];
			int i = 0;
			while (cursor.moveToNext()) {
				mPlayList[i] = cursor.getLong(0);
				i++;
			}
		}
	}

	public long[] getPlayList() {
		long[] list = Arrays.copyOf(mPlayList, mPlayList.length);
		return list;
	}

	public int getPlayListLength() {
		return mPlayListLen;
	}

	public void open(long[] playList) {
		if (playList != null) {
			mPlayList = playList;
			mPlayListLen = playList.length;
			mPlayHistoryPosList = new LinkedList<Integer>();
			mFailPosList = new ArrayList<Integer>(playList.length);
			mCurrentPosition = 0;
		}
	}

	public int getCurrentPosition() {
		return mCurrentPosition;
	}

	protected void setPlayPosition(int position) {
		if (position < 0) {
			position = 0;
		} else if (position >= mPlayListLen) {
			position = mPlayListLen - 1;
		}
		mCurrentPosition = position;
	}

	public void start() {
		prepareCurrentAndNext();
		if (mPlayer.isInitialized()) {
			mPlayer.start();
			isPlaying = true;
		}
	}

	public void goNext() {
		stop();
		mCurrentPosition = getNextPosition();
		start();
	}

	public void goLast() {
		stop();
		LinkedList<Integer> list =mPlayHistoryPosList;
		if (list.size() >= 2) {
			list.removeLast();
			mCurrentPosition = list.removeLast();
		} else if (list.size() == 1) {
			mCurrentPosition = list.removeLast();
		}
		start();
	}

	public void goPosition(int position) {
		clearHistoryPosList();
		stop();
		setPlayPosition(position);
		start();
	}

	public void stop() {
		if (mPlayer.isInitialized()) {
			mPlayer.stop();
			isPlaying = false;
		}
	}

	public void resume() {
		if (mPlayer.isInitialized()) {
			mPlayer.start();
			isPlaying = true;
		}
	}

	public void pause() {
		if (isPlaying) {
			mPlayer.pause();
			isPlaying = false;
		}
	}

	public long seek(long pos) {
		if (mPlayer.isInitialized()) {
			if (pos < 0)
				pos = 0;
			if (pos > mPlayer.duration())
				pos = mPlayer.duration();
			return mPlayer.seek(pos);
		}
		return -1;
	}

	protected void prepareCurrentAndNext() {
		while (!prepareCurrent()) {
			mFailPosList.add(mCurrentPosition);
			if (mFailPosList.size() >= mPlayListLen) {
				mCurrentPosition = 0;
				Toast.makeText(mContext, "无法打开文件", Toast.LENGTH_SHORT).show();
				return;
			}
			if (isExpectToBeContinue()) {
				mCurrentPosition = getNextPosition();
			}
		}
		if (isExpectToBeContinue()) {
			prepareNext();
		}
	}

	protected boolean prepareCurrent() {
		String path = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				mPlayList[mCurrentPosition]).toString();
		addCurrentToHistoryList();
		mPlayer.setDataSource(path);
		if (mPlayer.isInitialized()) {
			return true;
		}
		return false;
	}

	protected void prepareNext() {
		mNextPosition = getNextPosition();
		String path = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mPlayList[mNextPosition])
				.toString();
		mPlayer.setNextDataSource(path);
	}

	// 获取下一首歌的位置，子类可以根据播放策略不同，来重写
	protected int getNextPosition() {
		int position = mCurrentPosition;
		int result = 0;
		if (position >= mPlayListLen) {
			position = 0;
		} else {
			int temp = position + 1;
			if (temp >= mPlayListLen) {
				result = 0;
			} else {
				result = temp;
			}
		}
		return result;
	}

	/**
	 * 是否需要继续播放下去，比如播放列表播放完毕就期望不再播放了。
	 * 
	 * @return
	 */
	protected boolean isExpectToBeContinue() {
		if (mPlayHistoryPosList.size() >= mPlayListLen) {
			return false;
		}
		return true;
	}

	public List<Integer> getPlayHistoryPosList() {
		return mPlayHistoryPosList;
	}

	public void addCurrentToHistoryList() {
		mPlayHistoryPosList.add(mCurrentPosition);
	}

	public void clearHistoryPosList() {
		mPlayHistoryPosList.clear();
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	private StatusChangedListener statusChangedListener = new StatusChangedListener() {
		@Override
		public void onPlayEnd() {
			if (isExpectToBeContinue()) {
				start();
			} else {
				isPlaying = false;
			}
		}

		@Override
		public void onNextPlayed() {
			mCurrentPosition = mNextPosition;
			addCurrentToHistoryList();
			if (isExpectToBeContinue()) {
				prepareNext();
			} else {
			}
		}

		@Override
		public void onError() {
			isPlaying = false;
		}
	};

	protected void notifyChanged(String action) {

	}
}
