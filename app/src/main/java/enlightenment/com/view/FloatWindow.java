package enlightenment.com.view;

import android.app.Service;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.service.MediaService;

import java.lang.reflect.Field;

import enlightenment.com.base.R;

/**
 * Created by admin on 2018/7/5.
 */

public class FloatWindow implements View.OnTouchListener {
    public static String TAG = "FloatWindow";

    private static FloatWindow mFloatWindow = null;

    private Context mContext;
    private WindowManager.LayoutParams mWindowParams;
    private WindowManager mWindowManager;

    private View mFloatLayout;
    private View mCloseLayout = null;
    private TextView mCloseView;
    private ImageView mPlayView;

    private int windowID = -1;

    private int closeTime = 2000;

    private float mInViewX;
    private float mInViewY;
    private float mDownInScreenX;
    private float mDownInScreenY;
    private float mInScreenX;
    private float mInScreenY;
    private float mLastX;
    private float mLastY;
    private float mCurrentMoveX = 0;
    private float mCurrentMoveY = 0;

    private int windowHeight = 0;
    private int windowWidth = 0;

    private int mDownTime;

    private boolean isCloseFlag = false;

    private View.OnLayoutChangeListener layoutChangeListener;

    public static FloatWindow newInstance(int WindowID, Service context) {
        if (mFloatWindow != null && WindowID == mFloatWindow.getWindowID())
            return mFloatWindow;
        if (mFloatWindow != null) {
            mFloatWindow.closeFloatWindow();
            mFloatWindow = null;
        }
        mFloatWindow = new FloatWindow(context);
        mFloatWindow.setWindowID(WindowID);
        return mFloatWindow;
    }

    public static void closeWindowPlay() {
        if (mFloatWindow != null) {
            mFloatWindow.closeFloatWindow();
            mFloatWindow = null;
        }
    }

    public static void releaseWindowPlay() {
        if (mFloatWindow != null) {
            mFloatWindow.releaseFloatWindow();
            mFloatWindow = null;
        }
    }

    public FloatWindow(Service context) {
        this.mContext = context;
        initFloatWindow();
    }

    private void initFloatWindow() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (inflater == null)
            return;
        mFloatLayout = (View) inflater.inflate(R.layout.view_float_window_play, null);
        mPlayView = (ImageView) mFloatLayout.findViewById(R.id.view_float_state);
        mCloseLayout = (View) inflater.inflate(R.layout.view_flost_window_close, null);
        mCloseView = (TextView) mCloseLayout.findViewById(R.id.view_float_close);
        mPlayView.setImageDrawable(mContext.getDrawable(R.drawable.ic_audio_close));
        mPlayView.setOnTouchListener(this);

        mWindowParams = new WindowManager.LayoutParams();
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {//8.0新特性
            mWindowParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mWindowParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        mWindowParams.format = PixelFormat.RGBA_8888;
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mWindowParams.gravity = Gravity.START | Gravity.TOP;
        mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return floatLayoutTouch(motionEvent);
    }

