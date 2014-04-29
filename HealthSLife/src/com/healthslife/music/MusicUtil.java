package com.healthslife.music;

import java.util.ArrayList;

import android.content.Context;

import com.yp.music.SmartMediaPlayer;

public class MusicUtil {

	private static SmartMediaPlayer mediaPlayer;
	private static ArrayList<MusicInfo> musicInfos = new ArrayList<MusicInfo>();
	private static long[] playList;
	public static final String PLAY_MODE_LIST_REPEAT = "PLAY_MODE_LIST_REPEAT";
	public static final String PLAY_MODE_LIST_SHUFFLE = "PLAY_MODE_LIST_SHUFFLE";
	public static final String PLAY_MODE_REPEAT_CURRENT = "PLAY_MODE_REPEAT_CURRENT";

	public static void initPlayer(Context context) {
		mediaPlayer = new SmartMediaPlayer(context.getApplicationContext());
		musicInfos.addAll(MusicInfo.getMusicInfoList(context));
		if (musicInfos.size() > 0) {
			playList = new long[musicInfos.size()];
		}
		int i = 0;
		for (MusicInfo info : musicInfos) {
			playList[i] = info.getId();
			i++;
		}
		mediaPlayer.open(playList);
		setPlayMode(PLAY_MODE_LIST_REPEAT);
	}

	public static ArrayList<MusicInfo> getPlayList() {
		return musicInfos;
	}

	public static SmartMediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}

	public static void stop() {
		mediaPlayer.stop();
	}

	public static void start() {
		mediaPlayer.start();
	}

	public static void pause() {
		mediaPlayer.pause();
	}

	public static void goNext() {
		mediaPlayer.goNext();
	}

	public static void goLast() {
		mediaPlayer.goLast();
	}

	public static void setPlayMode(String mode) {
		if (mode == null) {
			return;
		}
		if (mode.equals(PLAY_MODE_REPEAT_CURRENT)) {
			mediaPlayer.setPlayMode(SmartMediaPlayer.CONTROL_REPEAT_CURRENT, SmartMediaPlayer.CONTROL_SHUFFLE_NONE);
		} else if (mode.equals(PLAY_MODE_LIST_REPEAT)) {
			mediaPlayer.setPlayMode(SmartMediaPlayer.CONTROL_REPEAT_ALL, SmartMediaPlayer.CONTROL_SHUFFLE_NONE);
		} else if (mode.equals(PLAY_MODE_LIST_SHUFFLE)) {
			mediaPlayer.setPlayMode(SmartMediaPlayer.CONTROL_REPEAT_ALL, SmartMediaPlayer.CONTROL_SHUFFLE_NORMAL);
		}
	}
	
	public static String covertToPlayMoe(String repeatMode,String shuffleMode){
		if(repeatMode==null||shuffleMode==null){
			return PLAY_MODE_LIST_REPEAT;
		}
		if(repeatMode.equals(SmartMediaPlayer.CONTROL_REPEAT_CURRENT)){
			return PLAY_MODE_REPEAT_CURRENT;
		}else if(repeatMode.equals(SmartMediaPlayer.CONTROL_REPEAT_ALL)){
			if(shuffleMode.equals(SmartMediaPlayer.CONTROL_SHUFFLE_NONE)){
				return PLAY_MODE_LIST_REPEAT;
			}else if(shuffleMode.equals(SmartMediaPlayer.CONTROL_SHUFFLE_NORMAL)){
				return PLAY_MODE_LIST_SHUFFLE;
			}
		}
		return PLAY_MODE_LIST_REPEAT;
	}

}
