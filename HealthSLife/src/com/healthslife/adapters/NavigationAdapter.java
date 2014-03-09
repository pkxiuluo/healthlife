package com.healthslife.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.healthslife.R;

public class NavigationAdapter extends BaseAdapter {
	private Context mContext;
	private List<DataHolder> mDataList;

	public NavigationAdapter(Context context , List<DataHolder> dataList) {
		mContext = context;
		mDataList= dataList;
	}

	@Override
	public int getCount() {
		return mDataList.size();
	}

	@Override
	public Object getItem(int position) {
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_navigation, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.navi_icon_img);
			holder.txt = (TextView) convertView.findViewById(R.id.navi_title_txt);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.img.setImageDrawable(mDataList.get(position).icon);
		holder.txt.setText(mDataList.get(position).title);
		return convertView;
	}

	private class ViewHolder {
		ImageView img;
		TextView txt;
	}
	public static class DataHolder{
	 public	Drawable icon;
	 public String title;
	}

}
