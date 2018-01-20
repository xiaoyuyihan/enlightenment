package enlightenment.com.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import enlightenment.com.base.R;
import enlightenment.com.main.CarouselBean;

/**
 * Created by lw on 2017/9/24.
 */

public class CarouselView extends FrameLayout {

    private Context context;

    private ViewPager viewPager;
    private LinearLayout carouselLayout;
    private PagerAdapter adapter;

    private boolean carouselFalg=false;
    private List<CarouselBean> carouselBeanList =new ArrayList<>();
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
           if (!carouselFalg){
               int count=viewPager.getCurrentItem();
               ((ImageView)carouselLayout.getChildAt(count)).setImageDrawable(getResources().getDrawable(R.drawable.ic_spot));
               Log.d("Carousel","Carousel count : "+count);
               if (count>=carouselBeanList.size()-1){
                   count=0;
                   viewPager.setCurrentItem(count,false);

               }else{
                   count=count+1;
                   viewPager.setCurrentItem(count);
               }
               ((ImageView)carouselLayout.getChildAt(count)).setImageDrawable(getResources().getDrawable(R.drawable.ic_spot_current));
           }
            mHandler.sendEmptyMessageDelayed(1,3000);
        }
    };

    private int lastPosition=0;
    private int currentPositon=0;

    private void initData(){
        carouselBeanList.add(new CarouselBean().setPhoto("http://img.tupianzj.com/uploads/allimg/160624/9-160624110432.jpg"));
        carouselBeanList.add(new CarouselBean().setPhoto("http://img.tupianzj.com/uploads/allimg/160624/9-160624110548.jpg"));
        carouselBeanList.add(new CarouselBean().setPhoto("http://img.tupianzj.com/uploads/allimg/160624/9-160624110549.jpg"));
        carouselBeanList.add(new CarouselBean().setPhoto("http://img.tupianzj.com/uploads/allimg/160624/9-160624110600-50.jpg"));
        carouselBeanList.add(new CarouselBean().setPhoto("http://img.tupianzj.com/uploads/allimg/160626/9-1606261G631.jpg"));
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
        initData();
        View view = LayoutInflater.from(context).inflate(R.layout.carousel_layout,null);
        viewPager = (ViewPager) view.findViewById(R.id.gallery);
        carouselLayout = (LinearLayout)view.findViewById(R.id.CarouselLayoutPage);
        adapter=new CarouselAdapter(carouselBeanList,context);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentPositon=position;
            }

            @Override
            public void onPageSelected(int position) {
                lastPosition=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == 2){
                    ((ImageView)carouselLayout.getChildAt(currentPositon)).setImageDrawable(getResources().getDrawable(R.drawable.ic_spot_current));
                    ((ImageView)carouselLayout.getChildAt(lastPosition)).setImageDrawable(getResources().getDrawable(R.drawable.ic_spot));
                }
            }
        });
        viewPager.setAdapter(adapter);
        viewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        carouselFalg = true;
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        carouselFalg = false;
                    default:
                        break;
                }
                return false;
            }
        });
        addView(view);
        setLinearView();
        mHandler.sendEmptyMessage(1);
    }

    private void setLinearView() {
        carouselLayout.removeAllViews();
        for (int i=0;i<carouselBeanList.size();i++){
            ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(48,48);
            ImageView imageView=new ImageView(context);
            imageView.setLayoutParams(layoutParams);
            imageView.setPadding(2,2,2,2);
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_spot));
            carouselLayout.addView(imageView);
        }
        ((ImageView)carouselLayout.getChildAt(0)).setImageDrawable(getResources().getDrawable(R.drawable.ic_spot_current));
    }

    public void setCarouselBeanList(List<CarouselBean> carouselBeanList) {
        this.carouselBeanList = carouselBeanList;
        adapter.notifyDataSetChanged();
        setLinearView();
    }

    class CarouselAdapter extends PagerAdapter{

       List<ImageView> imageViewList=new ArrayList<>();
       List<CarouselBean> viewData;

       public CarouselAdapter(List<CarouselBean> ViewData,Context context){
           this.viewData=ViewData;
           initView(context);
       }

       private void initView(Context context){
           for (CarouselBean bean :viewData){
               ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
               ImageView imageView=new ImageView(context);
               imageView.setLayoutParams(layoutParams);
               Glide.with(context).load(bean.getPhoto()).into(imageView);
               imageViewList.add(imageView);
           }
       }

       @Override
       public boolean isViewFromObject(View view, Object object) {
           //根据传来的key，找到view,判断与传来的参数View arg0是不是同一个视图
           return view == imageViewList.get((int)Integer.parseInt(object.toString()));
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

   }
}
