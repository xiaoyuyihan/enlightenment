package enlightenment.com.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import enlightenment.com.base.R;

/**
 * Created by lw on 2018/3/16.
 */

public class ContentDetailsWebFragment extends Fragment {

    //web
    @BindView(R.id.view_content_details_progress)
    ProgressBar mWebProgress;
    @BindView(R.id.view_content_details_web)
    WebView mWebView;

    //bottom
    @BindView(R.id.view_content_details_bottom_arrow)
    ImageView mContentArrow;
    @BindView(R.id.view_content_details_bottom_name)
    TextView mContentUsername;
    @BindView(R.id.view_content_details_bottom_column)
    TextView mContentColumn;
    @BindView(R.id.view_content_details_bottom_model)
    TextView mContentModel;
    @BindView(R.id.view_content_details_bottom_follow)
    TextView mContentFollow;
    @BindView(R.id.view_content_details_bottom_reward)
    TextView mContentReward;
    @BindView(R.id.view_content_details_bottom_recycler)
    RecyclerView mRecycler;

    private View mContentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView=inflater.inflate(R.layout.view_content_details_web,container,false);
        ButterKnife.bind(mContentView);
        return mContentView;
    }
}
