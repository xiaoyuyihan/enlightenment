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
import enlightenment.com.tool.gson.GsonUtils;
import enlightenment.com.view.NineGridLayout.ItemNineGridLayout;

/**
 * Created by lw on 2017/7/28.
 */

public class MainItemAdapter extends RecyclerView.Adapter {

    private static final int TYPE_LOAD_MORE = 100;
    private static final int TYPE_TEXT_MORE = 1;

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
            case TYPE_LOAD_MORE:
                view = LayoutInflater.from(context).inflate(R.layout.item_footer_view, parent, false);
                mViewHolder = new ItemViewHolder.BaseViewHolder(view);
                break;
            case TYPE_TEXT_MORE:
                view = LayoutInflater.from(context).inflate(viewID, parent, false);
                mViewHolder = new ItemViewHolder.TextViewHolder(view);
                break;
        }
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder.TextViewHolder) {
            final ItemViewHolder.TextViewHolder mTextViewHolder = (ItemViewHolder.TextViewHolder) holder;
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
                if (GsonUtils.isJSONVaild(contentBean.getContent())) {
                    List<WebContentBean> webContentBeans = createShowDate(contentBean.getContent());
                    if (webContentBeans.size() > 0) {
                        switch (webContentBeans.get(0).getViewFlag()) {
                            case EditBean.TYPE_AUDIO:
                                mTextViewHolder.setAudio(webContentBeans.get(0).getContent(), new MediaPlayerUtil.OnNextAudioListener() {
                                    @Override
                                    public void onNext(int position) {

                                    }
                                });
                                break;
                            case EditBean.TYPE_PHOTO:
                                mTextViewHolder.setImages(webContentBeans.get(0).getContent(), onClickImageListener);
                                break;
                            case EditBean.TYPE_TEXT:
                                mTextViewHolder.setWebContent(webContentBeans.get(0).getContent());
                                break;
                            case EditBean.TYPE_VIDEO:
                                break;
                        }
                    }
                } else {
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
                mTextViewHolder.getItemView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onContentItemListener.onItemClick((ContentBean) bean);
                    }
                });
            }
        }
    }

    private List<WebContentBean> createShowDate(String content) {
        try{
            return GsonUtils.parseJsonArrayWithGson(content, WebContentBean[].class);
        }catch (JsonSyntaxException e){
            Log.d(MainItemAdapter.class.getName(),e.toString());
            return createShowDate(content);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size() > 6 ? mData.size() + 1 : mData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= mData.size()) {
            return TYPE_LOAD_MORE;
        } else {
            return TYPE_TEXT_MORE;
        }
    }
}
