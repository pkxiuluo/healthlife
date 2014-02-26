package com.healthslife.activitys;

import java.io.StringReader;
import java.io.StringWriter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.RotateDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.healthslife.BaseFragmentActivity;
import com.healthslife.R;
import com.healthslife.http.MyAsyncHttpResponseHandler;
import com.loopj.android.http.AsyncHttpClient;

public class MainActivity extends BaseFragmentActivity {
	public Button btn;
	private ImageView img;
	public AsyncHttpClient mClient;
	public MyAsyncHttpResponseHandler handler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		handler = new MyAsyncHttpResponseHandler(this, mClient) {
			public void onSuccess(int arg0, org.apache.http.Header[] arg1, byte[] arg2) {
				Bitmap bitmap = BitmapFactory.decodeByteArray(arg2, 0,
						arg2.length);
				img.setImageBitmap(bitmap);
				super.onSuccess(arg0, arg1, arg2);

			};
		};
	}

	private void init() {
		mClient = new AsyncHttpClient();
		img = (ImageView) findViewById(R.id.img);
		btn = (Button) findViewById(R.id.btn);

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mClient.get("http://www.baidu.com/img/bdlogo.gif", handler);
			}

		});
	}

}
