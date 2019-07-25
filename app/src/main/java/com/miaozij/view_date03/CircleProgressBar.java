package com.miaozij.view_date03;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

public class CircleProgressBar extends View {
    private int mCircleTextColor = Color.RED;
    private int mCircleTextSize = 15;
    private int mBorderWidth = 40;
    private int mTopColor = Color.RED;
    private int mBottomColor = Color.BLUE;

    //上层画笔
    private Paint mTopPaint;
    //底层画笔
    private Paint mBottomPaint;
    //文字画笔
    private Paint mTextPaint;

    private float mSweepAngle = 0;

    public CircleProgressBar(Context context) {
        this(context,null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        mBorderWidth = (int)typedArray.getDimension(R.styleable.CircleProgressBar_borderWidth,mBorderWidth);
        mBottomColor = typedArray.getColor(R.styleable.CircleProgressBar_bottomColor,mBottomColor);
        mTopColor = typedArray.getColor(R.styleable.CircleProgressBar_topColor,mTopColor);
        mCircleTextColor = typedArray.getColor(R.styleable.CircleProgressBar_topColor,mTopColor);
        mCircleTextSize = typedArray.getDimensionPixelSize(R.styleable.CircleProgressBar_circleTextSize,sp2sp(mCircleTextSize));
        typedArray.recycle();

        //初始化画笔
        mBottomPaint = getPaintByColor(mBottomColor);
        mTopPaint = getPaintByColor(mTopColor);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mCircleTextColor);
        mTextPaint.setTextSize(mCircleTextSize);
    }

    /**
     * 根据颜色不同获取画笔
     * @param color
     * @return
     */
    private Paint getPaintByColor(int color){
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(mBorderWidth);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        return paint;
    }

    private int sp2sp(int mCircleTextSize) {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,mCircleTextSize,getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量宽度和高度 保证是个圆形
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width>height?height:width,width>height?height:width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        //先画底层圆圈
        //cx 圆心x坐标 cy 圆心y座标 radius 半径
        int bottomX = getWidth() / 2 ;
        int bottomY = getHeight() / 2 ;
        int bottomRadius = getWidth() / 2 - mBorderWidth/2;
        canvas.drawCircle(bottomX,bottomY,bottomRadius,mBottomPaint);

        //画外圈
        RectF rectF = new RectF(mBorderWidth/2,mBorderWidth/2,getWidth()-mBorderWidth/2,getHeight()-mBorderWidth/2);
        canvas.drawArc(rectF,0,mSweepAngle*360,false,mTopPaint);

        int s = (int)(mSweepAngle*100);
        String text = s + "%";
        Rect rect = new Rect();
        mTextPaint.getTextBounds(text,0,text.length(),rect);
        //dx 等于控件宽度的一半 减去文字的宽度的一半
        int dx = getWidth()/2 - rect.width()/2;
        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top)/2 - fontMetricsInt.bottom;
        int baseLine = getHeight()/2 + dy;
        canvas.drawText(text,dx,baseLine,mTextPaint);

    }

    public void setSweepAngle(float sweepAngle){
        this.mSweepAngle = sweepAngle;
        invalidate();
    }
}
