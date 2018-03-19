package enlightenment.com.details;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enlightenment.com.base.R;
import enlightenment.com.main.ContentBean;
import enlightenment.com.tool.device.CheckUtils;
import enlightenment.com.tool.glide.GlideCircleTransform;
import enlightenment.com.tool.recycelr.SpacesItemDecoration;
import enlightenment.com.view.PopupWindow.CommonPopupWindow;

/**
 * Created by lw on 2018/3/16.
 */

public class ContentDetailsSysFragment extends Fragment {

    @BindView(R.id.view_content_details_coordinator)
    CoordinatorLayout mCoordinator;

    @BindView(R.id.content_details_top_name)
    TextView mContentName;
    @BindView(R.id.content_details_recycler)
    RecyclerView mContentRecycler;
    @BindView(R.id.view_content_details_sys_text)
    TextView mSysContent;

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

    RecyclerView mShrinkRecycler;

    private StaggeredGridLayoutManager mGridManager;
    private LinearLayoutManager mShrinkGridManager;
    private ContentResAdapter mContentResAdapter;
    private ContentResAdapter mContentShrinkResAdapter;

    private ContentBean mContent;
    private String[] mPhotos = new String[0];
    private String[] mAudios = new String[0];

    private View mContentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.view_content_details_sys, container, false);
        ButterKnife.bind(this, mContentView);
        mContent = getArguments().getParcelable(ContentDetailsActivity.CONTENT_EXTRA_DATA);
        mGridManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mShrinkGridManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        init();
        mShrinkRecycler = (RecyclerView) LayoutInflater.from(getActivity()).inflate(R.layout.fragment_recycler_only, null);
        return mContentView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void showPopupWindow() {
        mShrinkRecycler.setLayoutManager(mShrinkGridManager);
        ContentResAdapter contentResAdapter=new ContentResAdapter();
        contentResAdapter.setFlag(true);
        mShrinkRecycler.setAdapter(contentResAdapter);
        CommonPopupWindow popupWindow = new CommonPopupWindow.Builder(getActivity())
                //设置PopupWindow布局
                .setView(mShrinkRecycler)
                //设置宽高
                .setSize(ViewGroup.LayoutParams.MATCH_PARENT, 72)
                //开始构建
                .create();
        //弹出PopupWindow
        popupWindow.showAsDropDown(mCoordinator);
    }

    private void init() {
        if (mContent.getPhoto() != null)
            mPhotos = mContent.getPhoto().split(",");
        if (mContent.getAudio() != null)
            mAudios = mContent.getAudio().split(",");
        if (mPhotos == null)
            mPhotos = new String[0];
        if (mAudios == null)
            mAudios = new String[0];
        mContentName.setText(mContent.getName());
        mSysContent.setText(mContent.getContent());
        mContentResAdapter = new ContentResAdapter();
        mContentRecycler.setLayoutManager(mGridManager);
        mContentRecycler.addItemDecoration(new SpacesItemDecoration(16));
        mContentRecycler.setAdapter(mContentResAdapter);
        mContentRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mGridManager.invalidateSpanAssignments();
                //这行主要解决了当加载更多数据时，底部需要重绘，否则布局可能衔接不上。
            }
        });

        Glide.with(this).load(mContent.getAvatar())
                .transform(new GlideCircleTransform(getActivity()))
                .into(mContentArrow);
        mContentUsername.setText(mContent.getUsername());
        mContentModel.setText(CheckUtils.getModelName(mContent));
        mContentColumn.setText(mContent.getColumnName());
    }

    class ContentResAdapter extends RecyclerView.Adapter {

        private boolean isFlag = false;

        public void setFlag(boolean flag) {
            isFlag = flag;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ResViewHolder mResView = new ResViewHolder(
                    LayoutInflater.from(getActivity())
                            .inflate(R.layout.item_view_details_res, parent, false));
            return mResView;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ResViewHolder resView = (ResViewHolder) holder;
            if (position < mPhotos.length) {
                if (!isFlag)
                    resView.setImageUrl(mPhotos[position]);
                else
                    resView.setImage(mPhotos[position]);
            } else {
                resView.setResUrl(mAudios[position]);
            }
        }

        @Override
        public int getItemCount() {
            return mPhotos.length + mAudios.length;
        }
    }

    class ResViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_view_details_image)
        ImageView mDetailsImage;
        @BindView(R.id.item_view_details_play)
        ImageView mDetailsPlay;

        private String mResUrl;

        public ResViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setImage(String url) {
            Glide.with(ContentDetailsSysFragment.this).load(url).into(mDetailsImage);
        }

        public void setImageUrl(String url) {
            Glide.with(ContentDetailsSysFragment.this).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {

                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    int size = resource.getByteCount();
                    int imageWidth = resource.getWidth();
                    int imageHeight = resource.getHeight();
                    int height = mDetailsImage.getWidth() * imageHeight / imageWidth;
                    ViewGroup.LayoutParams para = mDetailsImage.getLayoutParams();
                    para.height = height;
                    para.width = mDetailsImage.getWidth();
                    mDetailsImage.setImageBitmap(resource);
                }
            });
        }

        public void setDetailsImage(Bitmap bitmap) {
            mDetailsImage.setImageBitmap(bitmap);
        }

        public void setResUrl(String mResUrl) {
            mDetailsPlay.setVisibility(View.VISIBLE);
            this.mResUrl = mResUrl;
        }
    }

    //文章作者
    @OnClick({R.id.view_content_details_arrow, R.id.view_content_details_bottom_name})
    public void onUsername(View v) {

    }

    //栏目
    @OnClick(R.id.view_content_details_bottom_column)
    public void onColumn(View v) {

    }

    //模块
    @OnClick(R.id.view_content_details_bottom_model)
    public void onModel(View v) {

    }

    //关注
    @OnClick(R.id.view_content_details_bottom_follow)
    public void onFollow(View v) {

    }

    //打赏
    @OnClick(R.id.view_content_details_bottom_reward)
    public void onReward(View v) {

    }
}
