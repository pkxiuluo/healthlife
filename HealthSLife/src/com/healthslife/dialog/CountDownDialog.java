package com.healthslife.dialog;

import android.R.integer;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.healthslife.R;

public class CountDownDialog extends Dialog {
	private ImageView img;
	private TextView txt;
	private TextSwitcher switcher;
	private static final int DEFAULT_DURATION=5;

	private int duration=DEFAULT_DURATION;
	public CountDownDialog(Context context) {
		this(context, R.style.fullScreenDialog);
	}
	private CountDownDialog(Context context, int theme) {
		super(context, theme);
		init();
	}

	private void init() {
		setContentView(R.layout.dialog_count_down);
		img = (ImageView) findViewById(R.id.count_down_img);
		switcher = (TextSwitcher) findViewById(R.id.count_down_switcher);
		switcher.setFactory(mViewFactory);
		switcher.setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.count_down_expand));
		switcher.setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.count_down_expand_in));
		setCanceledOnTouchOutside(false);
		setOnShowListener(new MyShowListener());
		setCancelable(false);
	}
	public void setDuratoin(int duration){
		if(duration<=0){
			this.duration = DEFAULT_DURATION;
			return;
		}
		this.duration =duration;
	}

	private class MyShowListener implements OnShowListener {
		@Override
		public void onShow(DialogInterface dialog) {
			switcher.setCurrentText(String.valueOf(duration));
			Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.count_down_rotate);
			animation.setRepeatCount(CountDownDialog.this.duration-1);
			animation.setAnimationListener(new AnimationListener() {
				int num = duration-1;

				@Override
				public void onAnimationStart(Animation animation) {
					switcher.setText(String.valueOf(num--));
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					switcher.setText(String.valueOf(num--));
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					CountDownDialog.this.dismiss();
				}
			});
			img.setAnimation(animation);
			animation.start();

		}
	}

	private ViewFactory mViewFactory = new ViewFactory() {
		@Override
		public View makeView() {
			TextView t = new TextView(CountDownDialog.this.getContext());
			t.setGravity(Gravity.CENTER);
			t.setTextColor(Color.WHITE);
//			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//			lp.gravity = Gravity.CENTER;
//			t.setLayoutParams(lp);
			DisplayMetrics dm = CountDownDialog.this.getContext().getResources().getDisplayMetrics();
			int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, dm);
			t.setTextSize(size);
			return t;
		}
	};

}
