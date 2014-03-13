package com.framework.common.crash;


public class CrashManager {
	/**
	 * 注册CrashHandler,把之前的log信息上传服务器
	 */
	public static void registerHandler() {
		// Register if not already registered
		Thread.setDefaultUncaughtExceptionHandler( CrashHandler.getInstance());
	/*ScheduledExecutorService scheduledExecutorService = Executors
				.newSingleThreadScheduledExecutor();
		scheduledExecutorService.schedule(new ClearLogTask(), 5, TimeUnit.SECONDS);*/

	}

}
