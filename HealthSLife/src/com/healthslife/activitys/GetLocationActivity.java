package com.healthslife.activitys;

import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;
import com.healthslife.BaseFragmentActivity;
import com.healthslife.R;

public class GetLocationActivity extends BaseFragmentActivity {
	private MapView mapView;
	private AMap aMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_loc);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 必须要写
		init();
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		// 自定义定位蓝点图标
		myLocationStyle
				.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));
		// 自定义精度范围的圆形边框颜色
		myLocationStyle.strokeColor(Color.BLACK);
		// 自定义精度范围的圆形边框宽度
		myLocationStyle.strokeWidth(5);
		// 将自定义的 myLocationStyle 对象添加到地图上
		aMap.setMyLocationStyle(myLocationStyle);
		// 构造 LocationManagerProxy 对象
		// 设置定位资源。如果不设置此定位资源则定位按钮不可点击。
		aMap.setLocationSource(new Source());
		// 设置默认定位按钮是否显示
		aMap.getUiSettings().setMyLocationButtonEnabled(true);
		aMap.getUiSettings().setCompassEnabled(true);
		aMap.getUiSettings().setRotateGesturesEnabled(false);
		// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		aMap.setMyLocationEnabled(true);

	}

	private class Source implements LocationSource {

		@Override
		public void activate(OnLocationChangedListener arg0) {
			Location location = new Location(LocationManager.GPS_PROVIDER);
			location.setLatitude(30.5080);
			location.setLongitude(114.4135);
			arg0.onLocationChanged(location);
		}

		@Override
		public void deactivate() {

		}

	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
		}
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}
}
