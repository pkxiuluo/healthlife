package com.healthslife.adapters;

import java.text.DecimalFormat;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.healthslife.R;
import com.healthslife.run.dao.RunResult;

public class RunDataAdapter extends BaseAdapter {
	private Context mContext;
	private RunResult mResult;
	private String[] titleStrings;
	private DecimalFormat speedFormat = new DecimalFormat("##0.0");
	private DecimalFormat distanceFormat = new DecimalFormat("##0");

	public RunDataAdapter(Context context, RunResult result) {
		mContext = context;
		mResult = result;
		titleStrings = context.getResources().getStringArray(R.array.run_data_title);
	}

	@Override
	public int getCount() {
		return titleStrings.length;
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_run_data, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.run_data_title_txt);
			holder.data = (TextView) convertView.findViewById(R.id.run_data_data_txt);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setText(titleStrings[position]);
		switch (position) {
		case 0:
			holder.data.setText(distanceFormat.format(mResult.getDistance()) + "m");
			break;
		case 1:
			holder.data.setText(DateUtils.formatElapsedTime(mResult.getDuration() / 1000));
			break;
		case 2:
			float averageSpeed = mResult.getDistance() * 1000 / mResult.getDuration();
			holder.data.setText(speedFormat.format(averageSpeed) + "m/s");
			break;
		case 3:
			holder.data.setText(0 + "cal");
			break;

		default:
			break;
		}
		return convertView;
	}

	private class ViewHolder {
		TextView title;
		TextView data;
	}

}
