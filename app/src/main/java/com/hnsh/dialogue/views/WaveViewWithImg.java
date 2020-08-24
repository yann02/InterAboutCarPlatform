package com.hnsh.dialogue.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.nfc.Tag;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.dosmono.logger.Logger;
import com.hnsh.dialogue.R;
import com.hnsh.dialogue.utils.DpWithPxUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 水波纹特效
 * Created by fbchen2 on 2016/5/25.
 */
public class WaveViewWithImg extends View {
    private float mInitialRadius;   // 初始波纹半径
    private float mMaxRadius;   // 最大波纹半径
    private long mDuration = 2000; // 一个波纹从创建到消失的持续时间
    private int mSpeed = 500;   // 波纹的创建速度，每500ms创建一个
    private float mMaxRadiusRate = 0.85f;
    private boolean mMaxRadiusSet;

    private boolean mIsRunning;

    private long mLastCreateTime;
    private List<Circle> mCircleList = new ArrayList<Circle>();

    private Runnable mCreateCircle = new Runnable() {
        @Override
        public void run() {
            if (mIsRunning) {
                newCircle();
                postDelayed(mCreateCircle, mSpeed);
            }
        }
    };

    private Interpolator mInterpolator = new LinearInterpolator();

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    private Bitmap mBitmap;

    private Integer mDrawableResource;
    private Integer mColorResource;

    public WaveViewWithImg(Context context) {
        super(context);
    }

    public WaveViewWithImg(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaveViewWithImg);
        mDrawableResource = typedArray.getResourceId(R.styleable.WaveViewWithImg_src, R.mipmap.ic_left_record);
        mColorResource = typedArray.getColor(R.styleable.WaveViewWithImg_color, 0x4992E6);
        mPaint.setColor(mColorResource);
        mCirclePaint.setAntiAlias(true);//抗锯齿
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
        typedArray.recycle();
    }

    public void setStyle(Paint.Style style) {
        mPaint.setStyle(style);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (!mMaxRadiusSet) {
            mMaxRadius = Math.min(w, h) * mMaxRadiusRate / 2.0f;
        }
    }

    public void setMaxRadiusRate(float maxRadiusRate) {
        mMaxRadiusRate = maxRadiusRate;
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }


    /**
     * 开始
     */
    public void start() {
        if (mIsRunning) {
            stopImmediately();
        }
        mIsRunning = true;
        mCreateCircle.run();
    }

    /**
     * 缓慢停止
     */
    public void stop() {
        mIsRunning = false;
    }

    /**
     * 立即停止
     */
    public void stopImmediately() {
        mIsRunning = false;
        mCircleList.clear();
        invalidate();
    }


    public void click() {
        if (mIsRunning) {
            stopImmediately();
        } else {
            start();
        }
    }

    public boolean isStop() {
        return !this.mIsRunning;
    }


    protected void onDraw(Canvas canvas) {
        Logger.d("onDraw  mCircleList.size()==="+mCircleList.size());
        Iterator<Circle> iterator = mCircleList.iterator();
        while (iterator.hasNext()) {
            Circle circle = iterator.next();
            float radius = circle.getCurrentRadius();
            if (System.currentTimeMillis() - circle.mCreateTime < mDuration) {
                mPaint.setAlpha(circle.getAlpha());
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, mPaint);
            } else {
                iterator.remove();
            }
        }
        if (mCircleList.size() > 0) {
            postInvalidateDelayed(10);
        }
        Logger.d("getWidth()====="+getWidth());
        Logger.d("getHeight()====="+getHeight());
        @SuppressLint("DrawAllocation") Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mDrawableResource);
//        RectF rectF = new RectF(26, 26, 185, 185);
//        RectF rectF = new RectF(44, 44, 172, 172);
        RectF rectF = new RectF(DpWithPxUtil.dip2px(getContext(),12f), DpWithPxUtil.dip2px(getContext(),12f), DpWithPxUtil.dip2px(getContext(),44f), DpWithPxUtil.dip2px(getContext(),44f));
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG));
        canvas.drawBitmap(bitmap, null, rectF, new Paint());
    }

    public void setInitialRadius(float radius) {
        mInitialRadius = radius;
    }

    public void setDuration(long duration) {
        mDuration = duration;
    }

    public void setMaxRadius(float maxRadius) {
        mMaxRadius = maxRadius;
        mMaxRadiusSet = true;
    }

    public void setSpeed(int speed) {
        mSpeed = speed;
    }

    private void newCircle() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastCreateTime < mSpeed) {
            return;
        }
        Circle circle = new Circle();
        mCircleList.add(circle);
        invalidate();
        mLastCreateTime = currentTime;
    }

    private class Circle {
        private long mCreateTime;

        Circle() {
            mCreateTime = System.currentTimeMillis();
        }

        int getAlpha() {
            float percent = (getCurrentRadius() - mInitialRadius) / (mMaxRadius - mInitialRadius);
//            Log.e("huanghuang","per:"+percent + "  mInterpolator.getInterpolation(percent):"+ mInterpolator.getInterpolation(percent));
            float dd = mInterpolator.getInterpolation(percent);
            if (dd < 0.5) {
                dd = 0.5f;
            }
            return (int) (255 - percent * 255);
        }

        float getCurrentRadius() {
            float percent = (System.currentTimeMillis() - mCreateTime) * 1.0f / mDuration;
            return mInitialRadius + mInterpolator.getInterpolation(percent) * (mMaxRadius - mInitialRadius);
        }
    }

    public void setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
        if (mInterpolator == null) {
            mInterpolator = new LinearInterpolator();
        }
    }

}
