package enlightenment.com.view.PopupWindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import enlightenment.com.base.R;

/**
 * Created by admin on 2018/6/5.
 */

public class CusPopupWindow extends PopupWindow {

    public static class Builder {
        public int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        public int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        public boolean focusable = true;       //
        public boolean outsideTouchable = true; //外部监听
        public boolean touchable = true;        //监听
        public Context context;
        public Drawable background;
        public OnCusPopupListener onCusPopupListener;
        public View view;

        public static Builder getInstance(Context context) {
            return new Builder(context);
        }

        public Builder(Context context) {
            this.context = context;
            this.background = new ColorDrawable(
                    context.getResources().getColor(R.color.white));
        }

        public OnCusPopupListener getOnCusPopupLinener() {
            return onCusPopupListener;
        }

        public Builder setOnCusPopupLinener(OnCusPopupListener onCusPopupListener) {
            this.onCusPopupListener = onCusPopupListener;
            return this;
        }


        public boolean isTouchable() {
            return touchable;
        }

        public Builder setTouchable(boolean touchable) {
            this.touchable = touchable;
            return this;
        }

        public Context getContext() {
            return context;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Drawable getBackground() {
            return background;
        }

        public Builder setBackground(Drawable background) {
            this.background = background;
            return this;
        }

        public View getView() {
            return view;
        }

        public Builder setView(int view) {
            this.view = LayoutInflater.from(context).inflate(view,null);
            return this;
        }

        public int getWidth() {

            return width;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public int getHeight() {
            return height;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public boolean isFocusable() {
            return focusable;
        }

        public Builder setFocusable(boolean focusable) {
            this.focusable = focusable;
            return this;
        }

        public boolean isOutsideTouchable() {
            return outsideTouchable;
        }

        public Builder setOutsideTouchable(boolean outsideTouchable) {
            this.outsideTouchable = outsideTouchable;
            return this;
        }

        public CusPopupWindow builder() {
            return new CusPopupWindow(view, width, height, focusable, outsideTouchable,
                    touchable, background, onCusPopupListener);
        }
    }

    public interface OnCusPopupListener {
        void onCusPopupListener(View view);
    }

    public CusPopupWindow(View view, int width, int height,
                          boolean focusable, boolean outsideTouchable,
                          boolean touchable, Drawable background,
                          OnCusPopupListener onCusPopupListener) {
        if (onCusPopupListener != null)
            onCusPopupListener.onCusPopupListener(view);
        setContentView(view);
        setWidth(width);
        setHeight(height);
        setFocusable(focusable);
        setOutsideTouchable(outsideTouchable);
        setTouchable(touchable);
        //setAnimationStyle(null);
        setBackgroundDrawable(background);
    }

    /**
     * 水平方向：
     ALIGN_LEFT：在锚点内部的左边；
     ALIGN_RIGHT：在锚点内部的右边；
     CENTER_HORI：在锚点水平中部；
     TO_RIGHT：在锚点外部的右边；
     TO_LEFT：在锚点外部的左边。
     垂直方向：
     ALIGN_ABOVE：在锚点内部的上方；
     ALIGN_BOTTOM：在锚点内部的下方；
     CENTER_VERT：在锚点垂直中部；
     TO_BOTTOM：在锚点外部的下方；
     TO_ABOVE：在锚点外部的上方。
     * @param anchor
     * @param x
     * @param y
     * @param gravity
     */

}
