package com.framework.base.utils.image;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Looper;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.widget.ImageView;

public class ImageManager implements ImageManagerImpl {
	private static ImageManager instance;
	private Context mContext;
	private LruCache<String, Bitmap> mMemoryCache;
	private ExecutorService mExecutorService;

	public ImageManager getInstance(Context context) {
		if (Looper.myLooper() != Looper.getMainLooper()) {
			throw new RuntimeException("Cannot instantiate outside UI thread.");
		}
		if (instance == null) {
			instance = new ImageManager(context);
		}
		return instance;
	}

	private ImageManager(Context context) {
		mContext = context;
		int memClass = ((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE))
				.getMemoryClass();
		memClass = memClass > 32 ? 32 : memClass;
		// 使用可用内存的1/8作为图片缓存
		int cacheSize = 1024 * 1024 * memClass / 8;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// return bitmap.getByteCount(); //api版本12可用
				return bitmap.getRowBytes() * bitmap.getHeight();
			}
		};
		mExecutorService = Executors.newFixedThreadPool(4);
	}

	@Override
	public void setImage(ImageView imageView, String url, int resId, boolean isReload) {
		if (imageView == null) {
			return;
		}
		if (resId > 0) {
			imageView.setImageResource(resId);
		}
		if (TextUtils.isEmpty(url)) {
			return;
		}
		if(isReload){
			//如果是重新加载，则删除所有与之相关的缓存，包括内存缓存，外存缓存
			mMemoryCache.remove(url);
		}else{
			Bitmap bitmap = mMemoryCache.get(url);
			if (bitmap != null) {
				setImageBitmap(imageView, bitmap, false);
				return;
			}
		}
		imageView.setTag(url);
		

	}

	/**
	 * 添加图片显示渐现动画
	 * 
	 */
	private static void setImageBitmap(ImageView imageView, Bitmap bitmap, boolean isTran) {
		if (isTran) {
			final TransitionDrawable td = new TransitionDrawable(new Drawable[] {
					new ColorDrawable(android.R.color.transparent), new BitmapDrawable(bitmap) });
			td.setCrossFadeEnabled(true);
			imageView.setImageDrawable(td);
			td.startTransition(300);
		} else {
			imageView.setImageBitmap(bitmap);
		}
	}
	private class ImageLoadTask implements Runnable{
		private String url;
		private ImageView imageView;
		public ImageLoadTask(ImageView imageView , String url){
			this.imageView = imageView;
			this.url = url;
		}
		@Override
		public void run() {
			 // 最后从指定的url中下载图片
            try {
                  
                    URL imageUrl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) imageUrl
                                    .openConnection();
                    conn.setConnectTimeout(30000);
                    conn.setReadTimeout(30000);
                    conn.setInstanceFollowRedirects(true);
                    InputStream is = conn.getInputStream();
                    final Bitmap bitmap =  BitmapFactory.decodeStream(is);
                   imageView.post(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						setImageBitmap(imageView , bitmap , true);
					}
				});
                    
            } catch (Exception ex) {
                    return ;
            }
		}
		
	}

}
