package com.healthslife.run.fragments;

import com.healthslife.R;
import com.healthslife.run.dao.RunSetting;
import com.healthslife.run.dao.RunSettingGetable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class DistanceRunFragment extends Fragment implements RunSettingGetable {

	private EditText targeEdt;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_run_distance, container, false);
		targeEdt = (EditText) root.findViewById(R.id.run_distance_target_edt);
		targeEdt.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() == 0) {
					s.append("0");
				} else if (s.length() >= 2) {
					int deleteLength = 0;
					for (int i = 0; i < s.length(); i++) {
						char c = s.charAt(i);
						if (c == '0') {
							deleteLength++;
						} else {
							break;
						}
					}
					s.delete(0, deleteLength);
				}
			}
		});
		return root;
	}

	@Override
	public RunSetting getRunSetting() {
		RunSetting settting = new RunSetting(RunSetting.DISTANCE);
		int distance = Integer.parseInt(targeEdt.getText().toString());
		settting.setDistance(distance);
		return settting;
	}
}
