package enlightenment.com.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnTextChanged;
import enlightenment.com.base.R;
import enlightenment.com.operationBean.CarouselBean;

/**
 * Created by lw on 2017/9/24.
 */

public class CarouselView extends FrameLayout implements View.OnTouchListener {

    private Context context;

    private int mDownX = 0;
    private int count = 0;

    private ViewPager viewPager;
    private LinearLayout carouselLayout;
    private CarouselAdapter adapter;

    private boolean carouselFlag = false;
    private List<CarouselBean> carouselBeanList = new ArrayList<>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!carouselFlag) {
                count = viewPager.getCurrentItem();
                ((ImageView) carouselLayout.getChildAt(count)).setImageDrawable(getResources().getDrawable(R.drawable.ic_spot));
                Log.d("Carousel", "Carousel count : " + count);
                if (count >= carouselBeanList.size() - 1) {
                    count = 0;
                    viewPager.setCurrentItem(count, false);

                } else {
                    count = count + 1;
                    viewPager.setCurrentItem(count);
                }
                ((ImageView) carouselLayout.getChildAt(count)).setImageDrawable(getResources().getDrawable(R.drawable.ic_spot_current));
            }
            mHandler.sendEmptyMessageDelayed(1, 3000);
        }
    };

    private int lastPosition = 0;
    private int currentPositon = 0;

    private void initData() {
        carouselBeanList.add(new CarouselBean().setPhoto(
                "https://p4.gexing.com/shaitu/20130118/1217/50f8ccc0def8f.jpg"
        ));
        carouselBeanList.add(new CarouselBean().setPhoto(
                "https://p2.gexing.com/shaitu/20130118/1217/50f8ccca3c419.jpg"
        ));
        carouselBeanList.add(new CarouselBean().setPhoto(
                "https://p1.gexing.com/shaitu/20130118/1217/50f8ccc7a6b3d.jpg"
        ));
        carouselBeanList.add(new CarouselBean().setPhoto(
                "https://p2.gexing.com/shaitu/20130118/1217/50f8cccbce70d.jpg"
        ));
        carouselBeanList.add(new CarouselBean().setPhoto(
                "https://p2.gexing.com/shaitu/20130118/1217/50f8ccd0a9ba1.jpg"
        ));
        carouselBeanList.add(new CarouselBean().setPhoto(
                "https://p3.gexing.com/shaitu/20130118/1217/50f8ccdae8760.jpg"
        ));
    }


    public CarouselView(Context context) {
        super(context);
    }

    public CarouselView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public CarouselView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view = LayoutInflater.from(context).inflate(R.layout.carousel_layout, null);
        viewPager = (ViewPager) view.findViewById(R.id.gallery);
        carouselLayout = (LinearLayout) view.findViewById(R.id.CarouselLayoutPage);
        adapter = new CarouselAdapter(carouselBeanList, context,viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentPositon = position;
            }

            @Override
            public void onPageSelected(int position) {
                lastPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 2) {
                    ((ImageView) carouselLayout.getChildAt(currentPositon)).setImageDrawable(getResources().getDrawable(R.drawable.ic_spot_current));
                    ((ImageView) carouselLayout.getChildAt(lastPosition)).setImageDrawable(getResources().getDrawable(R.drawable.ic_spot));
                }
            }
        });
        viewPager.setOnTouchListener(this);
        viewPager.setAdapter(adapter);
        addView(view);
        viewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                initData();
                setLinearView();
                adapter.setViewData(carouselBeanList);

                adapter.notifyDataSetChanged();
                mHandler.sendEmptyMessage(1);
            }
        });

    }

    private void clearCarouselLayout() {
        for (int i = 0; i < carouselBeanList.size(); i++) {
            ((ImageView) carouselLayout.getChildAt(i))
                    .setImageDrawable(getResources().getDrawable(R.drawable.ic_spot));
        }
    }

    private void setLinearView() {
        carouselLayout.removeAllViews();
        for (int i = 0; i < carouselBeanList.size(); i++) {
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(48, 48);
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(layoutParams);
            imageView.setPadding(2, 2, 2, 2);
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_spot));
            carouselLayout.addView(imageView);
        }
        ((ImageView) carouselLayout.getChildAt(0)).setImageDrawable(getResources().getDrawable(R.drawable.ic_spot_current));
    }

    public void setCarouselBeanList(List<CarouselBean> carouselBeanList) {
        this.carouselBeanList = carouselBeanList;
        adapter.notifyDataSetChanged();
        setLinearView();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mHandler.removeMessages(1);
                mDownX = (int) motionEvent.getX();
            case MotionEvent.ACTION_MOVE:
                int move = (int) motionEvent.getX() - mDownX;
                clearCarouselLayout();
                if (move > 0) {
                    if (Math.abs(move) > 48) {
                        if (count == 0) {
                            ((ImageView) carouselLayout.getChildAt(carouselBeanList.size() - 1))
                                    .setImageDrawable(getResources().getDrawable(R.drawable.ic_spot_current));
                            ((ImageView) carouselLayout.getChildAt(0))
                                    .setImageDrawable(getResources().getDrawable(R.drawable.ic_spot));
                        } else {
                            ((ImageView) carouselLayout.getChildAt(count - 1))
                                    .setImageDrawable(getResources().getDrawable(R.drawable.ic_spot_current));
                            ((ImageView) carouselLayout.getChildAt(count))
                                    .setImageDrawable(getResources().getDrawable(R.drawable.ic_spot));
                        }
                    } else {
                        if (count == 0) {
                            ((ImageView) carouselLayout.getChildAt(0))
                                    .setImageDrawable(getResources().getDrawable(R.drawable.ic_spot_current));
                            ((ImageView) carouselLayout.getChildAt(carouselBeanList.size() - 1))
                                    .setImageDrawable(getResources().getDrawable(R.drawable.ic_spot));
                        } else {
                            ((ImageView) carouselLayout.getChildAt(count))
                                    .setImageDrawable(getResources().getDrawable(R.drawable.ic_spot_current));
                            ((ImageView) carouselLayout.getChildAt(count - 1))
                                    .setImageDrawable(getResources().getDrawable(R.drawable.ic_spot));
                        }
                    }
                } else if (move < 0) {
                    if (Math.abs(move) > 48) {
                        if (count == carouselBeanList.size() - 1) {
                            ((ImageView) carouselLayout.getChildAt(0))
                                    .setImageDrawable(getResources().getDrawable(R.drawable.ic_spot_current));
                            ((ImageView) carouselLayout.getChildAt(count))
                                    .setImageDrawable(getResources().getDrawable(R.drawable.ic_spot));
                        } else {
                            ((ImageView) carouselLayout.getChildAt(count + 1))
                                    .setImageDrawable(getResources().getDrawable(R.drawable.ic_spot_current));
                            ((ImageView) carouselLayout.getChildAt(count))
                                    .setImageDrawable(getResources().getDrawable(R.drawable.ic_spot));
                        }
                    } else {
                        if (count == carouselBeanList.size() - 1) {
                            ((ImageView) carouselLayout.getChildAt(count))
                                    .setImageDrawable(getResources().getDrawable(R.drawable.ic_spot_current));
                            ((ImageView) carouselLayout.getChildAt(0))
                                    .setImageDrawable(getResources().getDrawable(R.drawable.ic_spot));
                        } else {
                            ((ImageView) carouselLayout.getChildAt(count))
                                    .setImageDrawable(getResources().getDrawable(R.drawable.ic_spot_current));
                            ((ImageView) carouselLayout.getChildAt(count + 1))
                                    .setImageDrawable(getResources().getDrawable(R.drawable.ic_spot));
                        }
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                count = viewPager.getCurrentItem();
                mHandler.sendEmptyMessageDelayed(1, 5000);
            default:
                break;
        }
        return false;
    }
}

