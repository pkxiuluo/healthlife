package com.healthslife.healthtest;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.healthslife.R;

public class EcgResultActivity extends Activity {
	private ActionBar actionBar;
	private ImageView resultPic;
	private TextView resultContent;
	private String resultContentStr;
	private String picPath;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ecg_result);
		resultPic = (ImageView) findViewById(R.id.result_pic);
		resultContent = (TextView) findViewById(R.id.result_content);
		Intent intent = getIntent();
		picPath = intent.getExtras().getString("pic_path");
		resultContentStr = intent.getExtras().getString("analysis_result");
		init();
		setActionBar();
	}

	private void init() {
		BitmapFactory.Options op = new BitmapFactory.Options();
		op.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap mBitmap = BitmapFactory.decodeFile(picPath, op);
		resultPic.setImageBitmap(mBitmap);
		resultContent.setText(resultContentStr);
	}

	private void setActionBar() {
		actionBar = getActionBar();
		actionBar.setTitle("心电图分析结果");
		actionBar.setDisplayHomeAsUpEnabled(true);
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

}
