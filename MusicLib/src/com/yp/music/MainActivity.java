package com.yp.music;

import java.util.List;

import com.yp.musiclib.R;

import android.database.Cursor;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Audio.Media;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements OnClickListener {
	SmartMediaPlayer player;
	ListView lv;
	long[] playList;
	Button btn;
	EditText edt;
	Button playBtn;

	Button shuffle,back,pause,next;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_main);
		prepareData();
		lv = (ListView) findViewById(R.id.lv);
		btn = (Button) findViewById(R.id.btn);
		edt = (EditText) findViewById(R.id.edt);
		edt.setText(300000 + "");
		lv.setAdapter(new MyAdapter(playList));
		
		playBtn = (Button) findViewById(R.id.play);
		shuffle = (Button) findViewById(R.id.shuffle);
		back = (Button) findViewById(R.id.back);
		pause = (Button) findViewById(R.id.pause);
		next = (Button) findViewById(R.id.next);
		
		btn.setOnClickListener(this);
		playBtn.setOnClickListener(this);
		shuffle.setOnClickListener(this);
		back.setOnClickListener(this);
		pause.setOnClickListener(this);
		next.setOnClickListener(this);
		
		
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		player = new SmartMediaPlayer(this);
		player.open(playList);
//		player.setPlayPosition(1);
//		player.setPlayMode(SmartMediaPlayer.CONTROL_REPEAT_ALL_ONCE, SmartMediaPlayer.CONTROL_SHUFFLE_NORMAL);

	}
	boolean isPause=false;
	@Override
	public void onClick(View v) {
		if(v==playBtn){
			player.start();
		}else	if(v==btn){
			int seek = Integer.decode(edt.getText().toString());
			player.seek(seek);
		}else	 if(v==back){
			player.goLast();
		}else if(v==next){
			player.goNext();
		}else if(v==pause){
			if(!isPause){
				isPause = true;
				player.pause();
			}else{
				isPause = false;
				player.start();
			}
		}else if(v==shuffle){
			player.setPlayMode(SmartMediaPlayer.CONTROL_REPEAT_CURRENT, SmartMediaPlayer.CONTROL_SHUFFLE_NONE);
		}
	}

	private void prepareData() {
		Cursor cursor = getContentResolver().query(Media.EXTERNAL_CONTENT_URI, new String[] { Media._ID }, null, null,
				null);
		playList = new long[5];
		int i = 0;
		while (cursor.moveToNext()) {
			playList[i] = cursor.getLong(0);
			if (i == 4) {
				break;
			}
			i++;

		}
	}

	public class MyAdapter extends BaseAdapter {
		long[] list;

		MyAdapter(long[] list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView text;
			if (convertView == null) {
				text = new TextView(MainActivity.this);
				text.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources()
						.getDisplayMetrics()));
			} else {
				text = (TextView) convertView;
			}
			Cursor cursor = getContentResolver()
					.query(Media.EXTERNAL_CONTENT_URI, new String[] { Media._ID, Media.TITLE, Media.ARTIST },
							Media._ID + "=" + list[position], null, null);

			StringBuffer sBuffer = new StringBuffer();

			if (cursor.moveToFirst()) {
				sBuffer.append("id");
				sBuffer.append(cursor.getString(0));
				sBuffer.append("title");
				sBuffer.append(cursor.getString(1));
				sBuffer.append("artist");
				sBuffer.append(cursor.getString(2));
			}
			cursor.close();
			text.setText(sBuffer.toString());
			return text;
		}

	}

}
