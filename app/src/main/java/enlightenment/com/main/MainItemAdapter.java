package enlightenment.com.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.edit.EditActivity;
import com.edit.bean.EditBean;
import com.edit.bean.WebContentBean;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

import enlightenment.com.base.R;
import enlightenment.com.operationBean.ContentBean;
import enlightenment.com.tool.Image.MediaPlayerUtil;
import enlightenment.com.tool.device.CheckUtils;
import enlightenment.com.tool.device.DisplayUtils;
import enlightenment.com.tool.gson.GsonUtils;
import enlightenment.com.view.NineGridLayout.ItemNineGridLayout;

/**
 * Created by lw on 2017/7/28.
 */

public class MainItemAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    public static final int ITEM_TYPE_BOTTOM = 2;

    public static final int ITEM_BOTTOM_TYPE_FLAG_REFEWSH = 1001;
    public static final int ITEM_BOTTOM_TYPE_FLAG_ERROR = 1002;
    public static final int ITEM_BOTTOM_TYPE_FLAG_END = 1003;


    private int mHeaderCount = 1;//头部View个数
    private int mBottomCount = 1;//底部View个数

    private String mHeaderMsg = "我们正在搜寻····";
    private int mCurFlag = ITEM_BOTTOM_TYPE_FLAG_REFEWSH;

    private Context context;
    private int viewID;
    private ArrayList mData = new ArrayList();

    private ItemNineGridLayout.OnClickImageListener onClickImageListener;
    private OnContentItemListener onContentItemListener;

    public void setOnItemListener(OnContentItemListener onContentItemListener) {
        this.onContentItemListener = onContentItemListener;
    }

    public void setOnClickImageListener(ItemNineGridLayout.OnClickImageListener onClickImageListener) {
        this.onClickImageListener = onClickImageListener;
    }

    public void setHeaderCount(String msg) {
        if (msg != null) {
            this.mHeaderMsg = msg;
            this.mHeaderCount = 1;
        } else
            this.mHeaderCount = 0;
    }

    public void updataBottomViewType(int flag) {
        this.mCurFlag = flag;
    }

    public MainItemAdapter(Context context, ArrayList mData, int viewID) {
        this.context = context;
        this.viewID = viewID;
        this.mData = mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder mViewHolder = null;
        View view;
        switch (viewType) {
            case ITEM_TYPE_BOTTOM:
                view = LayoutInflater.from(context).inflate(R.layout.item_footer_view, parent, false);
                mViewHolder = new ItemViewHolder.BottomViewHolder(view);
                break;
            case ITEM_TYPE_CONTENT:
                view = LayoutInflater.from(context).inflate(viewID, parent, false);
                mViewHolder = new ItemViewHolder.ItemHomeViewHolder(view);
                break;
            case ITEM_TYPE_HEADER:
                mViewHolder = new ItemViewHolder.TextViewHolder(
                        ItemViewHolder.createTextView(context,
                                DisplayUtils.dp2px(context, 48),
                                ViewGroup.LayoutParams.MATCH_PARENT));
        }
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case ITEM_TYPE_CONTENT:
                final ItemViewHolder.ItemHomeViewHolder mTextViewHolder = (ItemViewHolder.ItemHomeViewHolder) holder;
                mTextViewHolder.setOnContentItemListener(onContentItemListener);
                final Object bean = mData.get(position);
                if (bean instanceof ContentBean) {
                    ContentBean contentBean = (ContentBean) bean;
                    Glide.with(context).load(contentBean.getAvatar()).asBitmap().centerCrop()
                            .placeholder(R.mipmap.logo)
                            .into(new BitmapImageViewTarget(mTextViewHolder.getAvatarView()) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    mTextViewHolder.getAvatarView().setImageDrawable(circularBitmapDrawable);
                                }
                            });
                    mTextViewHolder.setUsernameView(contentBean.getUsername());
                    mTextViewHolder.setModelNameView(CheckUtils.getModelName(contentBean));
                    mTextViewHolder.setContentNameView(contentBean.getName());
                    //web
                    if (GsonUtils.isJSONVaild(contentBean.getContent())) {
                        List<WebContentBean> webContentBeans = createShowDate(contentBean.getContent());
                        mTextViewHolder.clearImage();
                        if (webContentBeans.size() > 0) {
                            for (WebContentBean webContentBean : webContentBeans) {
                                switch (webContentBean.getFlag()) {
                                    case EditBean.TYPE_AUDIO:
                                        mTextViewHolder.setAudio(webContentBean.getContent(), new MediaPlayerUtil.OnNextAudioListener() {
                                            @Override
                                            public void onNext(int position) {

                                            }
                                        });
                                        break;
                                    case EditBean.TYPE_PHOTO:
                                        mTextViewHolder.addImage(webContentBean.getContent());
                                        break;
                                    case EditBean.TYPE_TEXT:
                                        mTextViewHolder.setWebContent(webContentBean.getContent());
                                        break;
                                    case EditBean.TYPE_VIDEO:
                                        break;
                                }
                            }
                        }
                        mTextViewHolder.showImage(onClickImageListener);
                    } else {
                        //system
                        if (contentBean.getPhoto() != null && !contentBean.getPhoto().equals(""))
                            mTextViewHolder.setImages(contentBean.getPhoto(), onClickImageListener);
                        if (contentBean.getContent() != null && !contentBean.getContent().equals(""))
                            mTextViewHolder.setContent(contentBean.getContent());
                        if (contentBean.getAudio() != null && !contentBean.getAudio().equals(""))
                            mTextViewHolder.setAudio(contentBean.getAudio(), new MediaPlayerUtil.OnNextAudioListener() {
                                @Override
                                public void onNext(int position) {
                                    mTextViewHolder.setAudioNameText(position);
                                }
                            });
                    }

                    mTextViewHolder.setLiveView(contentBean.getLive());
                    mTextViewHolder.setNumberView(contentBean.getNumber());
                    mTextViewHolder.setType(contentBean.getType());
                    mTextViewHolder.setTimeText(contentBean.getTime());
                    mTextViewHolder.getItemView(contentBean).setOnClickListener(this);
                }
                break;
            case ITEM_TYPE_BOTTOM:
                ItemViewHolder.BottomViewHolder bottomViewHolder = (ItemViewHolder.BottomViewHolder) holder;
                switch (mCurFlag) {
                    case ITEM_BOTTOM_TYPE_FLAG_REFEWSH:
                        bottomViewHolder.showRefresh();
                        break;
                    case ITEM_BOTTOM_TYPE_FLAG_ERROR:
                        bottomViewHolder.showError();
                        break;
                    case ITEM_BOTTOM_TYPE_FLAG_END:
                        bottomViewHolder.showEnd();
                        break;
                }
                break;
            case ITEM_TYPE_HEADER:
                ItemViewHolder.TextViewHolder textViewHolder = (ItemViewHolder.TextViewHolder) holder;
                textViewHolder.getMsgView().setTextColor(context.getResources().getColor(R.color.mainTopColor));
                textViewHolder.setMsg(mHeaderMsg);
                break;

        }
    }

    private List<WebContentBean> createShowDate(String content) {
        try {
            return GsonUtils.parseJsonArrayWithGson(content, WebContentBean[].class);
        } catch (JsonSyntaxException e) {
            Log.d(MainItemAdapter.class.getName(), e.toString());
            return new ArrayList<>();
        }
    }

    @Override
    public int getItemCount() {
        if (mData.size() > 0)
            return mHeaderCount + mData.size() + mBottomCount;
        else
            return mHeaderCount + mData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderCount != 0 && position < mHeaderCount) {
            //头部View
            return ITEM_TYPE_HEADER;
        } else if (mBottomCount != 0 && position >= (mHeaderCount + mData.size())) {
            //底部View
            return ITEM_TYPE_BOTTOM;
        } else {
            //内容View
            return ITEM_TYPE_CONTENT;
        }
    }

    @Override
    public void onClick(View view) {
        ContentBean bean = (ContentBean) view.getTag();
        onContentItemListener.onItemClick(bean);
    }
}
