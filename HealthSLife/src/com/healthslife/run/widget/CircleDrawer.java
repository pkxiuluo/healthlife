package com.healthslife.run.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;

public class CircleDrawer {

	private static Paint DEFAULT_PAINT ;
	public static final int DEFAULT_STROKE_WIDTH = 30;
	// 87,135,182
	public static final int LIGHT_BLUE_COLOR = Color.rgb(0x68, 0xa2, 0xda);
	// 66,102,138
	public static final int DARK_BLUE_COLOR = Color.rgb(0x42, 0x66, 0x8a);
	static {
		DEFAULT_PAINT= new Paint();
		DEFAULT_PAINT.setAntiAlias(true); // 设置画笔为抗锯齿
		DEFAULT_PAINT.setColor(LIGHT_BLUE_COLOR);// 天蓝色
		DEFAULT_PAINT.setStyle(Style.STROKE);
		DEFAULT_PAINT.setStrokeWidth(DEFAULT_STROKE_WIDTH); // 线宽
	}

	private Paint mPaint =new Paint(DEFAULT_PAINT) ;
	private RectF mCircleRectF =new RectF();
	private RectF mCircleDrawRectF = new RectF();
	private int mFullValue = 100;
	private int mValue = 0;

	public CircleDrawer(RectF circleRectF) {
		this(circleRectF,100);
	}
	
	public CircleDrawer(RectF circleRectF, int fullValue){
		this.mCircleRectF = circleRectF;
		this.mFullValue = fullValue;
		
	}

	public void setCircleRectF(RectF rectF) {
		this.mCircleRectF = rectF;
		
	}
	public void setPaint(Paint paint){
		mPaint =new Paint(paint);
	}

	public void setPaint(int color) {
		setPaint(color, DEFAULT_STROKE_WIDTH);
	}
	public void setPaintStrokeWidth(int width) {
		mPaint.setStrokeWidth(width);
	}
	public Paint getPaint(){
		return mPaint;
	}

	public void setPaint(int color, int strokeWidth) {
		mPaint.setColor(color);// 天蓝色
		mPaint.setStrokeWidth(strokeWidth); // 线宽
	}

	/**
	 * 不论改变Paint的线宽（StrokeWidth）还是改变CircleRectF的大小位置都要调用该方法，重新设定CircleDrawRectF
	 */
	private void resetCircleDrawRectF() {
		float half = mPaint.getStrokeWidth() / 2;
		mCircleDrawRectF = new RectF(mCircleRectF.left + half, mCircleRectF.top + half,
				mCircleRectF.right - half, mCircleRectF.bottom - half);
	}

	public int getFullValue() {
		return mFullValue;
	}

	public void setFullValue(int fullValue) {
		this.mFullValue = fullValue;
	}

	public int getValue() {
		return mValue;
	}

	public void setValue(int value) {
		this.mValue = value;
	}

	public void draw(Canvas canvas) {
		resetCircleDrawRectF();
		System.out.println(mCircleDrawRectF.width());
		System.out.println(mPaint.getColor());
		canvas.drawArc(mCircleDrawRectF, -90, ((float) mValue / mFullValue) * 360, false, mPaint); // 绘制进度圆弧，这里是蓝色
	}

}
