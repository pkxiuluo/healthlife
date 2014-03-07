package com.healthslife.activitys;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.widget.ListView;

import com.healthslife.BaseFragmentActivity;
import com.healthslife.R;
import com.healthslife.adapters.NavigationAdapter;

public class MainActivity extends BaseFragmentActivity {
	private DrawerLayout drawerLayout;
	private ListView navi;
	private String[] title = { "1", "2", "3" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
		navi = (ListView) findViewById(R.id.main_left_navi_list);
		navi.setAdapter(new NavigationAdapter(this));
	}

}
