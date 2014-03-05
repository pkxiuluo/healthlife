package com.example.locationlib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.dm.location.DMLocation;
import com.dm.location.DMLocationClient;
import com.dm.location.DMLocationClient.OnLocationChangeListener;
import com.dm.location.DMLocationClientOption;

public class MainActivity extends Activity implements OnClickListener {
	private Button start;
	private Button shut;
	private Button change;
	private Button map;
	private TextView locationTextView;
	private boolean isGPS = false;
	DMLocationClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		start = (Button) findViewById(R.id.start);
		shut = (Button) findViewById(R.id.shut);
		change = (Button) findViewById(R.id.change);
		map = (Button) findViewById(R.id.start_map);
		setChangeBtnText();
		locationTextView = (TextView) findViewById(R.id.location);
		change.setOnClickListener(this);
		start.setOnClickListener(this);
		shut.setOnClickListener(this);
		map.setOnClickListener(this);
		
		client = new DMLocationClient(this);
		DMLocation last =client.getLastKnownLocation();
		System.out.println("last:"+last);
		DMLocationClientOption option = new DMLocationClientOption();
		option.setInterval(2000);
		option.setPriority(DMLocationClientOption.GPS_FIRST);
		client.setOption(option);
		client.setLocationListener(new OnLocationChangeListener() {
			
			@Override
			public void onLocationChanged(DMLocation loation) {
				// TODO Auto-generated method stub
				System.out.println(loation);	
			}
		});
		
	}

	private void setChangeBtnText() {
		if (isGPS) {
			change.setText("GPS");
		} else {
			change.setText("NetWork");
		}
	}

	@Override
	public void onClick(View v) {
		if (v == change) {
			if (isGPS) {
				isGPS = false;
				setChangeBtnText();
			} else {
				isGPS = true;
				setChangeBtnText();
			}
		}
		if (v == start) {
			locationTextView.setText("开启\n");
			client.start();
		}
		if (v == shut) {
			locationTextView.setText("stoped");
			client.stop();
		}
		if (v == map) {
			Intent intent = new Intent(this, MapActivity.class);
			startActivity(intent);
		}
	}

}
