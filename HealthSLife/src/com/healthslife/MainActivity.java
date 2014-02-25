package com.healthslife;

import android.app.ActionBar;
import android.os.Bundle;

public class MainActivity extends BaseFragmentActivity {
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActionBar bar = getActionBar();
		
		System.out.println("啊");
	}

}
