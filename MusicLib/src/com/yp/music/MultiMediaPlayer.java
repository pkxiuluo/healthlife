package com.yp.music;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;

public class MultiMediaPlayer {

	private Context mContext;

	private MediaPlayer mCurrentMediaPlayer;
	private MediaPlayer mNextMediaPlayer;
	private StatusChangedListener mListener;

	private boolean mIsInitialized = false;

	public MultiMediaPlayer(Context context) {
		mContext = context;
		mCurrentMediaPlayer = new MediaPlayer();
		mCurrentMediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
	}

	public void setDataSource(String path) {
		mIsInitialized = setDataSourceImpl(mCurrentMediaPlayer, path);
		if (mIsInitialized) {
			setNextDataSource(null);
		}
	}

	private boolean setDataSourceImpl(MediaPlayer player, String path) {
		try {
			player.reset();
			player.setOnPreparedListener(null);
			if (path.startsWith("content://")) {
				player.setDataSource(mContext, Uri.parse(path));
			} else {
				player.setDataSource(path);
			}
			player.setAudioStreamType(AudioManager.STREAM_MUSIC);
			player.prepare();
		} catch (IOException ex) {
			return false;
		} catch (IllegalArgumentException ex) {
			return false;
		}
		player.setOnCompletionListener(completelistener);
		player.setOnErrorListener(errorListener);
		return true;
	}

	public void setNextDataSource(String path) {
		if (mNextMediaPlayer != null) {
			mNextMediaPlayer.release();
			mNextMediaPlayer = null;
		}
		if (path == null) {
			return;
		}
		mNextMediaPlayer = new MediaPlayer();
		mNextMediaPlayer.setWakeMode(mContext, PowerManager.PARTIAL_WAKE_LOCK);
		mNextMediaPlayer.setAudioSessionId(getAudioSessionId());
		if (setDataSourceImpl(mNextMediaPlayer, path)) {
		} else {
			// failed to open next, we'll transition the old fashioned way,
			// which will skip over the faulty file
			mNextMediaPlayer.release();
			mNextMediaPlayer = null;
		}
	}

	public boolean isInitialized() {
		return mIsInitialized;
	}

	public void start() {
		mCurrentMediaPlayer.start();
	}

	public void stop() {
		mCurrentMediaPlayer.reset();
		mIsInitialized = false;
	}

	/**
	 * You CANNOT use this player anymore after calling release()
	 */
	public void release() {
		stop();
		mCurrentMediaPlayer.release();
	}

	public void pause() {
		mCurrentMediaPlayer.pause();
	}

	/*
	 * public void setHandler(Handler handler) { mHandler = handler; }
	 */

	MediaPlayer.OnCompletionListener completelistener = new MediaPlayer.OnCompletionListener() {
		@Override
		public void onCompletion(MediaPlayer mp) {
			if (mp == mCurrentMediaPlayer && mNextMediaPlayer != null) {
				mCurrentMediaPlayer.release();
				mCurrentMediaPlayer = mNextMediaPlayer;
				mNextMediaPlayer = null;
				mCurrentMediaPlayer.start();
				mListener.onNextPlayed();
				/* mHandler.sendEmptyMessage(TRACK_WENT_TO_NEXT); */
			} else {
				mListener.onPlayEnd();
				// Acquire a temporary wakelock, since when we return from
				// this callback the MediaPlayer will release its wakelock
				// and allow the device to go to sleep.
				// This temporary wakelock is released when the RELEASE_WAKELOCK
				// message is processed, but just in case, put a timeout on it.

				/* mWakeLock.acquire(30000); */
				/*
				 * mHandler.sendEmptyMessage(TRACK_ENDED);
				 * mHandler.sendEmptyMessage(RELEASE_WAKELOCK);
				 */

			}

		}
	};

	MediaPlayer.OnErrorListener errorListener = new MediaPlayer.OnErrorListener() {
		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			switch (what) {
			case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
				mIsInitialized = false;
				mCurrentMediaPlayer.release();
				// Creating a new MediaPlayer and settings its wakemode
				// does not
				// require the media service, so it's OK to do this now,
				// while the
				// service is still being restarted
				mCurrentMediaPlayer = new MediaPlayer();
				mCurrentMediaPlayer.setWakeMode(mContext, PowerManager.PARTIAL_WAKE_LOCK);
				// mHandler.sendMessageDelayed(mHandler.obtainMessage(SERVER_DIED),
				// 2000);
				if(mListener!=null){
					mListener.onError();
				}
				return true;
			default:
				Log.d("MultiPlayer", "Error: " + what + "," + extra);
				break;
			}
			return false;
		}
	};

	public long duration() {
		return mCurrentMediaPlayer.getDuration();
	}

	public long position() {
		return mCurrentMediaPlayer.getCurrentPosition();
	}

	public long seek(long whereto) {
		mCurrentMediaPlayer.seekTo((int) whereto);
		return whereto;
	}

	public void setVolume(float vol) {
		mCurrentMediaPlayer.setVolume(vol, vol);
	}

	public void setAudioSessionId(int sessionId) {
		mCurrentMediaPlayer.setAudioSessionId(sessionId);
	}

	public int getAudioSessionId() {
		return mCurrentMediaPlayer.getAudioSessionId();
	}
	public void setStatusChangedListener(StatusChangedListener listener){
		this.mListener = listener;
	}
	public interface StatusChangedListener{
		public void onError();
		public void onNextPlayed();
		public void onPlayEnd();
	}
}
