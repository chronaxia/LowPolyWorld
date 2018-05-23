package com.chronaxia.lowpolyworld.view.custom;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by 一非 on 2018/5/2.
 */

public class ZoomImageView extends android.support.v7.widget.AppCompatImageView implements ViewTreeObserver.OnGlobalLayoutListener,
        ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener {

    private boolean Ones = false;

    /**
     * 初始化时缩放的值
     */
    private float mInitscale;

    /**
     * 双击放大时缩放的值
     */
    private float mMidscale;
    /**
     * 缩放的最大值
     */
    private float mMaxscale;

    /**
     * 矩阵
     */
    private Matrix mScaleMatrix;

    /**
     * 捕获用户多点触碰
     */
    private ScaleGestureDetector mScaleGestureDetector;

    // -------------自由移动-----------------

    /**
     * 记录上一次多点触碰的数量
     */
    private int mLastPointerCount;

    /**
     * 记录上一次中心点X坐标
     */
    private float mLastX;

    /**
     * 记录上一次中心点y坐标
     */
    private float mLasty;

    /**
     * 系统给标准值
     */
    private int mTouchSlop;

    /**
     * 是否可以移動
     */
    private boolean isCanDrag;

    /**
     * 是否检查左边界和右边界
     */
    private boolean isCleckLeftAndRight;

    /**
     * 是否检查上边界和下边界
     */
    private boolean isCleckTopAndBottom;

    // -------------双击放大与缩小--------
    private GestureDetector mGestureDetector;

    private boolean isAutoScale;


    public ZoomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        // 初始化矩阵
        mScaleMatrix = new Matrix();
        setScaleType(ScaleType.MATRIX);

        // 初始化多点触碰
        mScaleGestureDetector = new ScaleGestureDetector(context, this);

        setOnTouchListener(this);
        // 双击初始化
        mGestureDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {

                        if (isAutoScale) {
                            return true;
                        }
                        float x = e.getX();
                        float y = e.getY();

                        if (getScale() < mMidscale) {
                            postDelayed(new AutoScaleRunnable(mMidscale, x, y), 16);
                            isAutoScale = true;
                        } else {
                            postDelayed(new AutoScaleRunnable(mInitscale, x, y), 16);
                            isAutoScale = true;
                        }
                        return true;
                    }
                });
    }

    public boolean myDoScale(MotionEvent e, float midscale) {
        if (isAutoScale) {
            return true;
        }
        float x = e.getX();
        float y = e.getY();

        if (getScale() < midscale) {
            postDelayed(new AutoScaleRunnable(midscale, x, y), 16);
            isAutoScale = true;
        } else {
            postDelayed(new AutoScaleRunnable(mInitscale, x, y), 16);
            isAutoScale = true;
        }
        return true;
    }

    /**
     * @author girl
     *         自动放大与缩小
     */
    private class AutoScaleRunnable implements Runnable {
        /**
         * 目标的缩放值
         */
        private float mTargetScale;
        /**
         * 目标缩放的x点坐标
         */
        private float x;
        /**
         * 目标缩放的y点坐标
         */
        private float y;

        /**
         * 放大的固定值
         */
        private final float BIGGER = 1.07f;
        /**
         * 缩小的固定值
         */
        private final float SMALL = 0.93f;

        /**
         * 存放临时的变量
         */
        private float tmpScale;


        public AutoScaleRunnable(float mTargetScale, float x, float y) {
            this.mTargetScale = mTargetScale;
            this.x = x;
            this.y = y;

            if (getScale() < mTargetScale) {
                tmpScale = BIGGER;
            }

            if (getScale() > mTargetScale) {

                tmpScale = SMALL;
            }
        }

        @Override
        public void run() {
            //进行缩放
            mScaleMatrix.postScale(tmpScale, tmpScale, x, y);
            checkBorderAndCenterWhenScale();
            setImageMatrix(mScaleMatrix);
            float currentScale = getScale();
            if ((tmpScale > 1.0f && currentScale < mTargetScale) || (tmpScale < 1.0f && currentScale > mTargetScale)) {
                postDelayed(this, 16);
            } else {//设置为我们的目标值
                float scale = mTargetScale / currentScale;
                mScaleMatrix.postScale(scale, scale, x, y);
                checkBorderAndCenterWhenScale();
                setImageMatrix(mScaleMatrix);

                isAutoScale = false;
            }
        }


    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageView(Context context) {
        this(context, null);
    }

    /*
     * (non-Javadoc) 当此view附加到窗体上时调用该方法（）
     *
     * @see android.widget.ImageView#onAttachedToWindow()
     */
    @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    /*
     * (non-Javadoc)将视图从窗体上分离的时候调用该方法
     *
     * @see android.widget.ImageView#onDetachedFromWindow()
     */
    @SuppressWarnings("deprecation")
    @Override
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        super.onDetachedFromWindow();
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    /**
     * 800 --- 1600 | | ---------- | | 1000 | |500 | | ---------- --- 500/1000
     * =0.5
     */
    /*
     * (non-Javadoc)获取Imageview加载完成的图片
    *
    * @see
    * android.view.ViewTreeObserver.OnGlobalLayoutListener#onGlobalLayout()
    */
    /*
     * (non-Javadoc)
    *
    * @see
    * android.view.ViewTreeObserver.OnGlobalLayoutListener#onGlobalLayout()
    */
    @Override
    public void onGlobalLayout() {

        if (!Ones) {
            // 获得父类的宽和高
            int width = getWidth();
            int height = getHeight();

            // 获得图片以及图片的宽和高
            Drawable drawable = getDrawable();
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();

            // 计算缩放的比值

            float scale = 1.0f;

            // 图片的宽大于控件的宽，高小于控件的高，缩放宽
            if (intrinsicWidth > width && intrinsicHeight < height) {
                scale = width * 1.0f / intrinsicWidth;
            }
            // 图片的高大于控件的高，宽小于控件的宽，缩放高
            if (intrinsicHeight > height && intrinsicWidth < width) {
                scale = height * 1.0f / intrinsicHeight;
            }
            // 同大同小，则缩放宽高的最小值
            if ((intrinsicWidth < width && intrinsicHeight < height)
                    || (intrinsicWidth > width && intrinsicHeight > height)) {
                scale = Math.min(width * 1.0f / intrinsicWidth, height * 10f
                        / intrinsicHeight);
            }
            /**
             * 初始化缩放比例
             */
            mInitscale = scale;
            mMidscale = mInitscale * 2;
            mMaxscale = mInitscale * 4;

            // 将图片移动到控件的中心
            int dx = getWidth() / 2 - intrinsicWidth / 2;
            int dy = getHeight() / 2 - intrinsicHeight / 2;

            // 初始化平移缩放
            mScaleMatrix.postTranslate(dx, dy);
            mScaleMatrix.postScale(mInitscale, mInitscale, width / 2,
                    height / 2);
            setImageMatrix(mScaleMatrix);

            Ones = true;
        }

    }

    /**
     * 获取当前图片的缩放值
     *
     * @return
     */
    public float getScale() {
        float[] values = new float[9];
        mScaleMatrix.getValues(values);
        return values[Matrix.MSCALE_X];

    }

    /*
     * (non-Javadoc)縮放中
     *
     * @see
     * android.view.ScaleGestureDetector.OnScaleGestureListener#onScale(android
     * .view.ScaleGestureDetector)
     */
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        // 获取当前图片的缩放值
        float scale = getScale();
        // 拿到缩放的值
        float scaleFactor = detector.getScaleFactor();

        if (getDrawable() == null)
            return true;

        if ((scale < mMaxscale && scaleFactor > 1.0f)
                || (scale > mInitscale && scaleFactor < 1.0f)) {

            if (scale * scaleFactor < mInitscale) {
                scaleFactor = mInitscale / scale;
            }

            if (scale * scaleFactor > mMaxscale) {
                scaleFactor = mMaxscale / scale;
            }
            // 以多点的中心点缩放
            mScaleMatrix.postScale(scaleFactor, scaleFactor,
                    detector.getFocusX(), detector.getFocusY());

            checkBorderAndCenterWhenScale();
            setImageMatrix(mScaleMatrix);
        }

        return false;
    }

    /**
     * 获取放大缩小后的图片的四点坐标和位置
     *
     * @return
     */
    private RectF getMatrixRectF() {
        Matrix matrix = mScaleMatrix;
        RectF rectF = new RectF();

        Drawable d = getDrawable();

        if (d != null) {
            rectF.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rectF);
        }

        return rectF;
    }

    /**
     * 在缩放的时候进行边界控制以及位置控制
     */
    private void checkBorderAndCenterWhenScale() {

        RectF rectF = getMatrixRectF();

        float deltaX = 0;
        float deltaY = 0;

        int width = getWidth();
        int height = getHeight();
        // 缩放时进行边界检测，防止出现白边
        if (rectF.width() >= width) {
            if (rectF.left > 0) {
                deltaX = -rectF.left;
            }

            if (rectF.right < width) {
                deltaX = width - rectF.right;
            }
        }

        if (rectF.height() >= height) {
            if (rectF.top > 0) {
                deltaY = -rectF.top;
            }
            if (rectF.bottom < height) {
                deltaY = height - rectF.bottom;
            }
        }

        // 如何高度或者宽度小于控件的高度或者宽度要居中显示

        if (rectF.width() < width) {
            deltaX = width / 2f - rectF.right + rectF.width() / 2f;
        }

        if (rectF.height() < height) {
            deltaY = height / 2f - rectF.bottom + rectF.height() / 2f;
        }

        mScaleMatrix.postTranslate(deltaX, deltaY);
    }

    /*
     * (non-Javadoc)縮放開始
     *
     * @see
     * android.view.ScaleGestureDetector.OnScaleGestureListener#onScaleBegin
     * (android.view.ScaleGestureDetector)
     */
    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        // TODO Auto-generated method stub
        return true;
    }

    /*
     * (non-Javadoc)縮放結束
     *
     * @see
     * android.view.ScaleGestureDetector.OnScaleGestureListener#onScaleEnd(android
     * .view.ScaleGestureDetector)
     */
    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        // 将双击事件传给GestureDetector处理
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }
/*
        // 將事件传给ScaleGestureDetector处理
        mScaleGestureDetector.onTouchEvent(event);
        // 用于记录多点触碰的中心点
        float x = 0;
        float y = 0;
        // 多点触碰的数量
        int pointerCount = event.getPointerCount();

        for (int i = 0; i < pointerCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }

        x /= pointerCount;
        y /= pointerCount;

        if (mLastPointerCount != pointerCount) {
            isCanDrag = false;
            mLastX = x;
            mLasty = y;
        }
        mLastPointerCount = pointerCount;
        RectF rectF1 = getMatrixRectF();
        switch (event.getAction()) {

            case MotionEvent.ACTION_MOVE:

                float dx = x - mLastX;
                float dy = y - mLasty;

                if (!isCanDrag) {
                    isCanDrag = isMoveAction(dx, dy);
                }

                if (isCanDrag) {

                    RectF rectF = getMatrixRectF();

                    if (getDrawable() != null) {
                        isCleckLeftAndRight = isCleckTopAndBottom = true;

                        // 如何图片宽度小于控件宽度，不允许横向移动
                        if (rectF.width() < getWidth()) {
                            isCleckLeftAndRight = false;
                            dx = 0;
                        }
                        // 如果图片高度小于控件高度，不允许上下移动
                        if (rectF.height() < getHeight()) {
                            isCleckTopAndBottom = false;
                            dy = 0;
                        }

                        mScaleMatrix.postTranslate(dx, dy);
                        checkBorderWhenTranslate();
                        setImageMatrix(mScaleMatrix);
                    }

                    if (rectF1.width() > getWidth() + 0.01 || rectF1.height() > getHeight() + 0.01) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                mLastX = x;
                mLasty = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mLastPointerCount = 0;
                break;
            default:
                break;
        }*/
        return true;
    }

    /**
     * 当移动时进行边界检查
     */
    private void checkBorderWhenTranslate() {
        RectF rectF = getMatrixRectF();

        float deltaX = 0;
        float deltaY = 0;

        int width = getWidth();
        int height = getHeight();

        if (rectF.top > 0 && isCleckTopAndBottom) {
            deltaY = -rectF.top;
        }

        if (rectF.bottom < height && isCleckTopAndBottom) {
            deltaY = height - rectF.bottom;
        }

        if (rectF.left > 0 && isCleckLeftAndRight) {
            deltaX = -rectF.left;
        }
        if (rectF.right < width && isCleckLeftAndRight) {
            deltaX = width - rectF.right;
        }

        mScaleMatrix.postTranslate(deltaX, deltaY);
    }

    private boolean isMoveAction(float dx, float dy) {
        // 算平方根
        return Math.sqrt(dx * dx + dy * dy) > mTouchSlop;
    }

}
