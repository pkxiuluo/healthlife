package com.framework.common.crash;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Calendar;
import java.util.UUID;

import android.text.TextUtils;

import com.framework.base.MyApplicationInfo;
import com.framework.base.utils.file.FileManager;

public class CrashHandler implements UncaughtExceptionHandler {
	private static UncaughtExceptionHandler mPreviousHandler;
	private static volatile CrashHandler mInstance;

	private CrashHandler() {
	}

	public static CrashHandler getInstance() {
		if (mInstance == null) {
			synchronized (mInstance) {
				mInstance = new CrashHandler();
			}
		}
		mPreviousHandler = Thread.getDefaultUncaughtExceptionHandler();
		return mInstance;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable exception) {
		final Calendar now = Calendar.getInstance();
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		exception.printStackTrace(printWriter);

		try {
			String logDir = FileManager.getLogDir();
			if (TextUtils.isEmpty(logDir)) {
				return;
			}
			String filename = UUID.randomUUID().toString();
			String path = logDir + File.separator + filename + ".stacktrace";

			BufferedWriter write = new BufferedWriter(new FileWriter(path));
			write.write("Package: " + MyApplicationInfo.APP_PACKAGE + "\n");
			write.write("Version: " + MyApplicationInfo.APP_VERSION + "\n");
			write.write("Android: " + MyApplicationInfo.ANDROID_VERSION + "\n");
			write.write("Manufacturer: " + MyApplicationInfo.PHONE_MANUFACTURER + "\n");
			write.write("Model: " + MyApplicationInfo.PHONE_MODEL + "\n");
			write.write("Date: " + now + "\n");
			write.write("\n");
			write.write(result.toString());
			write.flush();
			write.close();
		} catch (Exception another) {

		} finally {
			if (mPreviousHandler != null) {
				mPreviousHandler.uncaughtException(thread, exception);
			}
		}
	}

}
