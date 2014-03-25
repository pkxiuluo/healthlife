package com.healthslife.run.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.healthslife.R;

public class CircleProgressBar extends View {

	private int mMaxProgress = 100;
	private int mProgress = 0;
	private RectF mCircleRectF = new RectF();
	private SingleCircleDrawer mDrawer = new SingleCircleDrawer(mCircleRectF);
	private boolean isTouchable = false;

	private Padding mPadding = new Padding();
	{
		mPadding.left = 10;
		mPadding.top = 10;
		mPadding.right = 10;
		mPadding.bottom = 10;
	}

	public CircleProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context
				.getResources().getDisplayMetrics());
		mDrawer.setPaint(getResources().getColor(R.color.red), width);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mDrawer.setValue(mProgress);
		mDrawer.draw(canvas);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		measureCircleRect();
	}

	private void measureCircleRect() {
		int widthAfterPadding = getWidth() - mPadding.left - mPadding.right;
		int heightAfterPadding = getHeight() - mPadding.top - mPadding.bottom;
		int centerX = widthAfterPadding / 2 + mPadding.left;
		int centerY = heightAfterPadding / 2 + mPadding.top;
		float radius = Math.min(widthAfterPadding, heightAfterPadding) / 2;

		mCircleRectF.left = centerX - radius;
		mCircleRectF.right = centerX + radius;
		mCircleRectF.top = centerY - radius;
		mCircleRectF.bottom = centerY + radius;
	}

	public int getProgress() {
		return this.mProgress;
	}

	public void setProgress(int progress) {
		this.mProgress = progress;
		postInvalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// if(!super.onTouchEvent(event)){
		// return false;
		// }
		if (isTouchable == false) {
			return super.onTouchEvent(event);
		}
		int action = event.getAction();
		float x = event.getX();
		float y = event.getY();
		boolean isHandle = false;

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (inCircleTouchArea(x, y)) {
				setProgress(calcProgress(calcAngle(x, y)));
				isHandle = true;
			} else {
				isHandle = false;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			setProgress(calcProgress(calcAngle(x, y)));
			isHandle = true;
			break;

		default:
			break;
		}
		return isHandle;
	}

	private boolean inCircleTouchArea(float x, float y) {
		float vectorX = x - mCircleRectF.centerX();
		float vectorY = y - mCircleRectF.centerY();
		float radius = mCircleRectF.width() / 2;
		float distance = (float) Math.sqrt(vectorX * vectorX + vectorY * vectorY);
		float touchWidth = 20;
		return (distance >= radius - touchWidth) && (distance <= radius + touchWidth);
	}

	/**
	 * 计算和向量(0,1)的夹角
	 * 
	 * @param x
	 * @param y
	 * @return 左闭右开[0,360)
	 */
	private double calcAngle(float x, float y) {
		float vectorX = x - mCircleRectF.centerX();
		float vectorY = mCircleRectF.centerY() - y;
		double temp = Math.sqrt(vectorX * vectorX + vectorY * vectorY);
		if (temp == 0) {
			return 0;
		}
		double cosValue = vectorY / temp;
		double radians = vectorX <= 0 ? Math.PI + Math.PI - Math.acos(cosValue) : Math
				.acos(cosValue);
		return Math.toDegrees(radians);
	}

	/**
	 * 根据角度计算进度 角度为0时进度为零 ，左闭右开原则
	 * 
	 * @param angle
	 *            degree单位 范围[0,360) 左闭右开
	 * @return
	 */
	private int calcProgress(double angle) {
		angle = angle % 360;
		double ratio = angle / 360;
		return (int) ((mMaxProgress + 1) * ratio);
	}

	private class Padding {
		public int left;
		public int right;
		public int top;
		public int bottom;
	}

}
