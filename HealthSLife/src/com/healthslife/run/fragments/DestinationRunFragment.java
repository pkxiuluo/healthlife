package com.healthslife.run.fragments;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.dm.location.DMLocation;
import com.healthslife.R;
import com.healthslife.activitys.GetLocationActivity;
import com.healthslife.dialog.HttpLoadingDialog;
import com.healthslife.run.dao.RunRecord;
import com.healthslife.run.dao.RunRecordDB;
import com.healthslife.run.dao.RunSetting;
import com.healthslife.run.dao.RunSettingGetable;

public class DestinationRunFragment extends Fragment implements RunSettingGetable, OnClickListener,
		OnGeocodeSearchListener {
	private TextView destInputEdt;
	private View earthBtn;
	private static final int DEST_REQUEST_CODE = 1;
	private GeocodeSearch geocode;
	private HttpLoadingDialog dialog;
	private DestInfo destInfo;

	private ListView historyListView;
	private List<RunRecord> historyRunList = new ArrayList<RunRecord>();
	private RunRecordDB recordDB;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_run_destiation, container, false);
		destInputEdt = (TextView) root.findViewById(R.id.run_target_dest_input_edt);
		// destInputEdt.setEnabled(false);
		destInputEdt.setOnClickListener(this);
		earthBtn = root.findViewById(R.id.run_target_dest_earth_btn);
		earthBtn.setOnClickListener(this);
		geocode = new GeocodeSearch(getActivity());
		geocode.setOnGeocodeSearchListener(this);
		dialog = new HttpLoadingDialog(getActivity());

		historyListView = (ListView) root.findViewById(R.id.run_target_dest_history_list);
		historyListView.setAdapter(mHistoryAdapter);
		historyListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				RunRecord record = historyRunList.get(position);
				destInfo = new DestInfo();
				destInfo.addressName = record.getTargetDestName();
				destInfo.addressNameLong = record.getTargetDestNameLong();
				destInfo.location = new Location(LocationManager.GPS_PROVIDER);
				destInfo.location.setLatitude(record.getTargetLat());
				destInfo.location.setLongitude(record.getTargetLng());
				destInputEdt.setText(destInfo.addressNameLong);
			}
		});
		recordDB = new RunRecordDB(getActivity());

		return root;
	}

	@Override
	public void onResume() {
		historyRunList.clear();
		try {
			historyRunList.addAll(recordDB.query(2, 3, 0));
			mHistoryAdapter.notifyDataSetChanged();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (historyRunList.size() == 0) {
			historyListView.setVisibility(View.GONE);
		}
		super.onResume();
	}

	@Override
	public RunSetting getRunSetting() {
		RunSetting setting = new RunSetting(RunSetting.DESTINATION);
		if (destInfo != null) {
			DMLocation dmLocation = new DMLocation(destInfo.location);
			setting.setDest(dmLocation);
			setting.setDestName(destInfo.addressName);
			setting.setDestNameLong(destInfo.addressNameLong);
		}
		return setting;
	}

	@Override
	public void onClick(View v) {
		if (v == earthBtn || v == destInputEdt) {
			Intent intent = new Intent(getActivity(), GetLocationActivity.class);
			startActivityForResult(intent, DEST_REQUEST_CODE);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && requestCode == DEST_REQUEST_CODE) {
			LatLng latLng = data.getParcelableExtra(GetLocationActivity.EXTRA_LATLNG);
			RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latLng.latitude, latLng.longitude), 200,
					GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
			geocode.getFromLocationAsyn(query);// 设置同步逆地理编码请求
			dialog.show();
			// System.out.println("result" + "latitude" + latLng.latitude +
			// "longitude" + latLng.longitude);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onGeocodeSearched(GeocodeResult arg0, int arg1) {

	}

	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		if (rCode == 0) {

			if (result != null && result.getRegeocodeAddress() != null
					&& result.getRegeocodeAddress().getFormatAddress() != null) {

				destInfo = new DestInfo();
				destInfo.location = new Location(LocationManager.GPS_PROVIDER);
				LatLonPoint point = result.getRegeocodeQuery().getPoint();
				destInfo.location.setLatitude(point.getLatitude());
				destInfo.location.setLongitude(point.getLongitude());
				String addressName = result.getRegeocodeAddress().getFormatAddress();
				destInfo.addressName = addressName;
				addressName = addressName + "(" + point.getLongitude() + "," + point.getLatitude() + ")";
				destInfo.addressNameLong = addressName;

				destInputEdt.setText(addressName);
				dialog.dismiss();
			} else {
			}
		} else if (rCode == 27) {
		} else if (rCode == 32) {
		} else {
		}

	}

	private class DestInfo {
		String addressName;
		String addressNameLong;
		Location location;
	}

	private BaseAdapter mHistoryAdapter = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView textView;
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_run_dest_history, null);
			}
			textView = (TextView) convertView.findViewById(R.id.item_run_dest_history_txt);
			textView.setText(historyRunList.get(position).getTargetDestNameLong());
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public int getCount() {
			int size = historyRunList.size();
			if (size > 1) {
				size = 1;
			}
			return size;
		}
	};
}
