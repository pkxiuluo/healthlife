package com.example.locationlib;

import com.amap.api.maps.MapView;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MapActivity extends FragmentActivity {

	private MapView mapView;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_map);
		mapView = (MapView) findViewById(R.id.map);
	}

}
