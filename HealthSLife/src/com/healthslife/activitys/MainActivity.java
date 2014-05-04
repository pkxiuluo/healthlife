package com.healthslife.activitys;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.healthslife.BaseFragmentActivity;
import com.healthslife.R;
import com.healthslife.adapters.NavigationAdapter;
import com.healthslife.adapters.NavigationAdapter.DataHolder;
import com.healthslife.fragments.HealthTestFragment;
import com.healthslife.fragments.InviteFragment;
import com.healthslife.fragments.SettingFragment;
import com.healthslife.music.MusicUtil;
import com.healthslife.run.fragments.RunFragment;
import com.yp.music.ListMediaPlayer;

public class MainActivity extends BaseFragmentActivity {
	private DrawerLayout drawerLayout;
	private ListView navi;
	private List<DataHolder> naviList = new ArrayList<DataHolder>();
	private BaseAdapter naviAdapter;
	private FrameLayout contentLayout;
	private CharSequence mDrawerTitle;
	private CharSequence mNoDrawertitle;
	private MyDrawerToggle mDrawerToggle;
	
	private RunFragment runFragment;
	private HealthTestFragment  healthTestFragment;
	private InviteFragment inviteFragment;
	private SettingFragment settingFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initData();
		initView();
		if (savedInstanceState == null) {
			selectItem(0);
		}
	}

	private void initData() {
		TypedArray titleArray = getResources().obtainTypedArray(R.array.navigation_title);
		TypedArray iconArray = getResources().obtainTypedArray(R.array.navigation_icon);
		int count = titleArray.length();
		for (int i = 0; i < count; i++) {
			DataHolder data = new DataHolder();
			data.icon = iconArray.getDrawable(i);
			data.title = titleArray.getString(i);
			naviList.add(data);
		}
		titleArray.recycle();
		iconArray.recycle();
	}

	private void initView() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		mDrawerTitle = mNoDrawertitle = getTitle();
		drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
		contentLayout = (FrameLayout) findViewById(R.id.main_content_layout);
		navi = (ListView) findViewById(R.id.main_left_navi_list);
		naviAdapter = new NavigationAdapter(this, naviList);
		navi.setAdapter(naviAdapter);
		navi.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				selectItem(arg2);
			}
		});
		mDrawerToggle = new MyDrawerToggle();
		drawerLayout.setDrawerListener(mDrawerToggle);
	}

	private void selectItem(int position) {
		DataHolder data = (DataHolder) naviAdapter.getItem(position);
		navi.setItemChecked(position, true);
		setTitle(data.title);
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment =new RunFragment(); 
			break;
		case 1:
			fragment =new HealthTestFragment(); 
			break;
		case 2:
			fragment = new InviteFragment();
			break;
		case 3:
			fragment = new SettingFragment();
			break;
		default:
			break;
		}
		FragmentManager manager = getSupportFragmentManager();
		manager.beginTransaction().replace(R.id.main_content_layout, fragment).commit();
		drawerLayout.closeDrawer(navi);
	}

	@Override
	public void setTitle(CharSequence title) {
		mNoDrawertitle = title;
		getActionBar().setTitle(title);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		int selectedPosition = navi.getCheckedItemPosition();
		// menu.findItem(R.id.action_music).setVisible(isVisible);
		menu.findItem(R.id.action_history).setVisible(false);
		if (selectedPosition == 1 || selectedPosition == 0) {
			menu.findItem(R.id.action_history).setVisible(true);
		}
		switch (selectedPosition) {
		case 0:
			menu.findItem(R.id.action_history).setVisible(true);
			menu.findItem(R.id.action_music).setVisible(true);
			menu.findItem(R.id.action_music_control).setVisible(true);
			break;
		case 1:
			menu.findItem(R.id.action_music).setVisible(false);
			menu.findItem(R.id.action_music_control).setVisible(false);
			menu.findItem(R.id.action_history).setVisible(true);
			break;
		default:
			menu.findItem(R.id.action_history).setVisible(false);
			menu.findItem(R.id.action_music).setVisible(false);
			menu.findItem(R.id.action_music_control).setVisible(false);
			break;
		}
		if (isPlaying) {
			menu.findItem(R.id.action_music_control).setIcon(R.drawable.menu_music_stop);
		} else {
			menu.findItem(R.id.action_music_control).setIcon(R.drawable.menu_music_start);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		if (item.getItemId() == R.id.action_music) {
			startActivity(new Intent(MainActivity.this, MusicActivity.class));
		} else if (item.getItemId() == R.id.action_history) {
			int selectedPosition = navi.getCheckedItemPosition();
			if(selectedPosition==0){
				startActivity(new Intent(MainActivity.this, RunHistoryActivity.class));
			}else if(selectedPosition==1){
				//健康测试
			}
		} else if (item.getItemId() == R.id.action_music_control) {
			if (isPlaying) {
				MusicUtil.pause();
			} else {
				MusicUtil.start();
			}
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		IntentFilter filter = new IntentFilter();
		filter.addAction(ListMediaPlayer.ACTION_PLAYSTATE_CHANGED);
		registerReceiver(mBroadCastReceiver, filter);
		super.onResume();
	}

	@Override
	protected void onPause() {
		unregisterReceiver(mBroadCastReceiver);
		super.onPause();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private class MyDrawerToggle extends ActionBarDrawerToggle {

		public MyDrawerToggle() {
			super(MainActivity.this, drawerLayout, R.drawable.ic_drawer_navi, R.string.drawer_open,
					R.string.drawer_close);
		}

		@Override
		public void onDrawerOpened(View drawerView) {
			// super.onDrawerOpened(drawerView);
			getActionBar().setTitle(mDrawerTitle);
			invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
		}

		@Override
		public void onDrawerClosed(View drawerView) {
			// super.onDrawerClosed(drawerView);
			getActionBar().setTitle(mNoDrawertitle);
			invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()

		}

	}

	private long lastBackKeyTime;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long current = SystemClock.elapsedRealtime();
			if (current - lastBackKeyTime <= 350) {
				this.finish();
			} else {
				Toast.makeText(this, getText(R.string.logout_tip), Toast.LENGTH_SHORT).show();
			}
			lastBackKeyTime = current;
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private boolean isPlaying = false;
	private BroadcastReceiver mBroadCastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ListMediaPlayer.ACTION_PLAYSTATE_CHANGED)) {
				isPlaying = intent.getBooleanExtra(ListMediaPlayer.STATE_IS_PLAYING, false);
				invalidateOptionsMenu();
			}
		}
	};

}
