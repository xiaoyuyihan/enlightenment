package com.edit.automatic;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by lw on 2018/1/20.
 */

public class ToolLinearLayout extends LinearLayout {
    private int mHeight = 0;
    private float mDownY = 0;
    private float mLastY = 0;
    private float mTranslationY = 0;
    private float mTranslationHeight = 0;
    private boolean isFlag = false;

    public ToolLinearLayout(Context context) {
        super(context);
    }

    public ToolLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ToolLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mHeight == 0) {
            mHeight = getHeight();
            mTranslationHeight = mHeight - getChildAt(0).getHeight();
            mTranslationY = getTranslationY();
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isFlag)
                    startUpAnimator();
                else
                    startDownAnimator();
                isFlag = !isFlag;
                break;
        }
        return super.onTouchEvent(event);
    }

    private void startDownAnimator() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "translationY", getTranslationY(), 0);
        animator.setDuration(1000);
        animator.start();
    }

    private void startUpAnimator() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "translationY", getTranslationY(), -mTranslationHeight);
        animator.setDuration(1000);
        animator.start();
    }

    private void startMoveAnimator(int move) {
        layout(getLeft(), getTop() + move, getRight(), getBottom() + move);
    }
}
