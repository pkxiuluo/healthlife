package com.healthslife.music;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Media;

public class MusicInfo {

	public static final String[] columnNames = new String[] { Media._ID, Media.TITLE, Media.DISPLAY_NAME, Media.ALBUM,
			Media.ARTIST, Media.DURATION };
	
	private static  final String UNKNOWN ="\"<unknown>\"";

	private long id;
	private String title;
	private String displayName;
	private String albumName;
	private String artistName;
	private long duration;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getName() {
		if (title != null) {
			return title;
		} else if (displayName != null) {
			return displayName;
		}
		return "";
	}

	public void initialize(Cursor cursor) {
		int index = cursor.getColumnIndex(Media._ID);
		if (index >= 0) {
			setId(cursor.getLong(index));
		}
		index = cursor.getColumnIndex(Media.TITLE);
		if (index >= 0) {
			setTitle(cursor.getString(index));
		}
		index = cursor.getColumnIndex(Media.DISPLAY_NAME);
		if (index >= 0) {
			setDisplayName(cursor.getString(index));
		}
		index = cursor.getColumnIndex(Media.ALBUM);
		if (index >= 0) {
			setAlbumName(cursor.getString(index));
		}
		index = cursor.getColumnIndex(Media.ARTIST);
		if (index >= 0) {
			setArtistName(cursor.getString(index));
		}
		index = cursor.getColumnIndex(Media.DURATION);
		if (index >= 0) {
			setDuration(cursor.getLong(index));
		}
	}
	public static List<MusicInfo> getMusicInfoList(Context context) {
		List<MusicInfo> infoList = new ArrayList<MusicInfo>();
		ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query(Media.EXTERNAL_CONTENT_URI, columnNames,Media.ARTIST +" <> "+UNKNOWN , null, Media.DEFAULT_SORT_ORDER);
		if(cursor!=null){
			while (cursor.moveToNext()) {
				MusicInfo info = new MusicInfo();
				info.initialize(cursor);
				infoList.add(info);
			}
		}
		return infoList;
	}


}
