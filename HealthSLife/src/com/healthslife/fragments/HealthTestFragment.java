package com.healthslife.fragments;

import com.healthslife.R;
import com.healthslife.healthtest.EcgFragment;
import com.healthslife.healthtest.HeartRateFragment;
import com.healthslife.run.fragments.DestinationRunFragment;
import com.healthslife.run.fragments.DistanceRunFragment;
import com.healthslife.run.fragments.NormalRunFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

public class HealthTestFragment extends Fragment {
	private PagerSlidingTabStrip tabs;
	private ViewPager mViewpager;
	private MyPagerAdapter mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_health_test, container,
				false);
		tabs = (PagerSlidingTabStrip) root.findViewById(R.id.health_test_tabs);
		mViewpager = (ViewPager) root.findViewById(R.id.health_test_pager);
		mAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
		mViewpager.setAdapter(mAdapter);
		tabs.setViewPager(mViewpager);
		return root;
	}
	
	private class MyPagerAdapter extends FragmentStatePagerAdapter {
		String[] titleStrings;
		Fragment[] fragments;

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
			titleStrings = getResources().getStringArray(R.array.health_test_tab_title);
			fragments = new Fragment[titleStrings.length];
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

		public Fragment getFragment(int position) {
			return fragments[position];
		}

		@Override
		public Fragment getItem(int arg0) {
			Fragment fragment = null;
			switch (arg0) {
			case 0:
				fragment = new HeartRateFragment();
				break;
			case 1:
				fragment = new EcgFragment();
				break;
			default:
				break;
			}
			fragments[arg0] = fragment;
			return fragment;
		}
	}
}
