package com.healthslife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

public class BaseFragmentActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void startActivity(Intent intent) {
		// TODO Auto-generated method stub
		// overridePendingTransition(enterAnim, exitAnim)
		super.startActivity(intent);
	}
	
}
