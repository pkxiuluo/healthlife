package com.healthslife.healthtest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.healthslife.R;
import com.healthslife.healthtest.dao.ECGAnalysisRecord;
import com.healthslife.healthtest.dao.ECGDB;
import com.healthslife.healthtest.dao.HeartRateDB;
import com.healthslife.healthtest.dao.HeartRateHisRecord;

public class EcgTakePicActivity extends Activity implements
		SurfaceHolder.Callback, OnClickListener {

	private Camera mCamera;
	private ImageButton takePic, saveAndAnalysis, retakePic;
	private SurfaceView mSurfaceView;
	private SurfaceHolder holder;
	private AutoFocusCallback mAutoFocusCallback = new AutoFocusCallback();

	// 存储在手机中的文件夹名称
	private String path = "/HealthSLife/Image";
	private Bitmap bmp = null;
	private int count;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		/* 隐藏标题栏 */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/* 设定屏幕显示为横向 */
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.activity_ecg_take_pic);
		mSurfaceView = (SurfaceView) findViewById(R.id.mSurfaceView);
		mSurfaceView.setOnClickListener(this);
		holder = mSurfaceView.getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		takePic = (ImageButton) findViewById(R.id.ecg_take_pic);
		saveAndAnalysis = (ImageButton) findViewById(R.id.ecg_save_analysis); // 搜索按钮（1、保存文件，读取上传）
		retakePic = (ImageButton) findViewById(R.id.ecg_retake_pic); // 重拍按钮（放弃保存文件，并重新拍照）
		/* 按钮效果处理 */

		/* 拍照的事件处理 */
		takePic.setOnClickListener(this);
		/* 保存分析的事件处理 */
		saveAndAnalysis.setOnClickListener(this);
		/* 重拍按钮的事件处理 */
		retakePic.setOnClickListener(this);
	}

	@Override
	public void surfaceCreated(SurfaceHolder surfaceholder) {
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
			mCamera.setPreviewDisplay(holder);// 设置显示面板控制器

		} catch (IOException exception) {
			mCamera.release();
			mCamera = null;
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder surfaceholder, int format, int w,
			int h) {
		/* 相机初始化 */
		initCamera();
		count++;
		Log.i("changed", count + "times");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceholder) {
		stopCamera();
		mCamera.release();
		mCamera = null;
		holder = null;
		mSurfaceView = null;
	}

	private void takePicture() {
		if (mCamera != null) {
			mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
		}
	}

	private ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
			/* 按快门瞬间会调用这里的程序 */
		}
	};

	private PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] _data, Camera _camera) {
			/* 要处理raw data?写?否 */
		}
	};

	private PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] _data, Camera _camera) {
			/* 取得相片 */
			try {
				/* 设定Button可视性 */
				takePic.setVisibility(View.GONE);
				saveAndAnalysis.setVisibility(View.VISIBLE);
				retakePic.setVisibility(View.VISIBLE);
				/* 取得相片Bitmap对象 */
				bmp = BitmapFactory.decodeByteArray(_data, 0, _data.length);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

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

	public final class AutoFocusCallback implements
			android.hardware.Camera.AutoFocusCallback {
		public void onAutoFocus(boolean focused, Camera camera) {
			/* 对到焦点拍照 */
			if (focused) {
				takePicture();
			} else
				takePic.setVisibility(View.VISIBLE);
		}
	};

	/* 相机初始化的method */
	private void initCamera() {
		if (mCamera != null) {
			try {
				Camera.Parameters parameters = mCamera.getParameters();
				parameters.setPictureFormat(PixelFormat.JPEG);
				parameters.setPictureSize(960, 480);
				parameters.setFocusMode("macro");
				parameters.setZoom(parameters.getMaxZoom() / 2);
				// parameters
				mCamera.setParameters(parameters);
				mCamera.startPreview();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/* 停止相机的method */
	private void stopCamera() {
		if (mCamera != null) {
			try {
				/* 停止预览 */
				mCamera.stopPreview();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ecg_take_pic:
			takePicClickEvent();
			break;
		case R.id.ecg_save_analysis:
			saveAnalysisClickEvent();
			break;
		case R.id.ecg_retake_pic:
			retakePicClickEvent();
			break;
		case R.id.mSurfaceView:
			mCamera.autoFocus(null);
		default:
			break;
		}
	}

	private void retakePicClickEvent() {
		takePic.setVisibility(View.VISIBLE);
		saveAndAnalysis.setVisibility(View.GONE);
		retakePic.setVisibility(View.GONE);
		/* 重新设定Camera */
//		bmp = null;
		if (!bmp.isRecycled())
			bmp.recycle();
		stopCamera();
		initCamera();
	}

	private void saveAnalysisClickEvent() {
		saveAndAnalysis.setVisibility(View.GONE);
		retakePic.setVisibility(View.GONE);
		String analysisResult = null;
		String filePath = null;
		/* 保存文件 */
		if (bmp != null) {
			/* 检查SDCard是否存在 */
			if (!Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())) {
				/* SD卡不存在，显示Toast信息 */
				Toast.makeText(EcgTakePicActivity.this, "SD卡不存在!保存失败！",
						Toast.LENGTH_LONG).show();
			} else {
				try {
					/* 文件不存在就创建 */
					File firstFile = new File(
							Environment.getExternalStorageDirectory(),
							"/HealthSLife");
					if (!firstFile.exists()) {
						firstFile.mkdir();
					}
					
					File secondFile = new File(firstFile, "/ECGImage");
					if (!secondFile.exists()) {
						secondFile.mkdir();
					}
					/* 保存相片文件 */
					String fileName = new SimpleDateFormat("yyyyMMddHHmmss")
							.format(new Date()) + ".jpgs";
					File n = new File(secondFile, fileName);
					FileOutputStream bos = new FileOutputStream(
							n.getAbsolutePath());
					/* 文件转换 */
					bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
					/* 调用flush()方法，更新BufferStream */
					bos.flush();
					/* 结束OutputStream */
					bos.close();

					// 将拍照文件的路径返回到调用视图View中，在onActivityResult中进行获取文件的路径
					filePath = n.getAbsolutePath();

					analysisResult = ECGAnalysis.Analysis(filePath);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		if (!bmp.isRecycled())
			bmp.recycle();

		// takePic.setVisibility(View.VISIBLE);

		/* 重新设定Camera */
		// stopCamera();
		// initCamera();
		Intent intent = new Intent(this, EcgResultActivity.class);
		if (analysisResult == null) {
			// intent.putExtra("analysis_result", "图像太模糊，请重新拍摄");
			Toast.makeText(this, "图像太模糊，请重新拍摄", Toast.LENGTH_SHORT).show();
			deleteFileByPath(filePath);
			retakePicClickEvent();
			return;
		} else if (analysisResult.equals("BUG")) {
			Toast.makeText(this, "请正确拍摄心电图图像", Toast.LENGTH_SHORT).show();
			deleteFileByPath(filePath);
			retakePicClickEvent();
			return;
		} else {
			intent.putExtra("analysis_result", analysisResult);
		}
		intent.putExtra("pic_path", filePath);
		// setResult(20, data);
		// // 关闭掉这个Activity
		// surfaceDestroyed(holder);
		stopCamera();
		if (mCamera != null) {
			try {
				/* 停止预览 */
				mCamera.release();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		startActivity(intent);
		saveData(analysisResult, filePath);
		this.finish();
	}

	private void saveData(String content, String filePath) {
		ECGDB mDB = new ECGDB(this);
		mDB.add(new  ECGAnalysisRecord(new SimpleDateFormat("yyyy-MM-dd HH:mm")
				.format(new Date()), content, filePath));
	}

	public void deleteFileByPath(String filePath) {
		if (filePath == null)
			return;
		File file = new File(filePath);
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					this.deleteFileByPath(files[i].getAbsolutePath()); // 把每个文件
																		// 用这个方法进行迭代
				}
			}
			file.delete();
		}
		// else {
		// // Constants.Logdada("文件不存在！" + "\n");
		// }
	}

	private void takePicClickEvent() {
		/* 关闭闪光灯并拍照 */
		takePic.setVisibility(View.GONE);
		mCamera.autoFocus(mAutoFocusCallback);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (mCamera != null) {
			try {
				/* 停止预览 */
				mCamera.release();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