class CarouselAdapter extends PagerAdapter {

    List<ImageView> imageViewList = new ArrayList<>();
    List<CarouselBean> viewData;

    ViewPager viewGroup;
    Context context;

    private int viewSize;

    public CarouselAdapter(List<CarouselBean> ViewData, Context context,ViewPager viewGroup) {
        this.viewData = ViewData;
        this.context = context;
        this.viewGroup = viewGroup;
    }

    private void initView(Context context) {
        for (CarouselBean bean : viewData) {
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(layoutParams);
            Glide.with(context)
                    .load(bean.getPhoto())
                    .override(viewGroup.getWidth(),viewGroup.getHeight())
                    .into(imageView);
            imageViewList.add(imageView);
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        //根据传来的key，找到view,判断与传来的参数View arg0是不是同一个视图
        return view == imageViewList.get((int) Integer.parseInt(object.toString()));
    }

    @Override
    public int getCount() {
        return imageViewList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imageViewList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(imageViewList.get(position));
        //把当前新增视图的位置（position）作为Key传过去
        return position;
    }

    public void setViewData(List<CarouselBean> viewData) {
        this.viewData = viewData;
        initView(context);
    }

    @Override
    public void notifyDataSetChanged() {
        viewSize = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (viewSize > 0) {
            viewSize--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}
