package enlightenment.com.view.PopupWindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import enlightenment.com.base.R;

/**
 * Created by admin on 2018/3/29.
 */

public class CommentPopupWindow extends PopupWindow {
    private View mView;
    private EditText mComment;
    private TextView mSubject;
    private OnCommentClickListener onCommentClickListener;

    public void setOnCommentClickListener(OnCommentClickListener onCommentClickListener) {
        this.onCommentClickListener = onCommentClickListener;
    }

    public CommentPopupWindow(Context context){
        super(context);
        mView= LayoutInflater.from(context).inflate(R.layout.window_user_comment,null);
        mComment=mView.findViewById(R.id.comment_edit);
        mSubject=mView.findViewById(R.id.comment_subject);
        mSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onCommentClickListener!=null)
                    onCommentClickListener.onSubject(mComment.getText().toString());
            }
        });

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
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }
    public interface OnCommentClickListener{
        void onSubject(String comment);
    }
}
