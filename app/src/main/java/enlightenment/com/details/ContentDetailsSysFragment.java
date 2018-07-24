package enlightenment.com.details;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enlightenment.com.base.R;
import enlightenment.com.operationBean.ContentBean;
import enlightenment.com.operationBean.CommentBean;
import enlightenment.com.tool.device.CheckUtils;
import enlightenment.com.tool.device.DisplayUtils;
import enlightenment.com.tool.glide.GlideCircleTransform;
import enlightenment.com.tool.recycelr.SpacesItemDecoration;
import enlightenment.com.view.PopupWindow.ContentShrinkPopupWindow;

/**
 * Created by lw on 2018/3/16.
 */

public class ContentDetailsSysFragment extends ContentDetailsFragment {
    private String Tag = "ContentDetailsSysFragment";

    @BindView(R.id.view_content_details_scroll)
    NestedScrollView mCoordinator;

    @BindView(R.id.content_details_top_name)
    TextView mContentName;
    @BindView(R.id.content_details_recycler_grid)
    RecyclerView mContentRecycler;
    @BindView(R.id.content_details_recycler_horizontal)
    RecyclerView mContentHorRecycler;
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
    RecyclerView mCommentRecycler;

    private GridLayoutManager mGridManager;
    private ContentResAdapter mContentResAdapter;
    private ContentResAdapter mContentHorAdapter;
    private CommentAdapter mCommentAdapter;

    private ContentBean mContent;
    private String[] mPhotos = new String[0];
    private String[] mAudios = new String[0];

    private View mContentView;

    private ContentShrinkPopupWindow popupWindow = null;

