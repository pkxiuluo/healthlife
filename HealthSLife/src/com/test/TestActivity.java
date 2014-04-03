package com.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.dm.location.DMLocation;
import com.dm.location.DMLocationClient;
import com.dm.location.DMLocationClientOption;
import com.dm.location.DMLocationClient.OnLocationChangeListener;
import com.healthslife.BaseFragmentActivity;
import com.healthslife.R;

public class TestActivity extends BaseFragmentActivity implements OnClickListener, OnLocationChangeListener{
	private Button controlBtn;
	private Button clearButton;
	private TextView panelTextView;
	private DMLocationClient client;
	private boolean isStarted = false;
	private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		controlBtn = (Button) findViewById(R.id.control_btn);
		clearButton = (Button) findViewById(R.id.clear_btn);
		panelTextView = (TextView) findViewById(R.id.info_panel);

		controlBtn.setOnClickListener(this);
		clearButton.setOnClickListener(this);
		client = new DMLocationClient(this);
		client.setLocationListener(this);
		DMLocationClientOption option = new DMLocationClientOption();
		option.setInterval(5000);
		client.setOption(option);
	}

	@Override
	public void onClick(View v) {
		if (v == controlBtn) {
			if (isStarted) {
				isStarted = false;
				client.stop();
				controlBtn.setText("开启");
			} else {
				isStarted = true;
				client.start();
				controlBtn.setText("关闭");
			}
		} else if (v == clearButton) {
			panelTextView.setText("");
		}
	}

	@Override
	public void onLocationChanged(DMLocation arg0) {
		String info = panelTextView.getText().toString();
		info += "\n";
		info += "d:";
		Date date = new Date(arg0.getTime());
		info += format.format(date);
		info += " p:";
		info += arg0.getProvider();
		panelTextView.setText(info);
	}

	@Override
	protected void onStop() {
//		client.stop();
		super.onStop();
	}
}
