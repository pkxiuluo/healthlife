package com.healthslife.run.fragments;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.healthslife.R;
import com.healthslife.run.dao.RunSetting;
import com.healthslife.run.dao.RunSettingGetable;
import com.healthslife.run.dao.RunSettingUtil;

public class RunFragment extends Fragment {
	private ActionBar actionBar;
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyPagerAdapter mAdapter;
	private View beginBtn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_run, container, false);
		tabs = (PagerSlidingTabStrip) root.findViewById(R.id.tabs);
		pager = (ViewPager) root.findViewById(R.id.pager);
		beginBtn = root.findViewById(R.id.run_begin_btn);
		mAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
		pager.setAdapter(mAdapter);
		tabs.setViewPager(pager);

		beginBtn.setOnClickListener(new BeginClick());
		return root;
	}

	private class BeginClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			RunSettingGetable getable = (RunSettingGetable) mAdapter.getItem(pager.getCurrentItem());
			RunSetting setting = getable.getRunSetting();
			RunSettingUtil.startActivity(getActivity(), setting);
		}
	}

	private class MyPagerAdapter extends FragmentStatePagerAdapter {
		String[] titleStrings;

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
			titleStrings = getResources().getStringArray(R.array.run_tab_title);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			CharSequence title = titleStrings[position];
			return title;
		}

		@Override
		public int getCount() {
			return titleStrings.length;
		}

		@Override
		public Fragment getItem(int arg0) {
			Fragment fragment = null;
			switch (arg0) {
			case 0:
				fragment = new NormalRunFragment();
				break;
			case 1:
				fragment = new DistanceRunFragment();
				break;
			case 2:
				fragment = new DestinationRunFragment();
				break;
			default:
				break;
			}
			return fragment;
		}
	}

}
