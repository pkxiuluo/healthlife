package com.healthslife.run.fragments;

import com.healthslife.run.dao.RunSetting;
import com.healthslife.run.dao.RunSettingGetable;

import android.support.v4.app.Fragment;

public class DistanceRunFragment extends Fragment implements RunSettingGetable  {

	@Override
	public RunSetting getRunSetting() {
		// TODO Auto-generated method stub
		return new RunSetting();
	}

}
