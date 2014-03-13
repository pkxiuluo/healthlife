package com.framework.base.utils.file;

import java.io.File;

import android.os.Environment;

import com.framework.base.MyApplicationInfo;

public class FileManager {
	private static final String LOG = "log";
	private static final String AUDIO = "audio";
	private static final String IMAGE = "image";

	public static boolean isExternalStorageMounted() {
		boolean canRead = Environment.getExternalStorageDirectory().canRead();
		boolean onlyRead = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED_READ_ONLY);
		boolean unMounted = Environment.getExternalStorageState().equals(
				Environment.MEDIA_UNMOUNTED);
		return !(!canRead || onlyRead || unMounted);
	}

	public static String getLogDir() {
		return getCacheDirPath(LOG);
	}

	public static String getAudioDir() {
		return getCacheDirPath(AUDIO);
	}

	private static String getCacheDirPath(String dirName) {
		if (!isExternalStorageMounted())
			return "";
		else {
			String path = getSdCardCachePath() + File.separator + dirName;
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			return path;
		}
	}

	private static String getSdCardCachePath() {
		if (isExternalStorageMounted()) {
			File path = MyApplicationInfo.APPLICATION_CONTEXT.getExternalCacheDir();
			if (path != null) {
				return path.getAbsolutePath();
			}
		} else {
			return "";
		}
		return "";
	}
}
