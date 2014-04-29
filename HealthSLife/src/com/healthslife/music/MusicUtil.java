package com.healthslife.music;

import java.util.ArrayList;

import android.content.Context;

import com.yp.music.SmartMediaPlayer;

public class MusicUtil {

	private static SmartMediaPlayer mediaPlayer;
	private static ArrayList<MusicInfo> musicInfos = new ArrayList<MusicInfo>();
	private static long[] playList;

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

}
