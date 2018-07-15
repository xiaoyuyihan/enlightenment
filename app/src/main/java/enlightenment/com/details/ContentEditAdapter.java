package enlightenment.com.details;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edit.bean.EditBean;
import com.edit.bean.WebContentBean;
import com.edit.custom.CustomEditFragment;
import com.edit.custom.CustomViewHolder;
import com.utils.TypeConverUtil;
import com.webeditproject.R;
import com.yalantis.contextmenu.lib.Utils;

import java.util.List;

import enlightenment.com.main.ItemViewHolder;
import enlightenment.com.tool.device.DisplayUtils;

/**
 * Created by admin on 2018/6/14.
 */

public class ContentEditAdapter extends RecyclerView.Adapter {

    private static final int CONTENT_TOP_NAME = 1000;

    private LayoutInflater mInflater;
    private List<WebContentBean> mEditBeanList;
    private Activity context;
    private String mContentName;

    private ContentItemClick contentItemClick;

    public ContentEditAdapter(Activity context,
                              List<WebContentBean> mEditBeanList, String contentName) {
        mInflater = LayoutInflater.from(context);
        this.mEditBeanList = mEditBeanList;
        this.context = context;
        this.mContentName = contentName;
    }

    public void setContentItemClick(ContentItemClick contentItemClick) {
        this.contentItemClick = contentItemClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder mViewHolder = null;
        switch (viewType) {
            case EditBean.TYPE_AUDIO:
                mViewHolder = new CustomViewHolder.AudioViewHolder(
                        mInflater.inflate(R.layout.view_item_edit_audio,
                                parent, false));

                break;
            case EditBean.TYPE_PHOTO:
                mViewHolder = new CustomViewHolder.PhotoViewHolder(
                        mInflater.inflate(R.layout.view_item_edit_photo, parent, false),
                        context);
                break;
            case EditBean.TYPE_TEXT:
                mViewHolder = new CustomViewHolder.TextViewHolder(
                        mInflater.inflate(R.layout.view_item_edit_text,
                                parent, false));
                break;
            case EditBean.TYPE_VIDEO:
                mViewHolder = new CustomViewHolder.VideoViewHolder(
                        mInflater.inflate(R.layout.view_item_edit_video,
                                parent, false));
                break;
            case CONTENT_TOP_NAME:
                TextView textView = ItemViewHolder.createTextView(context,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                textView.setTextSize(24);
                textView .setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                mViewHolder =
                        new ItemViewHolder.BaseViewHolder(textView);
                break;
        }
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Log.d("EditAdapter", "onBindViewHolder");
        WebContentBean currentBean = null;
        if (position!=0)
            currentBean = mEditBeanList.get(position-1);
        switch (getItemViewType(position)) {
            case EditBean.TYPE_TEXT:
                CustomViewHolder.TextViewHolder textViewHolder = (CustomViewHolder.TextViewHolder) holder;
                textViewHolder.setHtml(currentBean.getContent());
                break;
            case EditBean.TYPE_AUDIO:
                final WebContentBean bean = currentBean;
                CustomViewHolder.AudioViewHolder audioViewHolder = (CustomViewHolder.AudioViewHolder) holder;
                audioViewHolder.setTopName(currentBean.getName());
                audioViewHolder.setTime(TypeConverUtil.TimeMSToMin(currentBean.getTime()));
                audioViewHolder.setPlayClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (contentItemClick!=null){
                            contentItemClick.onAudioClick(holder,bean.getContent());
                        }
                    }
                });
                break;
            case EditBean.TYPE_VIDEO:
                break;
            case EditBean.TYPE_PHOTO:
                CustomViewHolder.PhotoViewHolder photoViewHolder = (CustomViewHolder.PhotoViewHolder) holder;
                photoViewHolder.setWebPhotoView(currentBean.getContent());
                final WebContentBean finalCurrentBean = currentBean;
                photoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (contentItemClick!=null){
                            contentItemClick.onPhotoClick(finalCurrentBean.getContent());
                        }
                    }
                });
                break;
            case CONTENT_TOP_NAME:
                ((TextView) ((ItemViewHolder.BaseViewHolder) holder).itemView).setText(mContentName);
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position != 0) {
            return mEditBeanList.get(position - 1).getFlag();
        } else
            return CONTENT_TOP_NAME;
    }

    @Override
    public int getItemCount() {
        return mEditBeanList.size() + 1;
    }

    public interface ContentItemClick{
        void onPhotoClick(String path);
        void onAudioClick(RecyclerView.ViewHolder holder, String path);
    }

}