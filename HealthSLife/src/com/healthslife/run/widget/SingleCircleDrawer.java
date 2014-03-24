package com.healthslife.run.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class SingleCircleDrawer extends CircleDrawer {

	private CircleDrawer mDecoratedDrawer;

	public SingleCircleDrawer(RectF circleRectF) {
		super(circleRectF);
		mDecoratedDrawer = new CircleDrawer(circleRectF);
	}

	public SingleCircleDrawer(RectF circleRectF, int fullValue) {
		super(circleRectF, fullValue);
		mDecoratedDrawer = new CircleDrawer(circleRectF, fullValue);
	}

	public void setCircleRectF(RectF rectF) {
		super.setCircleRectF(rectF);
		mDecoratedDrawer.setCircleRectF(rectF);
	}

	@Override
	public void setFullValue(int fullValue) {
		super.setFullValue(fullValue);
		mDecoratedDrawer.setFullValue(getFullValue());
	}
	

	@Override
	public void draw(Canvas canvas) {
		Paint paint = new Paint(getPaint());
		paint.setColor(Color.WHITE);
		mDecoratedDrawer.setPaint(paint);
		mDecoratedDrawer.setValue(getFullValue());
		mDecoratedDrawer.draw(canvas);
		super.draw(canvas);
	}

}
