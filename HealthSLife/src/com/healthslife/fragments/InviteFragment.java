package com.healthslife.fragments;

import java.util.HashMap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.renren.Renren;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qq.QQ;

import com.healthslife.R;

public class InviteFragment extends Fragment implements OnClickListener {
	private ImageButton inviteByQQBtn;
	private ImageButton inviteBySinaBtn;
	private ImageButton inviteByWXBtn;

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
		return root;
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
		ShareParams sp = new ShareParams();
		sp.setAddress("");
		sp.setTitle("测试分享的标题");
		sp.setTitleUrl("http://sharesdk.cn"); // 标题的超链接
		sp.setText("测试分享的文本");
		sp.setImageUrl("http://www.someserver.com/测试图片网络地址.jpg");
		sp.setSite("发布分享的网站名称");
		sp.setSiteUrl("发布分享网站的地址");

		Platform renren = ShareSDK.getPlatform(getActivity(), ShortMessage.NAME);
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
		ShareParams sp = new ShareParams();
		sp.setText("测试分享的文本");
		sp.setImagePath("/mnt/sdcard/测试分享的图片.jpg");
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
		ShareParams sp = new ShareParams();
		sp.setTitle("测试分享的标题");
		sp.setTitleUrl("http://sharesdk.cn"); // 标题的超链接
		sp.setText("测试分享的文本");
		sp.setImageUrl("http://www.someserver.com/测试图片网络地址.jpg");
		sp.setSite("发布分享的网站名称");
		sp.setSiteUrl("发布分享网站的地址");

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
