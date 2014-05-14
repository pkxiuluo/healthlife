package com.healthslife.fragments;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.renren.Renren;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qq.QQ;

import com.healthslife.R;
import com.healthslife.healthtest.ECGAnalysis;
import com.healthslife.healthtest.EcgTakePicActivity;

public class InviteFragment extends Fragment implements OnClickListener {
	private ImageButton inviteByQQBtn;
	private ImageButton inviteBySinaBtn;
	private ImageButton inviteByWXBtn;
	private ShareParams sp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ShareSDK.initSDK(getActivity());
		View root = inflater
				.inflate(R.layout.fragment_invite, container, false);
		inviteByQQBtn = (ImageButton) root.findViewById(R.id.invite_qq_btn);
		inviteByQQBtn.setOnClickListener(this);
		inviteBySinaBtn = (ImageButton) root.findViewById(R.id.invite_sina_btn);
		inviteBySinaBtn.setOnClickListener(this);
		inviteByWXBtn = (ImageButton) root.findViewById(R.id.invite_wx_btn);
		inviteByWXBtn.setOnClickListener(this);
		sp = new ShareParams();
		sp.setAddress("");
		sp.setTitle("约跑");
		sp.setTitleUrl(""); // 标题的超链接
		sp.setText("hey！让我们一起来跑步吧！");
		// sp.setImageUrl("http://www.someserver.com/测试图片网络地址.jpg");
		Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.share_iamge);
		sp.setImageData(mBitmap);
		saveShareImage(mBitmap);
		File f = new File(Environment.getExternalStorageDirectory(),
				"/HealthSLife/shareImage/shareimage.jpgs");
		if (f.exists()) {
			sp.setImagePath(f.getAbsolutePath());
//			Toast.makeText(getActivity(), "shareImage", Toast.LENGTH_SHORT).show();
		}

		// sp.setSite("发布分享的网站名称");
		// sp.setSiteUrl("发布分享网站的地址");
		return root;
	}

	public void saveShareImage(Bitmap bmp) {
		if (bmp != null) {
			/* 检查SDCard是否存在 */
			if (!Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())) {
				/* SD卡不存在，显示Toast信息 */
			} else {
				try {
					/* 文件不存在就创建 */
					File firstFile = new File(
							Environment.getExternalStorageDirectory(),
							"/HealthSLife");
					if (!firstFile.exists()) {
						firstFile.mkdir();
					}
					
					File secondFile = new File(firstFile, "/shareImage");
					if (!secondFile.exists()) {
						secondFile.mkdir();
					}
					/* 保存相片文件 */
					String fileName = "shareimage.jpgs";
					File n = new File(secondFile, fileName);
					if (n.exists()) {
						return;
					}
					FileOutputStream bos = new FileOutputStream(
							n.getAbsolutePath());
					/* 文件转换 */
					bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
					/* 调用flush()方法，更新BufferStream */
					bos.flush();
					/* 结束OutputStream */
					bos.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.invite_qq_btn:
			inviteByQQ();
			break;
		case R.id.invite_sina_btn:
			inviteBySina();
			break;
		case R.id.invite_wx_btn:
			inviteByWX();
			break;
		default:
			;
			break;
		}
	}

	private void inviteByWX() {

		Platform renren = ShareSDK
				.getPlatform(getActivity(), ShortMessage.NAME);
		renren.setPlatformActionListener(new PlatformActionListener() {

			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onComplete(Platform arg0, int arg1,
					HashMap<String, Object> arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCancel(Platform arg0, int arg1) {
				// TODO Auto-generated method stub

			}
		}); // 设置分享事件回调
		// 执行图文分享
		renren.share(sp);
	}

	private void inviteBySina() {
		Platform weibo = ShareSDK.getPlatform(getActivity(), SinaWeibo.NAME);
		weibo.setPlatformActionListener(new PlatformActionListener() {

			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onComplete(Platform arg0, int arg1,
					HashMap<String, Object> arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCancel(Platform arg0, int arg1) {
				// TODO Auto-generated method stub

			}
		}); // 设置分享事件回调
		// 执行图文分享
		weibo.share(sp);
	}

	private void inviteByQQ() {

		Platform qq = ShareSDK.getPlatform(getActivity(), QQ.NAME);
		qq.setPlatformActionListener(new PlatformActionListener() {

			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onComplete(Platform arg0, int arg1,
					HashMap<String, Object> arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCancel(Platform arg0, int arg1) {
				// TODO Auto-generated method stub

			}
		}); // 设置分享事件回调
		// 执行图文分享
		qq.share(sp);
	}
}
