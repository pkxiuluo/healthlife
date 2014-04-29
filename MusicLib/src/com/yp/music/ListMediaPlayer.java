package com.yp.music;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.yp.music.MultiMediaPlayer.StatusChangedListener;

import android.R.integer;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

public class ListMediaPlayer {

	public static final String ACTION_PLAYSTATE_CHANGED = "PLAYSTATE_CHANGED";
	public static final String ACTION_META_CHANGED = "META_CHANGED";
	public static final String STATE_IS_PLAYING = "isPlaying";

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
			notifyChanged(ACTION_META_CHANGED);
			notifyChanged(ACTION_PLAYSTATE_CHANGED);
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
		if(mPlayList==null||mPlayList.length==0)
			return;
		if (mPlayer.isInitialized()) {
			mPlayer.start();
			setPlaying(true);
		} else {
			prepareCurrentAndNext();
			if (mPlayer.isInitialized()) {
				mPlayer.start();
				setPlaying(true);
			}
		}
	}

	public void goNext() {
		stop();
		mCurrentPosition = getNextPosition();
		start();
	}

	public void goLast() {
		stop();
		LinkedList<Integer> list = mPlayHistoryPosList;
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
			setPlaying(false);
		}
	}

	public void pause() {
		if (isPlaying) {
			mPlayer.pause();
			setPlaying(false);
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

	private void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
		notifyChanged(ACTION_PLAYSTATE_CHANGED);
	}

	private StatusChangedListener statusChangedListener = new StatusChangedListener() {
		@Override
		public void onPlayEnd() {
			if (isExpectToBeContinue()) {
				start();
			} else {
				setPlaying(false);
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
			setPlaying(false);
		}
	};

	public long getPlayTime() {
		if (mPlayer.isInitialized()) {
			return mPlayer.position();
		}
		return -1;
	}

	public long cacheAudioId = -1;
	public String cacheTitle = "";
	public String cacheArtist = "";

	public long getAudioId() {
		return mPlayList[mCurrentPosition];
	}

	public String getArtistName() {
		// mContext.getContentResolver().query(uri, projection, selection,
		// selectionArgs, sortOrder)
		return "";
	}

	protected Intent onCreateNotifyInfo(String action) {
		Intent intent = new Intent(action);
		intent.putExtra(STATE_IS_PLAYING, isPlaying);
		return intent;
	}

	protected void notifyChanged(String action) {
		mContext.sendStickyBroadcast(onCreateNotifyInfo(action));
	}
	
	@Override
	protected void finalize() throws Throwable {
//		mContext.removeStickyBroadcast(intent)
		super.finalize();
	}
}
