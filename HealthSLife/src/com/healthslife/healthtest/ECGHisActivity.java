package com.healthslife.healthtest;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.healthslife.R;
import com.healthslife.adapters.EcgHisListAdapter;
import com.healthslife.adapters.HeartRateHisListAdapter;
import com.healthslife.healthtest.dao.ECGAnalysisRecord;

public class ECGHisActivity extends Activity {
	private ActionBar actionBar;
	private ListView hisListView;
	private BaseAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivity_ecg_his);
		hisListView = (ListView) findViewById(R.id.ecg_his_listview);
		mAdapter = new EcgHisListAdapter(this);
		hisListView.setAdapter(mAdapter);
		hisListView.setOnItemClickListener(hisListItemClick);
		setActionBar();
	}

	private OnItemClickListener hisListItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ECGAnalysisRecord mRecord = (ECGAnalysisRecord) mAdapter
					.getItem(position);
			Intent intent = new Intent(ECGHisActivity.this,
					EcgResultActivity.class);
			intent.putExtra(EcgResultActivity.PIC_PATH, mRecord.getFilePath());
			intent.putExtra(EcgResultActivity.ANALYSIS_RESULT,
					mRecord.getContent());
			startActivity(intent);
		}
	};

	private void setActionBar() {
		actionBar = getActionBar();
		actionBar.setTitle("心电图历史记录");
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
