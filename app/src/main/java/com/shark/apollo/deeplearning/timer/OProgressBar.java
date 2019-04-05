package com.shark.apollo.deeplearning.timer;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.shark.apollo.deeplearning.R;

public class OProgressBar extends View {

    private static final String STR_BASIC = "00:00";
    private static final int DEFAULT_MAX = 1500;
    private static final int DEFAULT_RADIUS = 120;

    private int mAlpha;
    private int mRadius;
    private int mRadiusOuter;
    private int mProgressMax;
    private int mBackgroundColor;
    private int mProgressColor;
    private int mProgressTextColor;
    private int mProgressWidth;
    private int mLastProgress;
    private float mProgress;
    private float mProgressTextSize;
    private boolean isAnimating;
    private boolean isOpenDiffusing;

    private String mStrText = "Start";
    private Paint mPaint;
    private RectF mRectF;
    private Rect mRectText;
    private BlurMaskFilter mMaskFilter;
    private ValueAnimator animatorDiffusing;
    private ValueAnimator animatorAlpha;
    private ValueAnimator mAnimatorProgress;

    public OProgressBar(Context context) {
        this(context, null);
    }

    public OProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mRectF = new RectF();
        mRectText = new Rect();

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.OProgressBar,
                defStyleAttr, 0);
        mProgress = a.getInt(R.styleable.OProgressBar_progress, 0);
        mProgressMax = a.getInt(R.styleable.OProgressBar_progressMax, DEFAULT_MAX);
        mRadius =  a.getDimensionPixelOffset(R.styleable.OProgressBar_progressRadius,
                DEFAULT_RADIUS);
        mBackgroundColor = a.getColor(R.styleable.OProgressBar_progressBackgroundColor,
                ContextCompat.getColor(getContext(), R.color.colorLightGray));
        mProgressColor = a.getColor(R.styleable.OProgressBar_progressColor,
                ContextCompat.getColor(getContext(), R.color.colorAccent));
        mProgressTextColor = a.getColor(R.styleable.OProgressBar_progressTextColor,
                ContextCompat.getColor(getContext(), R.color.colorLightGray));
        mProgressTextSize = a.getDimension(R.styleable.OProgressBar_progressTextSize,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 24, getResources()
                        .getDisplayMetrics()));
        mProgressWidth = a.getDimensionPixelSize(R.styleable.OProgressBar_progressWidth, 6);
        a.recycle();
        mRadiusOuter = mRadius;
        mMaskFilter = new BlurMaskFilter(24, BlurMaskFilter.Blur.SOLID);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width;
        int height;
        int measuredSize = MeasureSpec.getSize(widthMeasureSpec);
        int measuredMode = MeasureSpec.getMode(widthMeasureSpec);

        if(measuredMode == MeasureSpec.EXACTLY) {
            width = measuredSize;
        } else {
            width = 2 * (mRadius + mProgressWidth);
        }

        measuredSize = MeasureSpec.getSize(heightMeasureSpec);
        measuredMode = MeasureSpec.getMode(heightMeasureSpec);
        if(measuredMode == MeasureSpec.EXACTLY) {
            height = measuredSize;
        } else {
            height = 2 * (mRadius + mProgressWidth);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerPointX = getMeasuredWidth() / 2;
        int centerPointY = getMeasuredHeight() / 2;
        mPaint.setColor(mBackgroundColor);
        mPaint.setStrokeWidth(mProgressWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        canvas.drawCircle(centerPointX, centerPointY, mRadius, mPaint);

        if(isOpenDiffusing) {
            mPaint.setStrokeWidth(mProgressWidth);
            mPaint.setAlpha(mAlpha);
            canvas.drawCircle(centerPointX, centerPointY, mRadiusOuter, mPaint);
        }

        mPaint.setStrokeWidth(mProgressWidth * 3 / 2);
        mPaint.setAlpha(255);
        mPaint.setColor(mProgressColor);
        mPaint.setMaskFilter(mMaskFilter);
        mRectF.set(centerPointX - mRadius, centerPointY - mRadius, centerPointX + mRadius,
                centerPointY + mRadius);
        canvas.drawArc(mRectF, -90, 360 * (mProgress / mProgressMax), false, mPaint);

        mPaint.setMaskFilter(null);
        mPaint.setColor(mProgressTextColor);
        mPaint.setTextSize(mProgressTextSize);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.getTextBounds(STR_BASIC, 0, STR_BASIC.length(), mRectText);
        Paint.FontMetricsInt fontMetricsInt = mPaint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetricsInt.bottom + fontMetricsInt.top) / 2
                - fontMetricsInt.top;
        canvas.drawText(mStrText, getMeasuredWidth() / 2 - mRectText.width()/ 2, baseline, mPaint);
    }

    public void setProgress(int progress) {
        if(progress < 0 || progress == mProgress) {
            return;
        } else if(progress > mProgressMax) {
            progress = mProgressMax;
        }
        mProgress = progress;
        startProgressAnim(progress);
        if(isOpenDiffusing && !isAnimating) {
            initDiffusingAnim();
            startDiffusionAnim();
        }
        postInvalidate();
    }

    public void openDiffusing() {
        isOpenDiffusing = true;
    }

    private void initDiffusingAnim() {
        animatorDiffusing = ValueAnimator.ofInt(mRadiusOuter, mRadiusOuter + 240);
        animatorDiffusing.addUpdateListener(animation -> {
            OProgressBar.this.mRadiusOuter = (int) animation.getAnimatedValue();
            postInvalidate();
        });
        animatorDiffusing.setDuration(2400);
        animatorDiffusing.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorDiffusing.setRepeatCount(ValueAnimator.INFINITE);
        animatorDiffusing.setRepeatMode(ValueAnimator.RESTART);

        animatorAlpha = ValueAnimator.ofInt(255, 0);
        animatorAlpha.addUpdateListener(animation ->
                OProgressBar.this.mAlpha = (int) animation.getAnimatedValue());
        animatorAlpha.setDuration(2400);
        animatorAlpha.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorAlpha.setRepeatCount(ValueAnimator.INFINITE);
        animatorAlpha.setRepeatMode(ValueAnimator.RESTART);
    }

    private void startDiffusionAnim() {
        isAnimating = true;
        animatorDiffusing.start();
        animatorAlpha.start();
    }

    public void endDiffusionAnim() {
        if(isOpenDiffusing) {
            animatorDiffusing.end();
            animatorAlpha.end();
            mRadiusOuter = mRadius;
            isAnimating = false;
        }
    }

    public void closeDiffusing() {
        isOpenDiffusing = false;
    }

    public boolean isOpenDiffusing() {
        return isOpenDiffusing;
    }

    private void startProgressAnim(int progress) {
        mAnimatorProgress = ValueAnimator.ofFloat(mLastProgress, progress);
        mLastProgress = progress;
        mAnimatorProgress.addUpdateListener(animation -> {
            OProgressBar.this.mProgress = (float) animation.getAnimatedValue();
            if(mProgress == mProgressMax) {
                mLastProgress = 0;
            }
            postInvalidate();
        });
        mAnimatorProgress.setDuration(980);
        mAnimatorProgress.setInterpolator(new LinearInterpolator());
        mAnimatorProgress.start();
    }

    public void stopProgressAnim() {
        mAnimatorProgress.cancel();
        mLastProgress = 0;
    }

    public void clearProgress() {
        mProgress = 0;
        mStrText = "Start";
        postInvalidate();
    }

    public void setMax(int max) {
        mProgressMax = max;
    }

    public int getMax() {
        return mProgressMax;
    }

    public void setText(String text) {
        mStrText = text;
        postInvalidate();
    }
}