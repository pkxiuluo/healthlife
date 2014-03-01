package com.example.locationlib;

import java.util.Date;

import com.dm.location.DMLoation;
import com.dm.location.DMLocationManager;
import com.dm.location.DMLocationObserver;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	private DMLocationManager manager;
	private Button start;
	private Button shut;
	private Button change;
	private Button map;
	private TextView locationTextView;
	private MyLocationObserver observer;
	private boolean isGPS = false;
	private MyServiceConnection connection = new MyServiceConnection();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		start = (Button) findViewById(R.id.start);
		shut = (Button) findViewById(R.id.shut);
		change = (Button) findViewById(R.id.change);
		map = (Button) findViewById(R.id.start_map);
		Intent intent = new Intent();
		// bindService(service, conn, flags)
		setChangeBtnText();
		locationTextView = (TextView) findViewById(R.id.location);
		manager = new DMLocationManager(this);
		observer = new MyLocationObserver(LocationManager.NETWORK_PROVIDER, 2000);
		change.setOnClickListener(this);
		start.setOnClickListener(this);
		shut.setOnClickListener(this);
		map.setOnClickListener(this);
	}

	private void setChangeBtnText() {
		if (isGPS) {
			change.setText("GPS");
		} else {
			change.setText("NetWork");
		}
	}

	private class MyServiceConnection implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {

		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

	}

	private class MyLocationObserver extends DMLocationObserver {

		public MyLocationObserver(String provider, long interval) {
			super(provider, interval);
		}

		@Override
		public void callBack(DMLoation loation) {
			String old = locationTextView.getText().toString();
			StringBuffer sb = new StringBuffer(old);
			sb.append("time:");
			Date date = new Date(loation.getTime());
			sb.append(date.toLocaleString());
			sb.append("经度:");
			sb.append(loation.getLongitude());
			sb.append("纬度:");
			sb.append(loation.getLatitude());
			sb.append("\n");
			locationTextView.setText(sb.toString());
		}
	}

	@Override
	public void onClick(View v) {
		if (v == change) {
			if (isGPS) {
				isGPS = false;
				observer.setProvider(LocationManager.NETWORK_PROVIDER);
				manager.notifiObserverChanged();
				setChangeBtnText();
			} else {
				isGPS = true;
				observer.setProvider(LocationManager.GPS_PROVIDER);
				manager.notifiObserverChanged();
				setChangeBtnText();
			}
		}
		if (v == start) {
			manager.addLocatoinObserver(observer);
			locationTextView.setText("开启\n");
		}
		if (v == shut) {
			manager.removeLocatoinObserver(observer);
			locationTextView.setText("stoped");
		}
		if (v == map) {
			Intent intent = new Intent(this, MapActivity.class);
			startActivity(intent);
		}
	}

}
