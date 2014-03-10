package com.healthslife.fragments;

import com.healthslife.R;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class RunFragment extends Fragment   {
	private ActionBar actionBar;
	private FrameLayout contentLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_run, container, false);
		contentLayout = (FrameLayout) root.findViewById(R.id.run_content_layout);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

}