    private boolean mResViewFlag = true;
    private boolean mResFlag = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.view_content_details_sys, container, false);
        ButterKnife.bind(this, mContentView);
        init();
        initHor();
        return mContentView;
    }

    private void initHor() {
        mContentHorAdapter = new ContentResAdapter(getActivity(), mPhotos, mAudios);
        mContentHorAdapter.setFlag(true);
        mContentHorRecycler.setLayoutManager(
                new LinearLayoutManager(getActivity(),
                        LinearLayoutManager.HORIZONTAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                getActivity(), DividerItemDecoration.HORIZONTAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider_item_transparent));
        mContentHorRecycler.addItemDecoration(dividerItemDecoration);
        mContentHorRecycler.setAdapter(mContentHorAdapter);
        mContentHorAdapter.setOnContentResItemClickListener(new OnContentResItemClickListener() {
            @Override
            public void onClick(String url, int flag) {
                showPopupWindow(url, flag);
            }
        });
    }

    @Override
    protected void clearData() {

    }

    @Override
    protected void initData() {
        mContent = getArguments().getParcelable(ContentDetailsActivity.CONTENT_EXTRA_DATA);
        mGridManager = new GridLayoutManager(getContext(), 3);
        //mGridManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
    }

    private void showPopupWindow(String url, int flag) {
        popupWindow = new ContentShrinkPopupWindow(getActivity(), url, flag);
        int[] location = new int[2];
        mCoordinator.getLocationOnScreen(location);
        //弹出PopupWindow
        popupWindow.showAtLocation(mCoordinator,
                Gravity.TOP | Gravity.CENTER_HORIZONTAL, location[0], location[1]);
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
        if (mAudios.length == 0 && mPhotos.length == 0) {
            mResFlag = false;
        }
        mContentName.setText(mContent.getName());
        mSysContent.setText(mContent.getContent());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mCoordinator.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    boolean flag = isViewVisible(mContentRecycler);
                    if (mResFlag) {
                        if (scrollY > oldScrollY) {
                            // down
                            if (!flag && mResViewFlag) {
                                mResViewFlag = !mResViewFlag;
                                mContentRecycler.setVisibility(View.GONE);
                                mContentHorRecycler.setVisibility(View.VISIBLE);
                            }
                        }
                        if (scrollY < oldScrollY) {
                            //up
                            if (flag && !mResViewFlag) {
                                mResViewFlag = !mResViewFlag;
                                mContentRecycler.setVisibility(View.VISIBLE);
                                mContentHorRecycler.setVisibility(View.GONE);
                            }
                        }

                        if (scrollY == 0) {
                            mContentRecycler.setBackground(null);
                        } else {
                            mContentRecycler.setBackground(
                                    getResources().getDrawable(
                                            R.drawable.background_grey_green_corners_4));
                        }

                        if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {

                        }
                    }
                }

            });
        }

        mContentResAdapter = new ContentResAdapter(getActivity(), mPhotos, mAudios);
        mContentRecycler.setLayoutManager(mGridManager);
        mContentResAdapter.setOnContentResItemClickListener(new OnContentResItemClickListener() {
            @Override
            public void onClick(String url, int flag) {
                showPopupWindow(url, flag);
            }
        });
        mContentRecycler.addItemDecoration(new SpacesItemDecoration(16));
        mContentRecycler.setAdapter(mContentResAdapter);

        //初始化 文本信息
        Glide.with(this).load(mContent.getAvatar())
                .transform(new GlideCircleTransform(getActivity()))
                .into(mContentArrow);
        mContentUsername.setText(mContent.getUsername());
        mContentModel.setText(CheckUtils.getModelName(mContent));
        mContentColumn.setText(mContent.getColumnName());

        //更新评论
        updateReward(mContentFollow);
    }

    @Override
    void updateComments(List<CommentBean> commentBeans) {
        if (mCommentAdapter == null) {
            mCommentAdapter = new CommentAdapter(commentBeans, getActivity());
            mCommentRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            mCommentRecycler.setAdapter(mCommentAdapter);
        } else {
            mCommentAdapter.setCommentBeans(commentBeans);
            mCommentAdapter.notifyDataSetChanged();
        }
    }


    //文章作者
    @OnClick({R.id.view_content_details_arrow, R.id.view_content_details_bottom_name})
    public void onUsername(View v) {
        if (getActivity() instanceof onContentDetailsListener)
            ((onContentDetailsListener) getActivity()).onUsername();
    }

    //栏目
    @OnClick(R.id.view_content_details_bottom_column)
    public void onColumn(View v) {
        if (getActivity() instanceof onContentDetailsListener)
            ((onContentDetailsListener) getActivity()).onColumn();
    }

    //模块
    @OnClick(R.id.view_content_details_bottom_model)
    public void onModel(View v) {
        if (getActivity() instanceof onContentDetailsListener)
            ((onContentDetailsListener) getActivity()).onModel();
    }

    //关注
    @OnClick(R.id.view_content_details_bottom_follow)
    public void onFollow(View v) {
        if (getActivity() instanceof onContentDetailsListener)
            ((onContentDetailsListener) getActivity()).onFollow();
    }

    //打赏
    @OnClick(R.id.view_content_details_bottom_reward)
    public void onReward(View v) {
        if (getActivity() instanceof onContentDetailsListener)
            ((onContentDetailsListener) getActivity()).onReward();
        updateReward(mContentFollow);
    }

    @SuppressLint("ResourceAsColor")
    private void updateReward(TextView textView) {
        if (Boolean.valueOf(mContent.getIsAtten())) {
            textView.setBackground(getActivity().getDrawable(R.drawable.background_un_interest_module));
            textView.setText("取关");
            textView.setTextColor(R.color.mainTopColor);
        } else {
            textView.setBackground(getActivity().getDrawable(R.drawable.background_interest_module));
            textView.setText("关注");
            textView.setTextColor(R.color.white);
        }
    }

    public boolean isViewVisible(View view) {
        boolean cover;
        Rect rect = new Rect();
        cover = view.getGlobalVisibleRect(rect);
        if (cover) {
            if (rect.width() == 0 || rect.height() == 0) {
                return !cover;
            }
        }
        return cover;
    }

    public static class ContentResAdapter extends RecyclerView.Adapter {

        private boolean isFlag = false;
        private Context context;
        private String[] mPhotos;
        private String[] mAudios;
        private OnContentResItemClickListener onContentResItemClickListener;

        public ContentResAdapter(Context context, String[] Photos, String[] Audios) {
            this.context = context;
            this.mAudios = Audios;
            this.mPhotos = Photos;
        }

        public void setFlag(boolean flag) {
            isFlag = flag;
        }

        public void setOnContentResItemClickListener(OnContentResItemClickListener onContentResItemClickListener) {
            this.onContentResItemClickListener = onContentResItemClickListener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ResViewHolder mResView = new ResViewHolder(
                    LayoutInflater.from(context)
                            .inflate(R.layout.item_view_details_res, parent, false), context);
            return mResView;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ResViewHolder resView = (ResViewHolder) holder;
            if (position < mPhotos.length) {
                if (!isFlag)
                    resView.setImageUrl(mPhotos[position]);
                else
                    resView.setImage(mPhotos[position]);
            } else {
                resView.setResUrl(mAudios[position]);
            }

            resView.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onContentResItemClickListener != null) {
                        if (position < mPhotos.length) {
                            onContentResItemClickListener.onClick(mPhotos[position], 1);
                        } else {
                            onContentResItemClickListener.onClick(mAudios[position], 2);
                        }
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mPhotos.length + mAudios.length;
        }


    }

    public interface OnContentResItemClickListener {
        void onClick(String url, int flag);
    }

    static class ResViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_view_details_image)
        ImageView mDetailsImage;
        @BindView(R.id.item_view_details_play)
        ImageView mDetailsPlay;

        private Context context;

        private String mResUrl;

        public ResViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            ButterKnife.bind(this, itemView);
        }

        public void setImage(String url) {
            setLayoutParams();
            Glide.with(context).load(url).placeholder(R.drawable.ic_banner_default).into(mDetailsImage);
        }

        private void setLayoutParams() {
            mDetailsImage.setScaleType(ImageView.ScaleType.FIT_XY);
            ViewGroup.LayoutParams layoutParams = mDetailsImage.getLayoutParams();
            layoutParams.width = DisplayUtils.dp2px(context, 96);
            mDetailsImage.setLayoutParams(layoutParams);

        }

        public void setImageUrl(String url) {
            Glide.with(context).load(url).asBitmap().placeholder(R.drawable.ic_banner_default).into(mDetailsImage);
        }

        public void setDetailsImage(Bitmap bitmap) {
            mDetailsImage.setImageBitmap(bitmap);
        }

        @SuppressLint("WrongConstant")
        public void setResUrl(String mResUrl) {
            mDetailsPlay.setVisibility(View.VISIBLE);
            this.mResUrl = mResUrl;
        }
    }
}
