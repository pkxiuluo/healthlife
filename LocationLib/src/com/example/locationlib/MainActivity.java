package com.example.locationlib;

import java.util.Date;

import android.app.Activity;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	private DMLocationManager manager;
	private Button start;
	private Button shut;
	private Button change;
	private TextView locationTextView;
	private MyLocationObserver observer;
	private boolean isGPS = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		start = (Button) findViewById(R.id.start);
		shut = (Button) findViewById(R.id.shut);
		change = (Button) findViewById(R.id.change);
		setChangeBtnText();
		locationTextView = (TextView) findViewById(R.id.location);
		manager = new DMLocationManager(this);
		observer = new MyLocationObserver(LocationManager.NETWORK_PROVIDER, 2000);
		change.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isGPS) {
					isGPS=false;
					observer.setProvider(LocationManager.NETWORK_PROVIDER);
					manager.notifiObserverChanged();
					setChangeBtnText();
				} else {
					isGPS=true;
					observer.setProvider(LocationManager.GPS_PROVIDER);
					manager.notifiObserverChanged();
					setChangeBtnText();
				}

			}
		});

		start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				manager.addLocatoinObserver(observer);
				locationTextView.setText("开启\n");
			}
		});
		shut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				manager.removeLocatoinObserver(observer);
				locationTextView.setText("stoped");
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

}
