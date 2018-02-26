package enlightenment.com.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;

import enlightenment.com.base.R;
import enlightenment.com.tool.device.CheckUtils;

/**
 * Created by lw on 2017/7/28.
 */

public class MainItemAdapter extends RecyclerView.Adapter {

    private static final int TYPE_LOAD_MORE = 100;
    private static final int TYPE_TEXT_MORE = 1;

    private Context context;
    private int viewID;
    private ArrayList mData = new ArrayList();

    public MainItemAdapter(Context context, ArrayList mData, int viewID) {
        this.context = context;
        this.viewID = viewID;
        this.mData = mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder mViewHolder = null;
        View view;
        switch (viewType){
            case TYPE_LOAD_MORE:
                view = LayoutInflater.from(context).inflate(R.layout.item_footer_view, parent, false);
                mViewHolder = new ItemViewHolder.ImageViewHolder(view);
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
            Object bean = mData.get(position);
            if (bean instanceof ContentBean) {
                ContentBean contentBean = (ContentBean) bean;
                Glide.with(context).load(contentBean.getAvatar()).asBitmap().centerCrop()
                        .placeholder(R.drawable.image_logo)
                        .into(new BitmapImageViewTarget(mTextViewHolder.getAvatarView()){
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
                mTextViewHolder.setContent(contentBean.getContent());
                mTextViewHolder.setLiveView(contentBean.getLive());
                mTextViewHolder.setNumberView(contentBean.getNumber());
                mTextViewHolder.setType(contentBean.getType());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size()>6?mData.size()+1:mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position>=mData.size()){
            return TYPE_LOAD_MORE;
        }else {
            return TYPE_TEXT_MORE;
        }
    }
}
