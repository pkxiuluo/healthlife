package com.healthslife.activitys;

import java.util.ArrayList;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.healthslife.BaseFragmentActivity;
import com.healthslife.R;
import com.healthslife.adapters.MusicAdapter;
import com.healthslife.music.MusicInfo;
import com.healthslife.music.MusicUtil;
import com.yp.music.ListMediaPlayer;
import com.yp.music.SmartMediaPlayer;

public class MusicActivity extends BaseFragmentActivity implements OnClickListener {

	private ActionBar actionBar;
	private MusicAdapter mAdapter;
	private ListView mListView;
	private ImageView mStartBtn;
	private View mLastBtn;
	private View mNextBtn;

	private ArrayList<MusicInfo> musicInfos = new ArrayList<MusicInfo>();

	private boolean isPlaying = false;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setActionBar();
		setContentView(R.layout.activity_music);
		prepareData();
		initView();
	}
	
	private void prepareData() {
		musicInfos.addAll(MusicUtil.getPlayList());
	}

	private void initView() {
		mListView = (ListView) findViewById(R.id.music_lv);
		mAdapter = new MusicAdapter(this, musicInfos);
		mListView.setAdapter(mAdapter);
		mStartBtn = (ImageView) findViewById(R.id.music_start_img);
		mLastBtn = findViewById(R.id.music_last_img);
		mNextBtn = findViewById(R.id.music_next_img);
		mStartBtn.setOnClickListener(this);
		mLastBtn.setOnClickListener(this);
		mNextBtn.setOnClickListener(this);
	}

	private void setActionBar() {
		actionBar = getActionBar();
		actionBar.setTitle(R.string.music_action_bar_title);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void onResume() {
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
	public void onClick(View v) {
		if (v == mStartBtn) {
			if(isPlaying){
				MusicUtil.pause();
			}else{
				MusicUtil.start();
			}
		} else if (v == mLastBtn) {
			MusicUtil.goLast();
		} else if (v == mNextBtn) {
			MusicUtil.goNext();
		}

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private BroadcastReceiver mBroadCastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(action.equals(ListMediaPlayer.ACTION_PLAYSTATE_CHANGED)){
				isPlaying =	intent.getBooleanExtra(ListMediaPlayer.STATE_IS_PLAYING, false);
				reSetView();
			}
		}
	};
	
	private void reSetView(){
		if(isPlaying){
			mStartBtn.setImageResource(R.drawable.music_stop_btn_bg_selector);
		}else{
			mStartBtn.setImageResource(R.drawable.music_start_btn_bg_selector);
		}
	}

}
