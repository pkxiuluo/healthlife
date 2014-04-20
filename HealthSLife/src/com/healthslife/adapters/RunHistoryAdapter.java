package com.healthslife.adapters;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.SimpleFormatter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.healthslife.R;
import com.healthslife.run.dao.RunRecord;
import com.healthslife.run.dao.RunRecordDB;
import com.healthslife.run.dao.RunSetting;

public class RunHistoryAdapter extends BaseAdapter {
	private Context mContext;
	private List<RunRecord> recordList = new ArrayList<RunRecord>();
	private RunRecordDB rrDb;
	private DecimalFormat distanceFormat = new DecimalFormat("##0");
	private SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

	public RunHistoryAdapter(Context context) {
		mContext = context;
		rrDb = new RunRecordDB(context);
		try {
			recordList.addAll(rrDb.query(0, 0, 0));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void changeData(int date,int kind ,int complete){
		recordList.clear();
		try {
			recordList.addAll(rrDb.query(date, kind, complete));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return recordList.size();
	}

	@Override
	public Object getItem(int position) {
		return recordList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_run_history, null);
			holder = new ViewHolder();
			holder.competeImg = (ImageView) convertView.findViewById(R.id.item_run_history_complete_img);
			holder.distanceTxt = (TextView) convertView.findViewById(R.id.item_run_history_distance_txt);
			holder.durationTxt = (TextView) convertView.findViewById(R.id.item_run_history_duration_txt);
			holder.dateTxt = (TextView) convertView.findViewById(R.id.item_run_history_date_txt);
			holder.kindTxt = (TextView) convertView.findViewById(R.id.item_run_history_kind_txt);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		RunRecord record = recordList.get(position);
		if (record.getCompleteness() >= 1) {
			holder.competeImg.setImageResource(R.drawable.ic_correct_history);
		} else {
			holder.competeImg.setImageResource(R.drawable.ic_wrong_history);
		}
		switch (record.getKind()) {
		case RunSetting.NORMAL:
			holder.kindTxt.setText(R.string.run_history_pattern_free);
			break;
		case RunSetting.DISTANCE:
			holder.kindTxt.setText(R.string.run_history_pattern_distance);
			break;
		case RunSetting.DESTINATION:
			holder.kindTxt.setText(R.string.run_history_pattern_dest);
			break;
		default:
			break;
		}
		String duration = DateUtils.formatElapsedTime(record.getRunDuration()/1000);
		holder.durationTxt.setText(duration);
		holder.distanceTxt.setText(distanceFormat.format(record.getRunDistance()));
		holder.dateTxt.setText(dateFormat.format(record.getCreateTime()));
		return convertView;
	}

	private class ViewHolder {
		ImageView competeImg;
		TextView distanceTxt;
		TextView durationTxt;
		TextView kindTxt;
		TextView dateTxt;
	}

}
