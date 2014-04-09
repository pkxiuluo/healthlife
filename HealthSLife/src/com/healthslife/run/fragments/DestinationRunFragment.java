package com.healthslife.run.fragments;

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
import android.widget.EditText;

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
import com.healthslife.run.dao.RunSetting;
import com.healthslife.run.dao.RunSettingGetable;

public class DestinationRunFragment extends Fragment implements RunSettingGetable, OnClickListener,
		OnGeocodeSearchListener {
	private EditText destInputEdt;
	private View earthBtn;
	private static final int DEST_REQUEST_CODE = 1;
	private GeocodeSearch geocode;
	private HttpLoadingDialog dialog;
	private DestInfo destInfo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_run_destiation, container, false);
		destInputEdt = (EditText) root.findViewById(R.id.run_target_dest_input_edt);
		destInputEdt.setEnabled(false);
		earthBtn = root.findViewById(R.id.run_target_dest_earth_btn);
		earthBtn.setOnClickListener(this);
		geocode = new GeocodeSearch(getActivity());
		geocode.setOnGeocodeSearchListener(this);
		dialog = new HttpLoadingDialog(getActivity());
		return root;
	}

	@Override
	public RunSetting getRunSetting() {
		RunSetting setting = new RunSetting(RunSetting.DESTINATION);
		Location location = new Location(LocationManager.GPS_PROVIDER);
		// location.setAltitude(altitude)
		DMLocation dmLocation = new DMLocation(location);
		setting.setDest(dmLocation);
		return setting;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(getActivity(), GetLocationActivity.class);
		startActivityForResult(intent, DEST_REQUEST_CODE);
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
				addressName = addressName + "(" + point.getLongitude() + "," + point.getLatitude() + ")";
				destInfo.addressName = addressName;
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
		Location location;
	}
}
