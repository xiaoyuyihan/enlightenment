package enlightenment.com.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import enlightenment.com.base.R;

/**
 * Created by lw on 2017/9/13.
 */

public class HeadPortraitWinPopupWindow extends PopupWindow {
    private View mView;
    private TextView mShooting;
    private TextView mSelect;
    private TextView mBack;

    public HeadPortraitWinPopupWindow(View.OnClickListener onClickListener, Context context){
        super(context);
        mView= LayoutInflater.from(context).inflate(R.layout.window_head_portrait,null);
        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int height = mView.getTop();
                int y=(int) motionEvent.getY();
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });
        mShooting=(TextView)mView.findViewById(R.id.window_head_portrait_shooting);
        mShooting.setOnClickListener(onClickListener);
        mSelect=(TextView)mView.findViewById(R.id.window_head_portrait_select);
        mSelect.setOnClickListener(onClickListener);
        mBack=(TextView)mView.findViewById(R.id.window_head_portrait_back);
        mBack.setOnClickListener(onClickListener);
        //设置SelectPicPopupWindow的View
        this.setContentView(mView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }
}
