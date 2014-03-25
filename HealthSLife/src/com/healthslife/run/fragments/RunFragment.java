package com.healthslife.run.fragments;

import android.app.ActionBar;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.dm.location.DMLocation;
import com.dm.location.DMLocationUtils;
import com.healthslife.R;
import com.healthslife.dialog.MyAlertDailog;
import com.healthslife.run.dao.RunSetting;
import com.healthslife.run.dao.RunSettingGetable;
import com.healthslife.run.dao.RunSettingUtil;

public class RunFragment extends Fragment {
	private ActionBar actionBar;
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyPagerAdapter mAdapter;
	private View beginBtn;
	private MyAlertDailog dialog;
	private static int SETTING_REQUEST_CODE = 1;

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

		dialog = new MyAlertDailog(getActivity());
		dialog.setContent(getText(R.string.run_gps_tips));
		dialog.setPositiveClickListener(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent();
				intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				try {
					startActivityForResult(intent, SETTING_REQUEST_CODE);
				} catch (ActivityNotFoundException ex) {
					// The Android SDK doc says that the location settings
					// activity
					// may not be found. In that case show the general settings.
					// General settings activity
					intent.setAction(Settings.ACTION_SETTINGS);
					try {
						startActivityForResult(intent, SETTING_REQUEST_CODE);
					} catch (Exception e) {
					}
				}
			}
		});
		dialog.setNegativeClickListener(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				startRunActivity();
			}
		});
		return root;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SETTING_REQUEST_CODE) {
			startRunActivity();
		}
	}

	private class BeginClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (!isLegal()) {
				return;
			}
			if (DMLocationUtils.isGpsProviderEnable(getActivity()) == false) {
				dialog.show();
			} else {
				startRunActivity();
			}
		}
	}

	private boolean isLegal() {
		RunSettingGetable getable = (RunSettingGetable) mAdapter.getFragment(pager.getCurrentItem());
		RunSetting setting = getable.getRunSetting();
		if (setting == null) {
			return false;
		}
		if (setting.getKind() == RunSetting.DISTANCE && setting.getDistance() < 100) {
			Toast.makeText(getActivity(), getText(R.string.run_tips_distance_too_short),
					Toast.LENGTH_SHORT).show();
			return false;
		} else if (setting.getKind() == RunSetting.DESTINATION && setting.getDest() == null) {
			Toast.makeText(getActivity(), getText(R.string.run_tips_dest_empty), Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;
	}

	private class MyPagerAdapter extends FragmentStatePagerAdapter {
		String[] titleStrings;
		Fragment[] fragments;

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
			titleStrings = getResources().getStringArray(R.array.run_tab_title);
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
			fragments[arg0] = fragment;
			return fragment;
		}
	}

	private void startRunActivity() {
		if (DMLocationUtils.isGpsProviderEnable(getActivity())
				|| DMLocationUtils.isNetWorkProviderEnable(getActivity())) {
			RunSettingGetable getable = (RunSettingGetable) mAdapter.getFragment(pager.getCurrentItem());
			RunSetting setting = getable.getRunSetting();
			RunSettingUtil.startActivity(getActivity(), setting);
		} else {
			Toast.makeText(getActivity(), getText(R.string.run_noprovider_available_tips),
					Toast.LENGTH_SHORT).show();
		}
	}
}