    private boolean floatLayoutTouch(MotionEvent motionEvent) {

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownTime = (int) System.currentTimeMillis();
                // 获取相对View的坐标，即以此View左上角为原点
                mInViewX = motionEvent.getX();
                mInViewY = motionEvent.getY();
                // 获取相对屏幕的坐标，即以屏幕左上角为原点
                mDownInScreenX = motionEvent.getRawX();
                mDownInScreenY = motionEvent.getRawY() - getSysBarHeight(mContext);
                mLastX = mInScreenX = motionEvent.getRawX();
                mLastY = mInScreenY = motionEvent.getRawY() - getSysBarHeight(mContext);
                break;
            case MotionEvent.ACTION_MOVE:
                mInScreenX = motionEvent.getRawX();
                mInScreenY = motionEvent.getRawY() - getSysBarHeight(mContext);

                float moveY = mInScreenY - mLastY;
                float moveX = mInScreenX - mLastX;
                if ((int) System.currentTimeMillis() - mDownTime > closeTime) {
                    if (!isCloseFlag) {
                        Log.d(TAG, "close");
                        addCloseView();
                        setPlayPosition(motionEvent);
                    } else {
                        Log.d(TAG, "open");
                        mPlayView.layout((int) (mPlayView.getLeft() + moveX),
                                (int) (mPlayView.getTop() + moveY),
                                (int) (mPlayView.getRight() + moveX),
                                (int) (mPlayView.getBottom() + moveY));
                    }
                } else {
                    // 更新浮动窗口位置参数
                    mWindowParams.x = (int) (mInScreenX - mInViewX);
                    mWindowParams.y = (int) (mInScreenY - mInViewY);
                    // 手指移动的时候更新小悬浮窗的位置
                    mWindowManager.updateViewLayout(mFloatLayout, mWindowParams);
                    mCurrentMoveX = mCurrentMoveX + moveX;
                    mCurrentMoveY = mCurrentMoveY + moveY;
                }
                mLastX = mInScreenX;
                mLastY = mInScreenY;
                break;
            case MotionEvent.ACTION_UP:
                // 如果手指离开屏幕时，xDownInScreen和xInScreen相等，且yDownInScreen和yInScreen相等，则视为触发了单击事件。
                if (mDownInScreenX == mInScreenX && mDownInScreenY == mInScreenY) {
                    if (mContext instanceof MediaService) {
                        MediaService mediaService = (MediaService) mContext;
                        if (mediaService.getAudioState() == MediaService.SERVICE_MEDIA_STATE_PLAYING) {
                            mPlayView.setImageDrawable(mContext.getDrawable(R.drawable.ic_audio_play));
                            mediaService.onPauseAudio();
                        } else if (mediaService.getAudioState() == MediaService.SERVICE_MEDIA_STATE_PAUSE) {
                            mPlayView.setImageDrawable(mContext.getDrawable(R.drawable.ic_audio_close));
                            mediaService.onPauseAudio();
                        }
                    }
                } else {
                    if (isCloseFlag && isCloseWindow(motionEvent)) {
                        closeFloatWindow();
                    } else if (isCloseFlag) {
                        if (mCloseLayout.getParent() != null)
                            mWindowManager.removeViewImmediate(mCloseLayout);
                        updateFloatWindow(WindowManager.LayoutParams.WRAP_CONTENT,
                                WindowManager.LayoutParams.WRAP_CONTENT);
                        isCloseFlag = false;
                    }
                }
                break;
        }
        return true;
    }

    public void releaseFloatWindow() {
        mContext = null;
        hideFloatWindow();
        if (mCloseLayout.getParent() != null)
            mWindowManager.removeViewImmediate(mCloseLayout);
        isCloseFlag = false;
    }

    private void closeFloatWindow() {
        if (mContext instanceof MediaService) {
            ((MediaService) mContext).onCloseAudio();
            mContext = null;
        }
        hideFloatWindow();
        if (mCloseLayout.getParent() != null)
            mWindowManager.removeViewImmediate(mCloseLayout);
        isCloseFlag = false;
    }

    private void setPlayPosition(final MotionEvent motionEvent) {
        layoutChangeListener = new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.d(TAG, "left--" + left + "top--" + top + "right--" + right +
                        "bottom--" + bottom + "oldleft--" + oldLeft + "," + oldTop + "," + oldRight + "," + oldBottom);
                mPlayView.layout((int) (windowWidth - oldRight + mCurrentMoveX),
                        (int) (windowHeight + mCurrentMoveY),
                        (int) (windowWidth - oldRight + mCurrentMoveX + mPlayView.getWidth()),
                        (int) (windowHeight + mCurrentMoveY + mPlayView.getHeight()));
                mFloatLayout.removeOnLayoutChangeListener(layoutChangeListener);
                mCurrentMoveY = 0;
                mCurrentMoveX = 0;
            }
        };
        mFloatLayout.addOnLayoutChangeListener(layoutChangeListener);
    }

    public void showFloatWindow() {
        if (mFloatLayout.getParent() == null) {
            DisplayMetrics metrics = new DisplayMetrics();
            //默认固定位置，靠屏幕右边缘的中间
            mWindowManager.getDefaultDisplay().getMetrics(metrics);
            mWindowParams.x = windowWidth = metrics.widthPixels;
            mWindowParams.y = windowHeight = metrics.heightPixels / 2 - getSysBarHeight(mContext);
            mWindowManager.addView(mFloatLayout, mWindowParams);
        }
    }

    public void updateFloatWindow(int height, int width) {
        DisplayMetrics metrics = new DisplayMetrics();
        //默认固定位置，靠屏幕右边缘的中间
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
        mWindowParams.x = metrics.widthPixels;
        mWindowParams.y = metrics.heightPixels / 2 - getSysBarHeight(mContext);
        mWindowParams.height = height;
        mWindowParams.width = width;
        ViewGroup.LayoutParams params = mFloatLayout.getLayoutParams();
        params.width = width;
        params.height = height;
        mFloatLayout.setLayoutParams(params);
        mWindowManager.updateViewLayout(mFloatLayout, mWindowParams);
    }

    private void addCloseView() {
        if (mCloseLayout == null)
            return;
        updateFloatWindow(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        if (mCloseLayout.getParent() == null) {
            DisplayMetrics metrics = new DisplayMetrics();
            //默认固定位置，靠屏幕右边缘的中间
            mWindowManager.getDefaultDisplay().getMetrics(metrics);
            mWindowParams.x = 0;
            mWindowParams.y = metrics.heightPixels;
            mWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            mWindowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            mWindowManager.addView(mCloseLayout, mWindowParams);
            isCloseFlag = true;
        }
    }

    private boolean isCloseWindow(MotionEvent event) {
        int closeHeight = mCloseLayout.getHeight();
        float lint = mPlayView.getBottom() - mCloseLayout.getTop();
        if (lint > closeHeight / 4) {
            return true;
        }
        return false;
    }

    public void hideFloatWindow() {
        if (mFloatLayout.getParent() != null)
            mWindowManager.removeView(mFloatLayout);
    }

    public void setFloatLayoutAlpha(boolean alpha) {
        if (alpha)
            mFloatLayout.setAlpha((float) 0.5);
        else
            mFloatLayout.setAlpha(1);
    }

    // 获取系统状态栏高度
    public static int getSysBarHeight(Context contex) {
        Class<?> c;
        Object obj;
        Field field;
        int x;
        int sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = contex.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    public int getWindowID() {
        return windowID;
    }

    public void setWindowID(int windowID) {
        this.windowID = windowID;
    }

}
