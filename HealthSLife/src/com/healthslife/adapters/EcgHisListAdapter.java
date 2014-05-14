package com.healthslife.adapters;

import java.util.ArrayList;

import com.healthslife.R;
import com.healthslife.adapters.HeartRateHisListAdapter.ViewHolder;
import com.healthslife.healthtest.dao.ECGAnalysisRecord;
import com.healthslife.healthtest.dao.ECGDB;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EcgHisListAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<ECGAnalysisRecord> dataList;
	private int count;

	public EcgHisListAdapter(Context mContext) {
		this.mContext = mContext;
		ECGDB mDB = new ECGDB(mContext);
		dataList = (ArrayList<ECGAnalysisRecord>) mDB.queryAll();
		count = dataList.size();
	}

	@Override
	public int getCount() {
		return count;
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(count - position - 1);
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
					R.layout.item_ecg_history, null);
			holder = new ViewHolder();
			holder.date = (TextView) convertView
					.findViewById(R.id.item_ecg_history_date_txt);
			holder.content = (TextView) convertView
					.findViewById(R.id.item_ecg_history_content_txt);
			convertView.setTag(holder);// 绑定ViewHolder对象
		} else {
			holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
		}
		holder.date.setText(dataList.get(count - position - 1).getDate());
		holder.content.setText(removeWrap(dataList.get(count - position - 1).getContent()));
		return convertView;
	}

	public String removeWrap(String str) {
		String temp = "";
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '\n') {
				temp += ' ';
			} else {
				temp += str.charAt(i);
			}
		}
		return temp;
	}

	public class ViewHolder {
		private TextView date;
		private TextView content;
	}
}
