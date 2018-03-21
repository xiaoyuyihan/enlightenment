package enlightenment.com.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Scroller;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import enlightenment.com.base.R;

/**
 * Created by lw on 2017/7/26.
 * 轮播滚动
 */

public class RollingLayout extends ViewGroup {
    private static final int ANTICLOCKWISE_VIEW_ROOLLING = 0;     //逆时针
    private static final int CLOCKWISE_VIEW_ROOLLING = 1;         //顺时针

    private Context context;

    private List<ImageView> imageViews = new ArrayList<>();//Image list
    private List<String> bitmaps = new ArrayList<>();
    private ImageView lastImageView;
    private ImageView nowImageView;
    private ImageView nextImageView;
    private int imageRollTime = 5000;
    private int tounchRollInterval = 2000;
    private int imageViewLocation = 1;
    private boolean imageViewLastNull = false;
    private boolean imageViewNextNull = false;
    private int imageLocation = 0;
    private boolean imageLastNull = false;      //是否为最后一张
    private boolean imageNextNull = false;      //是否为第一张

    private float currentSlippingX=0;   //当前滑动距离
    private float slippingX=0;          //总计滑动距离
    private float downX=0;

    private Scroller mScroller;

    private boolean cycleFalg = true;

    private float firstTouchX;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d("Roolling", "image----" + imageLocation + "imageView----" + imageViewLocation);

            if (msg.what == ANTICLOCKWISE_VIEW_ROOLLING) {

            } else {
                imageViewLocation = ++imageViewLocation > imageViews.size() - 1 ? 0 : imageViewLocation;
                imageLocation = ++imageLocation > bitmaps.size() - 1 ? 0 : imageLocation;
                clockwiseRotatingView(1500);
                updataImageLocation();
            }
            mHandler.sendEmptyMessageDelayed(CLOCKWISE_VIEW_ROOLLING, imageRollTime);
        }
    };

    Bitmap bitmap;

    public RollingLayout(Context context) {
        super(context);
        init(context);
    }

    public RollingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RollingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        for (int j = 0; j < getChildCount(); j++) {
            View view = getChildAt(j);
            view.layout((j - 1) * i2, i3/4, j * i2, i3*3/4);
        }
    }

    private void init(Context context) {
        this.context=context;
        addImageView(context);
        mScroller = new Scroller(context);
        //updataImageLocation();
       // mHandler.sendEmptyMessageDelayed(CLOCKWISE_VIEW_ROOLLING, tounchRollInterval);
    }

    private void addImageView(Context context) {
        for (int i = 0; i < 3; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            imageView.setAlpha(0.3f*(i+1));
            imageViews.add(imageView);
            addView(imageView);
        }
        initBitmaps();
    }

    private void initBitmaps() {
        bitmaps.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505905327879&di=31bc0f4a14a77448114e742ccdd7a6ab&imgtype=0&src=http%3A%2F%2Fimage.tianjimedia.com%2FuploadImages%2F2013%2F254%2F511V25B9DCP7_1000x500.jpg");
        bitmaps.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2460511383,2932261489&fm=27&gp=0.jpg");
        bitmaps.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2575256973,1763544917&fm=27&gp=0.jpg");
        bitmaps.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1135109127,3734588469&fm=27&gp=0.jpg");
    }

    private void clockwiseRotatingView(int time) {

        AnimatorSet animatorSet = new AnimatorSet();
        lastImageView.setX(getWidth());
        ObjectAnimator nowAnimator = ObjectAnimator.ofFloat(nowImageView, "x", -getWidth());
        ObjectAnimator nextAnimator = ObjectAnimator.ofFloat(nextImageView, "x", 0);
        animatorSet.playTogether(nowAnimator, nextAnimator);
        animatorSet.setDuration(time).start();

    }

    /***
     * 更新Image位置
     */
    private void updataImageLocation() {
        //3个ImageView 的位置设置；
        imageViewNextNull = imageViewLocation >= imageViews.size() - 1;
        imageViewLastNull = imageViewLocation == 0;
        nextImageView = imageViewNextNull ? imageViews.get(0) : imageViews.get(imageViewLocation + 1);
        lastImageView = imageViewLastNull ? imageViews.get(imageViews.size() - 1) : imageViews.get(imageViewLocation - 1);
        nowImageView = imageViews.get(imageViewLocation);
        if (bitmaps.size() > 0) {
            //对3个虚位置设置图片
            imageLastNull = imageLocation == 0;
            imageNextNull = imageLocation >= bitmaps.size() - 1;
            String lastBitmap = imageLastNull ? bitmaps.get(bitmaps.size() - 1) : bitmaps.get(imageLocation - 1);
            String nextBitmap = imageNextNull ? bitmaps.get(0) : bitmaps.get(imageLocation + 1);
            //赋予图片
            Glide.with(context).load(nextBitmap).into(nextImageView);
            Glide.with(context).load(bitmaps.get(imageLocation)).into(nowImageView);
            Glide.with(context).load(lastBitmap).into(lastImageView);
            /*nextImageView.setImageBitmap(nextBitmap);
            nowImageView.setImageBitmap(bitmaps.get(imageLocation));
            lastImageView.setImageBitmap(lastBitmap);*/
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mHandler.removeMessages(CLOCKWISE_VIEW_ROOLLING);
                downX=event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                currentSlippingX=event.getX()-downX;
                slippingX=slippingX+currentSlippingX;
                downX=event.getX();
                if (Math.abs(currentSlippingX)>12){
                    scrollBy((int)currentSlippingX,0);
                }
                break;
            case MotionEvent.ACTION_UP:
               if (Math.abs(slippingX)>12*4){
                   if (slippingX>0){
                       //右滑
                       mScroller.startScroll(getScrollX(), 0, getWidth(), 0);
                   }else {
                       //左划
                       mScroller.startScroll(getScrollX(), 0, -getWidth(), 0);
                   }
               }else {
                   //回到原来位置
                   mScroller.startScroll(getScrollX(), 0, 0, 0);
               }
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    public void setBitmaps(List<String> bitmaps) {
        this.bitmaps = bitmaps;
        updataImageLocation();
    }
}
