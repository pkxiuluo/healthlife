package com.healthslife.adapters;

import java.util.ArrayList;

import org.w3c.dom.Text;

import com.healthslife.R;
import com.healthslife.healthtest.dao.HeartRateDB;
import com.healthslife.healthtest.dao.HeartRateHisRecord;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

public class HeartRateHisListAdapter extends BaseAdapter {
	private Context mContext;
	private int count;
	private ArrayList<HeartRateHisRecord> hisDataList;

	public HeartRateHisListAdapter(Context mContext) {
		this.mContext = mContext;
		HeartRateDB mHeartRateDB = new HeartRateDB(mContext);
		hisDataList = (ArrayList<HeartRateHisRecord>) mHeartRateDB.queryAll();
		count = hisDataList.size();
	}

	@Override
	public int getCount() {
		return hisDataList.size();
	}

	@Override
	public Object getItem(int position) {
		return hisDataList.get(count - position - 1);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_heart_rate_history, null);
			holder = new ViewHolder();
			holder.state = (TextView) convertView
					.findViewById(R.id.heart_rate_state);
			holder.time = (TextView) convertView
					.findViewById(R.id.heart_rate_time);
			holder.rate = (TextView) convertView
					.findViewById(R.id.heart_rate_rate);
			convertView.setTag(holder);// 绑定ViewHolder对象
		} else {
			holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
		}
		if (hisDataList.get(count - position - 1).getState() == 1) {
			holder.state.setText("平常态");
		} else {
			holder.state.setText("运动后");
		}
		// holder.state.setText(hisDataList.get(position).getState());
		holder.time.setText(hisDataList.get(count - position - 1).getDate());
		holder.rate.setText(hisDataList.get(count - position - 1)
				.getHeartRate() + "");
		return convertView;

	}

	public class ViewHolder {
		private TextView state;
		private TextView time;
		private TextView rate;
	}

}
