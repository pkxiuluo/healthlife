package com.healthslife.activitys;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnCameraChangeListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.LocationSource.OnLocationChangedListener;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.dm.location.DMLocation;
import com.dm.location.DMLocationClient;
import com.dm.location.DMLocationClient.OnLocationChangeListener;
import com.dm.location.DMLocationClientOption;
import com.healthslife.BaseFragmentActivity;
import com.healthslife.R;

public class GetLocationActivity extends BaseFragmentActivity implements OnCameraChangeListener {
	private MapView mapView;
	private AMap aMap;
	private DMLocationClient client;
	private OnLocationChangedListener listener;
	private LatLng resultLatLng;
	public static final String EXTRA_LATLNG = "LatLng";
	private ActionBar actionBar;
	private Source mLocSource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBar();
		setContentView(R.layout.activity_get_loc);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 必须要写
		client = new DMLocationClient(this);
		DMLocationClientOption option = DMLocationClientOption.getDefaultOption();
		client.setOption(option);
		client.setLocationListener(new OnLocationChangeListener() {
			@Override
			public void onLocationChanged(DMLocation loation) {
				if (loation != null) {
					if (listener != null) {
						listener.onLocationChanged(loation.getLocation());
						Location sLocation =loation.getLocation();
						LatLng latLng = new LatLng(sLocation.getLatitude(), sLocation.getLongitude());
						aMap.animateCamera(com.amap.api.maps.CameraUpdateFactory.changeLatLng(latLng));
					}
					client.stop();
				}
			}
		});
		init();
	}
	
	private void setActionBar() {
		actionBar = getActionBar();
		actionBar.setTitle(R.string.get_loc_title);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	private class Source implements LocationSource {

		@Override
		public void activate(OnLocationChangedListener arg0) {
			listener = arg0;
			client.start();
		}

		@Override
		public void deactivate() {
			client.stop();
			listener = null;
		}

	}

	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			MyLocationStyle myLocationStyle = new MyLocationStyle();
			myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_mark_position));
			myLocationStyle.strokeColor(Color.BLACK);
			myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
			myLocationStyle.strokeWidth(1.0f);
			aMap.setMyLocationStyle(myLocationStyle);
			mLocSource = new Source();
			aMap.setLocationSource(mLocSource);
			aMap.getUiSettings().setMyLocationButtonEnabled(true);
			aMap.getUiSettings().setRotateGesturesEnabled(false);
			aMap.setMyLocationEnabled(true);
			aMap.setOnCameraChangeListener(this);
		}
	}

	@Override
	public void onCameraChange(CameraPosition arg0) {
	}

	@Override
	public void onCameraChangeFinish(CameraPosition arg0) {
		resultLatLng = arg0.target;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.get_location, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==R.id.action_ok){		
			Intent intent = new Intent();
			intent.putExtra(EXTRA_LATLNG, resultLatLng);
			setResult(RESULT_OK,intent);
			this.finish();
			return true;
		}else if(item.getItemId()==android.R.id.home){
			setResult(RESULT_CANCELED);
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
		if(listener!=null){
			Location location = new Location(LocationManager.GPS_PROVIDER);
			location.setLatitude(30.5080);
			location.setLongitude(114.4135);
			listener.onLocationChanged(location);
			LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
			aMap.moveCamera(com.amap.api.maps.CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 17, 0, 0)));
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		mLocSource.deactivate();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

}
