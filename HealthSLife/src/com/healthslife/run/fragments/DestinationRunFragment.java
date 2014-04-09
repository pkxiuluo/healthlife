package com.healthslife.run.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

import com.amap.api.maps.model.LatLng;
import com.healthslife.R;
import com.healthslife.activitys.GetLocationActivity;
import com.healthslife.run.dao.RunSetting;
import com.healthslife.run.dao.RunSettingGetable;

public class DestinationRunFragment extends Fragment implements RunSettingGetable, OnClickListener {
	private EditText destInputEdt;
	private View earthBtn;
	private static final int DEST_REQUEST_CODE = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_run_destiation, container, false);
		destInputEdt = (EditText) root.findViewById(R.id.run_target_dest_input_edt);
		earthBtn = root.findViewById(R.id.run_target_dest_earth_btn);
		earthBtn.setOnClickListener(this);
		return root;
	}

	@Override
	public RunSetting getRunSetting() {
		return new RunSetting(RunSetting.DESTINATION);
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
			System.out.println("result"+"latitude" + latLng.latitude + "longitude" + latLng.longitude);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
