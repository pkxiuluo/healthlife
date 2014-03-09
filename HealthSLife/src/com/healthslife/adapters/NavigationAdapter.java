package com.healthslife.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.healthslife.R;

public class NavigationAdapter extends BaseAdapter {
	private Context mContext;
	private int[] titlesResId = { R.string.navi_run, R.string.navi_health_test, R.string.navi_invite,
			R.string.navi_setting };

	public NavigationAdapter(Context context) {
		mContext = context;
	}

	@Override
	public int getCount() {
		return titlesResId.length;
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_navigation, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.navi_icon_img);
			holder.txt = (TextView) convertView.findViewById(R.id.navi_title_txt);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txt.setText(titlesResId[position]);
		return convertView;
	}

	private class ViewHolder {
		ImageView img;
		TextView txt;
	}

}
