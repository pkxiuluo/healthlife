package com.framework.base.utils.file;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class UrlFileCache {

	private static AsyncHttpClient client = new AsyncHttpClient();
	private static Vector<Task> taskVector = new Vector<Task>();

	public static void getFile(String url, String cacheDir, LoadListener listener) {
		if (listener != null) {
			listener.onStart();
		}

		Task task = new Task(url, cacheDir);
		File file = new File(task.getAbsolutePath());
		if (file.exists()) {
			listener.onSuccess(file);
			return;
		}

		if (!taskVector.contains(task)) {
			// 1.如果不含有该任务 ，就添加任务到队列，然后开启下载
			taskVector.add(task);
			if (listener != null) {
				task.addLoadListener(listener);
			}
			client.get(url, new MyAsyncHttpResponseHandler(task));
		} else {
			// 2.如果已有同样的任务，就把listener添加到任务中
			task = taskVector.get(taskVector.indexOf(task));
			task.addLoadListener(listener);
		}
		return;
	}

	public static void cancle(String url, String cacheDir) {
		Task task = new Task(url, cacheDir);
		taskVector.remove(task);
	}

	public static interface LoadListener {
		public void onStart();

		public void onFailure();

		public void onProgress(int percent);

		public void onSuccess(File file);
	}

	private static class MyAsyncHttpResponseHandler extends AsyncHttpResponseHandler {

		private Task mTask;

		public MyAsyncHttpResponseHandler(Task task) {
			mTask = task;
		}

		@Override
		public void onStart() {
			super.onStart();
		}

		@Override
		public void onProgress(int bytesWritten, int totalSize) {
			if (taskVector.contains(mTask)) {
				for (LoadListener listener : mTask.listeners) {
					if (listener != null) {
						listener.onProgress(bytesWritten * 100 / totalSize);
					}
				}
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
			if (taskVector.contains(mTask)) {
				for (LoadListener listener : mTask.listeners) {
					if (listener != null) {
						listener.onFailure();
					}
				}
			}
		}

		@Override
		public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
			File file = new File(mTask.getAbsolutePath());
			File parentFile = file.getParentFile();
			if (!parentFile.isDirectory()) {
				parentFile.mkdirs();
			}
			FileOutputStream fos;
			try {
				file.createNewFile();
				fos = new FileOutputStream(file);
				fos.write(arg2);
				fos.close();
			} catch (Exception e) {
				e.printStackTrace(System.out);
				file.delete();
			}
			if (taskVector.contains(mTask)) {
				if (file.exists()) {
					for (LoadListener listener : mTask.listeners) {
						if (listener != null) {
							listener.onSuccess(file);
						}
					}
				} else {
					for (LoadListener listener : mTask.listeners) {
						if (listener != null) {
							listener.onFailure();
						}
					}
				}
			}
		}

		@Override
		public void onFinish() {
			taskVector.remove(mTask);
		}
	}

	private static class Task {
		private String url;
		private String cacheDir;
		private ArrayList<LoadListener> listeners = new ArrayList<UrlFileCache.LoadListener>();

		Task(String url, String cacheDir) {
			this.url = url;
			this.cacheDir = cacheDir;
		}

		public void addLoadListener(LoadListener listener) {
			if (!listeners.contains(listener)) {
				this.listeners.add(listener);
			}
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof Task)) {
				return false;
			}
			if (o.hashCode() == this.hashCode()) {
				return true;
			}
			return false;
		}

		public String getAbsolutePath() {
			File file = new File(cacheDir);
			String path = file.getAbsolutePath() + File.separator + url.hashCode();
			return path;
		}

		@Override
		public int hashCode() {
			return this.toString().hashCode();
		}

		@Override
		public String toString() {
			return getAbsolutePath();
		}

	}
}
