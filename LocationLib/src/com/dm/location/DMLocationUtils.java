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
		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - location2.getTime();
		boolean isSignificantlyNewer = timeDelta > ONE_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -ONE_MINUTES;
		boolean isNewer = timeDelta > 0;
		// If it's been more than two minutes since the current location, use
		// the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be
			// worse
		} else if (isSignificantlyOlder) {
			return false;
		}
		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - location2.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(), location2.getProvider());
		// Determine location quality using a combination of timeliness and
		// accuracy
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
