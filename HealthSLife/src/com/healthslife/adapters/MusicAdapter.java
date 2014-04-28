package com.healthslife.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.healthslife.R;
import com.healthslife.music.MusicInfo;

public class MusicAdapter extends BaseAdapter implements SectionIndexer {
	private Context mContext;
	private ArrayList<MusicInfo> musicInfos = new ArrayList<MusicInfo>();

	public MusicAdapter(Context context) {
		mContext = context;
		musicInfos.addAll(MusicInfo.getMusicInfoList(context));
		/*
		 * for(MusicInfo info :musicInfos){
		 * System.out.println(info.getTitle()+"  "
		 * +info.getDisplayName()+"  "+info.getAlbumName()); }
		 */
	}

	@Override
	public int getCount() {
		return musicInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_music_info, null);
			holder = new ViewHolder();
			holder.titleTextView = (TextView) convertView.findViewById(R.id.item_music_info_title);
			holder.artistTextView = (TextView) convertView.findViewById(R.id.item_music_info_artist);
			holder.albumTextView = (TextView) convertView.findViewById(R.id.item_music_info_album);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MusicInfo info = musicInfos.get(position);
		holder.titleTextView.setText(info.getName());
		holder.artistTextView.setText(info.getArtistName());
		holder.albumTextView.setText("  —  " + info.getAlbumName());
		return convertView;
	}

	private class ViewHolder {
		TextView titleTextView;
		TextView artistTextView;
		TextView albumTextView;
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	@Override
	public int getPositionForSection(int section) {
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}

}
