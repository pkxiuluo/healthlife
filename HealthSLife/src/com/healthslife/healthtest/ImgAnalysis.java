package com.healthslife.healthtest;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;

public class ImgAnalysis implements Callback {
	private Camera mCamera;// Camera对象
	private Context mContext;
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private ImgCaptureListener mImgCaptureListener;

	public ImgAnalysis(Context mContext) {
		this.mContext = mContext;
		mSurfaceView = new SurfaceView(mContext);
		((Activity) this.mContext).addContentView(mSurfaceView,
				new LayoutParams(1, 1));
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@SuppressWarnings("deprecation")
	public void startCaptureImg() {
		if (mCamera != null) {
			try {
				mCamera.startPreview();// 开始预览，这步操作很重要
				openLight();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void stopCaptureImg() {
		if (mCamera != null) {
			try {
				/* 停止预览 */
				mCamera.stopPreview();
				closeLight();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 打开闪光灯，用作手电筒
	public void openLight() {
		Parameters parameter = mCamera.getParameters();
		parameter.setFlashMode(Parameters.FLASH_MODE_TORCH);
		mCamera.setParameters(parameter);
	}

	// 关闭闪光灯
	public void closeLight() {
		Parameters parameter = mCamera.getParameters();
		parameter.setFlashMode(Parameters.FLASH_MODE_OFF);
		mCamera.setParameters(parameter);
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		Log.v("surfaceCreated", "surfaceCreated");
		try {
			mCamera = null;
			try {
				mCamera = Camera.open(0);// 打开相机；在低版本里，只有open（）方法；高级版本加入此方法的意义是具有打开多个
				// 摄像机的能力，其中输入参数为摄像机的编号
				// 在manifest中设定的最小版本会影响这里方法的调用，如果最小版本设定有误（版本过低），在ide里将不允许调用有参的
				// open方法;
				// 如果模拟器版本较高的话，无参的open方法将会获得null值!所以尽量使用通用版本的模拟器和API；
			} catch (Exception e) {
				Log.e("============", "摄像头被占用");
			}
			if (mCamera == null) {
				Log.e("============", "摄像机为空");
				System.exit(0);
			}
			mCamera.setPreviewDisplay(mSurfaceHolder);// 设置显示面板控制器
			priviewCallBack pre = new priviewCallBack();// 建立预览回调对象
			mCamera.setPreviewCallback(pre); // 设置预览回调对象
			// mCamera.getParameters().setPreviewFormat(ImageFormat.JPEG);
			// mCamera.startPreview();// 开始预览，这步操作很重要

		} catch (IOException exception) {
			mCamera.release();
			mCamera = null;
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		stopCamera();
		mCamera.release();
		mCamera = null;
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		initCamera();
	}

	private void initCamera() {
		if (mCamera != null) {
			try {
				Camera.Parameters parameters = mCamera.getParameters();
				/*
				 * 设定相片大小为1024*768， 格式为JPG
				 */
				// parameters.setPictureFormat(PixelFormat.JPEG);
				parameters.setPreviewFpsRange(5000, 5000);
				parameters.setPictureSize(1024, 768);
				mCamera.setParameters(parameters);
				/* 打开预览画面 */
				mCamera.startPreview();
			} catch (Exception e) {
				e.printStackTrace();
				Log.v("CameraError", "CameraError:" + e.getMessage());
			}
		}
	}

	/* 停止相机的method */
	private void stopCamera() {
		if (mCamera != null) {
			try {
				/* 停止预览 */
				mCamera.stopPreview();
				closeLight();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class priviewCallBack implements Camera.PreviewCallback {
		@Override
		public void onPreviewFrame(byte[] data, Camera camera) {
			Size size = mCamera.getParameters().getPreviewSize();
			int temp[] = decodeToGray(data, camera);
			if (mImgCaptureListener != null) {
				mImgCaptureListener.onCapture(temp, size.height, size.width);
			}
		}
	}

	// 从data中取得灰度值 nv21格式解析见
	// http://blog.csdn.net/vblittleboy/article/details/10945255
	public int[] decodeToGray(byte[] data, Camera _camera) {
		Size size = mCamera.getParameters().getPreviewSize();
		int height = size.height;
		int width = size.width;
		int rgb[] = new int[height * width];
		for (int j = 0, yp = 0; j < height; j++) {
			for (int i = 0; i < width; i++, yp++) {
				rgb[yp] = (0xff & ((int) data[yp])) - 16;
				if (rgb[yp] < 0)
					rgb[yp] = 0;
				// 将灰度值转化成argb
				// rgb[yp] = Color.argb(100, rgb[yp], rgb[yp], rgb[yp]);
			}
		}
		return rgb;
	}

	public interface ImgCaptureListener {
		public void onCapture(int[] rgb, int height, int width);
	}

	public void setImgCaptureListener(ImgCaptureListener mImgCaptureListener) {
		this.mImgCaptureListener = mImgCaptureListener;
	}
}
