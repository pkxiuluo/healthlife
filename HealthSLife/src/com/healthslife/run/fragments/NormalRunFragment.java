package com.healthslife.run.fragments;

import com.healthslife.R;
import com.healthslife.run.dao.RunSetting;
import com.healthslife.run.dao.RunSettingGetable;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NormalRunFragment extends Fragment implements RunSettingGetable {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_run_normal, container, false);
		return root;
	}

	@Override
	public RunSetting getRunSetting() {
		return new RunSetting();
	}

}
