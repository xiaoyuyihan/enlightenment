package enlightenment.com.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import enlightenment.com.base.R;

/**
 * Created by lw on 2017/7/24.
 */

public class CircularLayout extends ViewGroup {
    private boolean initFalg = false;
    //private int viewRadius = 0;                     //view 半径
    private int childfirstViewRadius = 100;                  //child1半径
    private int childtwoViewRadius = 80;                  //child2半径
    private int initfirstAngle = 0;                    //一级初始角度
    private int inittwoAngle = 0;                 //二级初始角度
    private int firstLevelAngle;                 //一级分隔角度
    private int twoLevelAngle;                  //二级分隔角度
    private int viewCenterX = 0;                  //圆中心坐标
    private int viewCenterY = 0;
    private int referenceX = 0;                   //参照坐标
    private int referenceY = 0;
    private int padding = 24;
    private int fristRingSize=8;
    private BaseAdapter mAdapter;
    private Handler mHander=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            initfirstAngle=(initfirstAngle+1)%360;
            inittwoAngle=(inittwoAngle+1)%360;
            CircularLayout.this.requestLayout();
            mHander.sendEmptyMessageDelayed(0,4000);
        }
    };
    private LayoutInflater mInflater;

    public void setAdapter(BaseAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    public CircularLayout(Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CircularLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CircularLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CircularLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.CircularLayout);
        childfirstViewRadius=(int)array.getDimension(R.styleable.CircularLayout_firstRadius,100);
        childtwoViewRadius=(int)array.getDimension(R.styleable.CircularLayout_twoRadius,80);
        padding=(int)array.getDimension(R.styleable.CircularLayout_circularPadding,16);
        fristRingSize=(int)array.getDimension(R.styleable.CircularLayout_ringSize,8);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mAdapter != null) {
            for (int i =0;i<mAdapter.getCount();i++){
                addView(mAdapter.getView(i,null,this));
            }
        }
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (width > height) {
            setMeasuredDimension(height, height);
        } else
            setMeasuredDimension(width, width);
    }

    @Override
    protected void onLayout(boolean f, int l, int t, int r, int b) {
        //getChildAt(0).layout(10, 10, 70, 70);
        if (getChildCount()>0&&!initFalg) {
            init();
            initFalg = !initFalg;
        }
        for (int i = 0; i < getChildCount(); i++) {
            if (i < fristRingSize) {
                getAnglesToCoordinate((i*firstLevelAngle)+initfirstAngle,getChildAt(i),true);
            } else {
                getAnglesToCoordinate(((i-7)*twoLevelAngle)+inittwoAngle,getChildAt(i),false);
            }
        }
        //initfirstAngle = (initfirstAngle + firstLevelAngle) % 360;
        //inittwoAngle = (inittwoAngle + twoLevelAngle) % 360;
        //mHander.sendEmptyMessageDelayed(1,1000);
    }

    private void init() {
        viewCenterX = getMeasuredWidth() / 2;
        viewCenterY = getMeasuredHeight() / 2;
        referenceX = getWidth() - padding - childfirstViewRadius;
        referenceY = viewCenterY;
        int cont = getChildCount();
        if (cont < fristRingSize+1) {
            firstLevelAngle = 360 / cont;
            twoLevelAngle = 0;
        } else {
            firstLevelAngle = 360 / fristRingSize;
            twoLevelAngle = 360 / (cont - fristRingSize);
        }

    }

    private void getAnglesToCoordinate(int angles, View childView,boolean Type) {
        if (Type){
            int viewX = (int)((referenceX-viewCenterX)*Math.cos(Math.toRadians(angles))+viewCenterX);
            int viewY = (int)((referenceX-viewCenterY)*Math.sin(Math.toRadians(angles))+viewCenterY);
            childView.layout(viewX - childfirstViewRadius, viewY - childfirstViewRadius,
                    viewX + childfirstViewRadius, viewY + childfirstViewRadius);
        }else {
            int viewX = (int)((referenceX-childfirstViewRadius*2-padding-viewCenterX)*Math.cos(Math.toRadians(angles))+viewCenterX);
            int viewY = (int)((referenceX-childfirstViewRadius*2-padding-viewCenterX)*Math.sin(Math.toRadians(angles))+viewCenterY);
            childView.layout(viewX - childfirstViewRadius, viewY - childfirstViewRadius,
                    viewX + childfirstViewRadius, viewY + childfirstViewRadius);
        }
    }

    public void setChildfirstViewRadius(int childfirstViewRadius) {
        this.childfirstViewRadius = childfirstViewRadius;
    }

    public void setChildtwoViewRadius(int childtwoViewRadius) {
        this.childtwoViewRadius = childtwoViewRadius;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public void setFristRingSize(int fristRingSize) {
        this.fristRingSize = fristRingSize;
    }
}
