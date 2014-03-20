package com.healthslife.run;

import com.amap.api.mapcore.l;
import com.dm.location.DMLocation;
import com.healthslife.run.dao.RunResult;
import com.healthslife.run.dao.RunSetting;

import android.R.integer;
import android.app.backup.RestoreObserver;
import android.content.Context;

public class TargetedRunClient extends RunClient {

	private RunSetting mRunSetting;
	private Context mContext;

	private float completeness;
	private OnStatusChangedListener listener;

	public TargetedRunClient(Context context, RunSetting setting) {
		super(context);
		this.mRunSetting = setting;
		mContext = context;
	}

	@Override
	protected void onLocationChanged(DMLocation loation) {
		if (mRunSetting.getKind() == RunSetting.DISTANCE) {
			completeness = getDistance() / mRunSetting.getDistance();
		}
		if (listener != null) {
			RunResult result = new RunResult();
			listener.onCompletenessChanged(completeness);
		}
		if (completeness >= 1) {
			if (listener != null) {
				RunResult result = new RunResult();
				result.setRunSetting(mRunSetting);
				result.setDistance(getDistance());
				result.setDuration(getDuration());
				listener.onTargetFinish(result);
			}
		}
		super.onLocationChanged(loation);
	}

	public void setOnStatusChangedListener(OnStatusChangedListener listener) {
		this.listener = listener;
	}

	public interface OnStatusChangedListener {
		public void onTargetFinish(RunResult result);
		public void onCompletenessChanged(float completeness);
	}

}
