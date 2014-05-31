package com.dm.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

public class DMLocationUtils {
	private static final int TWO_MINUTES = 1000 * 60 * 2;
	private static final int ONE_MINUTES = 1000 * 60;

	// Gps是否可用
	public static boolean isGpsProviderEnable(Context context) {
		LocationManager locationManager = ((LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE));
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	public static boolean isNetWorkProviderEnable(Context context) {
		LocationManager locationManager = ((LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE));
		return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}

	public static boolean batter(DMLocation location, DMLocation location2) {
		if (location2 == null) {
			return true;
		}
		// 检查哪个位置获取的时间更加靠后
		long timeDelta = location.getTime() - location2.getTime();
		boolean isSignificantlyNewer = timeDelta > ONE_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -ONE_MINUTES;
		boolean isNewer = timeDelta > 0;
		// 如果两个位置获取的时间相差超过了1分钟的时间就说明新的位置更加准确
		//因为用户很可能已经移动了
		if (isSignificantlyNewer) {
			return true;
		} else if (isSignificantlyOlder) {
			return false;
		}
		//检查哪个位置精度更高
		int accuracyDelta = (int) (location.getAccuracy() - location2.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// 检查两个位置是否是来自相同的Provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(), location2.getProvider());
		// 依据时间的间隔和精度的差别2个方面来决定到底哪个位置更好
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private static boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

	public static float distanceBetween(DMLocation location, DMLocation location2) {
		float[] results = new float[1];
		Location.distanceBetween(location.getLatitude(), location.getLongitude(),
				location2.getLatitude(), location2.getLongitude(), results);
		return results[0];

	}

}
