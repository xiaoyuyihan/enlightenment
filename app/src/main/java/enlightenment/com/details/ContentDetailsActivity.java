package enlightenment.com.details;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.edit.EditActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enlightenment.com.base.AppActivity;
import enlightenment.com.base.R;
import enlightenment.com.operationBean.ContentBean;
import enlightenment.com.mvp.BaseView;
import enlightenment.com.user.ContentUserActivity;
import enlightenment.com.view.PopupWindow.CommentPopupWindow;

/**
 * Created by lw on 2018/3/14.
 */

public class ContentDetailsActivity extends AppActivity implements onContentDetailsListener, DetailsView {

    public static String CONTENT_EXTRA_DATA = "CONTENT_EXTRA_DATA";

    @BindView(R.id.view_content_top_text)
    TextView mTopContentName;
    @BindView(R.id.view_content_top_comment)
    TextView mTopComment;
    @BindView(R.id.view_content_top_live)
    TextView mTopLive;

    private ContentBean mContentBean;

    private ContentDetailsFragment mFragment;
    private CommentPopupWindow mPopupWindow;

    private ContentDetailsPresenter contentDetailsPresenter;

    @Override
    protected void onStart() {
        super.onStart();
        contentDetailsPresenter = ContentDetailsPresenter.getInstance();
        contentDetailsPresenter.BindView(this);
        contentDetailsPresenter.onStart();
        contentDetailsPresenter.onUpdateComment();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_content_details;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        mContentBean = getIntent().getParcelableExtra(CONTENT_EXTRA_DATA);
        mTopContentName.setText(mContentBean.getColumnName());
        mTopComment.setText(mContentBean.getNumber());
        if (mContentBean.getViewType()== EditActivity.ACTIVITY_EDIT_TYPE_AUTOMATIC){
            mFragment = new ContentDetailsSysFragment();
        }else {
            mFragment = new ContentDetailsWebFragment();
        }
        Bundle b = new Bundle();
        b.putParcelable(CONTENT_EXTRA_DATA, mContentBean);
        mFragment.setArguments(b);
        FragmentTransaction f = getSupportFragmentManager().beginTransaction();
        f.add(R.id.content_details_fragment, mFragment).commit();
    }

    @Override
    protected void initData() {
        updateDrawable();
    }


    //返回
    @OnClick(R.id.view_content_top_back)
    public void onBack(View view) {
        finish();
    }

    //评论
    @OnClick({R.id.view_content_top_comment, R.id.view_content_details_comment})
    public void onComment(View view) {
        if (mPopupWindow==null){
            mPopupWindow = new CommentPopupWindow(this);
            mPopupWindow.setOnCommentClickListener(new CommentPopupWindow.OnCommentClickListener() {
                @Override
                public void onSubject(String comment) {
                    contentDetailsPresenter.onComment(comment);
                    mTopComment.setText(String.valueOf(
                            Integer.valueOf(mContentBean.getNumber())+1));
                    mPopupWindow.dismiss();
                    mPopupWindow=null;
                }
            });
        }
        //设置layout在PopupWindow中显示的位置
        mPopupWindow.showAtLocation(findViewById(R.id.content_details_view),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    //喜欢
    @OnClick(R.id.view_content_top_live)
    public void onLive(View v) {
        boolean isLive = Boolean.valueOf(mContentBean.getIsLive());
        if (isLive) {
            contentDetailsPresenter.onUnLive();
            mContentBean.setNumber(String.valueOf(
                    Integer.valueOf(mContentBean.getNumber())-1));
        } else {
            contentDetailsPresenter.onLive();
            mContentBean.setNumber(String.valueOf(
                    Integer.valueOf(mContentBean.getNumber())-1));
        }
        mContentBean.setIsLive(String.valueOf(!isLive));
        updateDrawable();
    }

    private void updateDrawable() {
        Drawable drawable;
        if (Boolean.valueOf(mContentBean.getIsLive())) {
            drawable = getDrawable(R.drawable.ic_live_yes);
        } else {
            drawable = getDrawable(R.drawable.ic_live_no);
        }
        mTopLive.setText(mContentBean.getLive());
        drawable.setBounds(0,0,38,38);
        mTopLive.setCompoundDrawables(drawable, null, null, null);
    }

    //分享
    @OnClick(R.id.view_content_top_share)
    public void onShare(View v) {

    }

    //更多
    @OnClick(R.id.view_content_top_more)
    public void onMore(View v) {

    }


    @Override
    public void onUsername() {
        Intent intent = new Intent(this, ContentUserActivity.class);
        intent.putExtra(ContentUserActivity.CONTENT_USER_INFO,mContentBean.getUsername());
        startActivity(intent);
    }

    @Override
    public void onColumn() {
        //Intent intent = new Intent(this,null);
    }

    @Override
    public void onModel() {

    }

    @Override
    public void onFollow() {
        boolean isAtten = Boolean.valueOf(mContentBean.getIsAtten());
        if (isAtten) {
            contentDetailsPresenter.onUnFollow();
        } else {
            contentDetailsPresenter.onFollow();
        }
        mContentBean.setIsAtten(String.valueOf(!isAtten));
    }

    @Override
    public void onReward() {

    }

    @Override
    public void onLoadAllComment() {

    }

    @Override
    public void showToast(String message) {
        showCustomToast(message);
    }

    @Override
    public Object getObj() {
        return mContentBean;
    }

    @Override
    public void updateComment() {
        mFragment.updateComments(contentDetailsPresenter.getCommentBeans());
    }
}
