package com.healthslife.run;

import android.content.Context;

import com.dm.location.DMLocation;
import com.dm.location.DMLocationUtils;
import com.healthslife.run.dao.RunResult;
import com.healthslife.run.dao.RunSetting;

public class TargetedRunClient extends RunClient {

	private RunSetting mRunSetting;
	private Context mContext;

	private float completeness;
	private OnStatusChangedListener listener;
	private float totalDistance = -1f;

	public TargetedRunClient(Context context, RunSetting setting) {
		super(context);
		this.mRunSetting = setting;
		mContext = context;
	}

	@Override
	protected void onLocationChanged(DMLocation loation) {

		if (mRunSetting.getKind() == RunSetting.DISTANCE) {
			completeness = getDistance() / mRunSetting.getDistance();
		} else if (mRunSetting.getKind() == RunSetting.DESTINATION) {
			if (totalDistance <= 0) {
				totalDistance = DMLocationUtils.distanceBetween(loation, mRunSetting.getDest());
				completeness = 0;
			} else {
				float currentDistance = DMLocationUtils.distanceBetween(loation,
						mRunSetting.getDest());
				completeness = (totalDistance - currentDistance) / totalDistance;
				completeness = completeness > 0 ? completeness : 0.0f;
			}
		}
		if (listener != null) {
			listener.onCompletenessChanged(completeness);
		}
		if (completeness >= 1) {
			if (listener != null) {
				RunResult result = new RunResult();
				result.setRunSetting(mRunSetting);
				result.setDistance(getDistance());
				result.setDuration(getDuration());
				result.setCompleteness(completeness);
				listener.onTargetFinish(result);
			}
		}
		super.onLocationChanged(loation);
	}

	public float getCompleteness() {
		return this.completeness;
	}

	public void setOnStatusChangedListener(OnStatusChangedListener listener) {
		this.listener = listener;
	}

	public interface OnStatusChangedListener {
		public void onTargetFinish(RunResult result);

		public void onCompletenessChanged(float completeness);
	}

}
