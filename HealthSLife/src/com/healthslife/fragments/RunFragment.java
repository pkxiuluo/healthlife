package com.healthslife.fragments;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.healthslife.R;

public class RunFragment extends Fragment {
	private ActionBar actionBar;
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyPagerAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_run, container, false);
		tabs = (PagerSlidingTabStrip) root.findViewById(R.id.tabs);
		pager = (ViewPager) root.findViewById(R.id.pager);
		mAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
		pager.setAdapter(mAdapter);
		//将各个tab设置为相同weight ，注意在setViewpager之前调用
		tabs.setShouldExpand(true);
		tabs.setViewPager(pager);
		return root;
	}

	private class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			CharSequence title = null;
			switch (position) {
			case 0:
				title = getResources().getString(R.string.run_normal);
				break;
			case 1:
				title = getResources().getString(R.string.run_destination);
				break;
			default:
				title = "";
				break;
			}
			return title;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public Fragment getItem(int arg0) {
			Fragment fragment = null;
			switch (arg0) {
			case 0:
				fragment = new NormalRunFragment();
				break;
			case 1:
				fragment = new DestinationRunFragment();
				break;
			default:
				break;
			}
			return fragment;
		}
	}

}
