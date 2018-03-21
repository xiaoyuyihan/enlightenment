package enlightenment.com.details;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.prefill.PreFillType;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enlightenment.com.base.AppActivity;
import enlightenment.com.base.R;
import enlightenment.com.main.ContentBean;
import enlightenment.com.view.HeadPortraitWinPopupWindow;
import enlightenment.com.view.PopupWindow.CommonPopupWindow;

/**
 * Created by lw on 2018/3/14.
 */

public class ContentDetailsActivity extends AppActivity {

    public static String CONTENT_EXTRA_DATA="CONTENT_EXTRA_DATA";

    @BindView(R.id.view_content_top_text)
    TextView mTopContentName;
    @BindView(R.id.view_content_top_comment)
    TextView mTopComment;
    @BindView(R.id.view_content_top_live)
    TextView mTopLive;

    private ContentBean mContentBean;

    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_details);
        ButterKnife.bind(this);
        mContentBean=getIntent().getParcelableExtra(CONTENT_EXTRA_DATA);
        mTopContentName.setText(mContentBean.getColumnName());
        mTopComment.setText(mContentBean.getNumber());
        mTopLive.setText(mContentBean.getLive());
        init();
    }

    private void init() {
        mFragment=new ContentDetailsSysFragment();
        Bundle b=new Bundle();
        b.putParcelable(CONTENT_EXTRA_DATA,mContentBean);
        mFragment.setArguments(b);
        FragmentTransaction f=getSupportFragmentManager().beginTransaction();
        f.add(R.id.content_details_fragment,mFragment).commit();
    }


    //返回
    @OnClick(R.id.view_content_top_back)
    public void onBack(View view) {
        finish();
    }

    //评论
    @OnClick(R.id.view_content_top_comment)
    public void onComment(View view) {

    }

    //喜欢
    @OnClick(R.id.view_content_top_live)
    public void onLive(View v) {

    }

    //分享
    @OnClick(R.id.view_content_top_share)
    public void onShare(View v) {

    }

    //更多
    @OnClick(R.id.view_content_top_more)
    public void onMore(View v) {

    }



}
