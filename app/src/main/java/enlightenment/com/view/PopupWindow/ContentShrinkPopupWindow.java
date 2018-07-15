package enlightenment.com.view.PopupWindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;

import enlightenment.com.base.R;
import enlightenment.com.details.ContentDetailsSysFragment;
import enlightenment.com.tool.device.DisplayUtils;

/**
 * Created by admin on 2018/3/22.
 */

public class ContentShrinkPopupWindow extends PopupWindow {
    private ImageView mImageView;

    public ContentShrinkPopupWindow(Context context,String url,int flag) {
        super(context);

        if(flag == 1){
            mImageView = (ImageView) LayoutInflater.from(context)
                    .inflate(R.layout.view_item_edit_photo, null);
            Glide.with(context).load(url).into(mImageView);

            setContentView(mImageView);
        }else {

        }
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(DisplayUtils.dp2px(context,240));
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(false);
        this.setOutsideTouchable(false);
        //设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.grey_green));
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }
}
