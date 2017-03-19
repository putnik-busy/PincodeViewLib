package com.ross.pincodeviewlib.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.animation.CycleInterpolator;

import static com.ross.pincodeviewlib.constants.Constants.MAX_RIPPLE_ALPHA;

/**
 * @author Sergey Rodionov
 */
class KeyRectView {

    private int rippleRadius;
    private int requiredRadius;
    private int circleAlpha;

    private boolean hasRippleEffect;

    private String value;

    private final View mView;
    private Rect mRect;
    private Bitmap mBitmapValue;
    private ValueAnimator mValueAnimator;
    private InterpolatedValueListener mInterpolatedValueListener;
    private RippleAnimListener mRippleAnimListener;

    KeyRectView(View view, Rect rect, String value) {
        mView = view;
        mRect = rect;
        this.value = value;
        requiredRadius = (mRect.right - mRect.left) / 4;
        setUpAnimator();
    }

    private void setUpAnimator() {
        mValueAnimator = ValueAnimator.ofFloat(0, requiredRadius);
        mValueAnimator.setDuration(400);
        final int circleAlphaOffset = MAX_RIPPLE_ALPHA / requiredRadius;
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (hasRippleEffect) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    rippleRadius = (int) animatedValue;
                    Log.i("Ripple start", "radius " + rippleRadius);
                    circleAlpha = (int) (MAX_RIPPLE_ALPHA - (animatedValue * circleAlphaOffset));
                    mInterpolatedValueListener.onValueUpdated();
                }
            }
        });

        mValueAnimator.addListener(new ValueAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                hasRippleEffect = true;
                mRippleAnimListener.onEnd();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                hasRippleEffect = false;
                rippleRadius = 0;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    void setValue(String value) {
        this.value = value;
    }

    void setBitmapValue(Bitmap bitmapValue) {
        mBitmapValue = bitmapValue;
    }

    void setError() {
        ValueAnimator goLeftAnimator = ValueAnimator.ofInt(0, 5);
        goLeftAnimator.setInterpolator(new CycleInterpolator(2));
        goLeftAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRect.left += (int) animation.getAnimatedValue();
                mRect.right += (int) animation.getAnimatedValue();
                mView.invalidate();
            }
        });
        goLeftAnimator.start();

        goLeftAnimator.addListener(new ValueAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mView.requestLayout();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    interface InterpolatedValueListener {
        void onValueUpdated();
    }

    private void setOnValueUpdateListener(InterpolatedValueListener listener) {
        mInterpolatedValueListener = listener;
    }

    void playRippleAnim(RippleAnimListener listener) {
        mRippleAnimListener = listener;
        setOnValueUpdateListener(new KeyRectView.InterpolatedValueListener() {
            @Override
            public void onValueUpdated() {
                mView.invalidate(mRect);
            }
        });
        mRippleAnimListener.onStart();
        mValueAnimator.start();
    }

    interface RippleAnimListener {
        void onStart();

        void onEnd();
    }

    public View getView() {
        return mView;
    }

    Rect getRect() {
        return mRect;
    }

    String getValue() {
        return value;
    }

    Bitmap getBitmapValue() {
        return mBitmapValue;
    }

    int getRippleRadius() {
        return rippleRadius;
    }

    int getCircleAlpha() {
        return circleAlpha;
    }

    boolean isHasRippleEffect() {
        return hasRippleEffect;
    }
}
