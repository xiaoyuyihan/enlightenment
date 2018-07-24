package enlightenment.com.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import enlightenment.com.base.R;

/**
 * Created by lw on 2017/9/16.
 */

public class ClipImageLayout extends RelativeLayout {
    private ZoomImageView mZoomImageView;
    private ClipImageBorderView mClipImageView;
    private Drawable mDrawable;

    /**
     * 这里测试，直接写死了大小，真正使用过程中，可以提取为自定义属性
     */
    private float mHorizontalPadding = 20;

    private float mViewProportion=1/3;

    public ClipImageLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ClipImageLayout);
        mHorizontalPadding=array.getDimension(R.styleable.ClipImageLayout_clipPadding,20);
        mViewProportion = array.getFloat(R.styleable.ClipImageLayout_clipProportion,1);
        mDrawable=array.getDrawable(R.styleable.ClipImageLayout_clipDrawable);
        array.recycle();

        mZoomImageView = new ZoomImageView(context);
        mClipImageView = new ClipImageBorderView(context);

        android.view.ViewGroup.LayoutParams lp = new LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        /**
         * 这里测试，直接写死了图片，真正使用过程中，可以提取为自定义属性
         */
        mZoomImageView.setImageDrawable(mDrawable);

        this.addView(mZoomImageView, lp);
        this.addView(mClipImageView, lp);


        // 计算padding的px
        mHorizontalPadding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
                        .getDisplayMetrics());
        mZoomImageView.setHorizontalPadding((int)mHorizontalPadding);
        mZoomImageView.setViewProportion(mViewProportion);
        mClipImageView.setHorizontalPadding((int)mHorizontalPadding);
        mClipImageView.setViewProportion(mViewProportion);
    }

    /**
     * 对外公布设置边距的方法,单位为dp
     *
     * @param mHorizontalPadding
     */
    public void setHorizontalPadding(int mHorizontalPadding)
    {
        this.mHorizontalPadding = mHorizontalPadding;
    }

    /**
     * 裁切图片
     *
     * @return
     */
    public Bitmap clip()
    {
        return mZoomImageView.clip();
    }

    public ZoomImageView getZoomImageView() {
        return mZoomImageView;
    }
}
