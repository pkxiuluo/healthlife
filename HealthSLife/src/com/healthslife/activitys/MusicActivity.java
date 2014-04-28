package com.healthslife.activitys;

import android.app.ActionBar;
import android.drm.DrmStore.Action;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ListView;

import com.healthslife.BaseFragmentActivity;
import com.healthslife.R;
import com.healthslife.adapters.MusicAdapter;

public class MusicActivity extends BaseFragmentActivity {
	
	private ActionBar actionBar;
	private MusicAdapter mAdapter;
	private ListView mListView;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setActionBar();
		setContentView(R.layout.activity_music);
		mListView = (ListView) findViewById(R.id.music_lv);
		mAdapter = new MusicAdapter(this);
		mListView.setAdapter(mAdapter);
	}
	
	private void setActionBar() {
		actionBar = getActionBar();
		actionBar.setTitle(R.string.music_action_bar_title);
		actionBar.setDisplayHomeAsUpEnabled(true);
		// actionBar.setLogo(R.drawable.navi_run_);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
