package com.healthslife.http;

import org.apache.http.Header;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

import com.healthslife.dialog.HttpLoadingDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class MyAsyncHttpResponseHandler extends AsyncHttpResponseHandler {
	private Context mContext;
	private AsyncHttpClient mClient;
	private static int requestTime = 0;
	private Dialog mDialog;

	public MyAsyncHttpResponseHandler(Context context, AsyncHttpClient client) {
		mContext = context;
		mClient = client;
		initDialog();
	}

	@Override
	public void onStart() {
		System.out.println("onstart");
		requestTime++;
		if (!mDialog.isShowing()) {
			mDialog.show();
		}
		super.onStart();
	}

	@Override
	public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
		System.out.println("onsuccess");
		super.onSuccess(arg0, arg1, arg2);

	}

	@Override
	public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
		super.onFailure(arg0, arg1, arg2, arg3);
	}

	@Override
	public void onFinish() {
		System.out.println("onfinish");
		requestTime--;
		if (requestTime == 0&&mDialog.isShowing()) {
			mDialog.dismiss();
		}
		super.onFinish();
	}

	private void initDialog() {
		mDialog = new HttpLoadingDialog(mContext);
		mDialog.setCancelable(true);
		mDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				System.out.println("cancel");
				mClient.cancelRequests(mContext, true);
				requestTime = 0;
			}
		});
	}
}
