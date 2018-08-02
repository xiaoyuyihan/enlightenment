package enlightenment.com.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enlightenment.com.base.R;
import enlightenment.com.operationBean.ContentBean;
import enlightenment.com.tool.Image.MediaPlayerUtil;
import enlightenment.com.tool.device.CheckUtils;
import enlightenment.com.tool.device.DisplayUtils;
import enlightenment.com.view.Dialog.ImageShowDialog;
import enlightenment.com.view.NineGridLayout.ItemNineGridLayout;

/**
 * Created by lw on 2017/9/6.
 */

public class ItemViewHolder {
    public static class ItemHomeViewHolder extends RecyclerView.ViewHolder {

        View itemView;
        @BindView(R.id.item_content_top_avatar)
        ImageView avatarView;
        @BindView(R.id.item_content_top_username)
        TextView usernameView;
        @BindView(R.id.item_content_top_model_name)
        TextView modelNameView;
        @BindView(R.id.item_content_center_name)
        TextView contentNameView;
        @BindView(R.id.item_content_text_content)
        TextView contentView;
        @BindView(R.id.item_content_bottom_live)
        TextView liveView;
        @BindView(R.id.item_content_bottom_number)
        TextView numberView;
        @BindView(R.id.item_content_bottom_type)
        TextView typeView;
        @BindView(R.id.item_content_center_image)
        ItemNineGridLayout itemNineGridLayout;
        @BindView(R.id.item_content_audio_layout)
        LinearLayout audioLayout;
        @BindView(R.id.item_content_audio_play)
        ImageView playImage;
        @BindView(R.id.item_content_audio_name)
        TextView nameText;
        @BindView(R.id.item_content_bottom_time)
        TextView timeText;

        private OnContentItemListener onContentItemListener;

        public void setOnContentItemListener(OnContentItemListener onContentItemListener) {
            this.onContentItemListener = onContentItemListener;
        }

        private boolean isLoadAudio = false;

        private ArrayList<String> mAudioList = new ArrayList<>();

        public ItemHomeViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }

        public View getItemView(ContentBean bean) {
            itemView.setTag(bean);
            return itemView;
        }

        public void setType(String type) {
            if (type.equals("0")) {
                typeView.setText("学习");
            } else if (type.equals("1")) {
                typeView.setText("创造");
            }else if (type.equals("2")){
                typeView.setText("问题");
            }
        }

        public void setAudio(String urls, final MediaPlayerUtil.OnNextAudioListener onNextAudioListener) {
            audioLayout.setVisibility(View.VISIBLE);
            mAudioList.addAll(Arrays.asList(urls.split(",")));
            setAudioNameText(0);
            playImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isLoadAudio) {
                        isLoadAudio = !isLoadAudio;
                        MediaPlayerUtil.getInstance().setUrls(mAudioList)
                                .setOnNextListener(onNextAudioListener);
                    }
                    if (MediaPlayerUtil.getInstance().isPlay()) {
                        MediaPlayerUtil.getInstance().onPause();
                    } else {
                        MediaPlayerUtil.getInstance().onPlay();
                    }
                }
            });
        }

        public void setAudioNameText(int position) {
            String[] name = mAudioList.get(position).split("/");
            nameText.setText(name[name.length - 1]);
        }

        public void setImages(String urls, ItemNineGridLayout.OnClickImageListener onClickImageListener) {
            itemNineGridLayout.setIsShowAll(false);
            itemNineGridLayout.setUrlList(Arrays.asList(urls.split(",")));
            itemNineGridLayout.setOnClickImageListener(onClickImageListener);
            itemNineGridLayout.show();
        }

        public void clearImage(){
            itemNineGridLayout.clear();
        }

        public void addImage(String url){
            itemNineGridLayout.addUrl(url);
        }
        public void showImage(ItemNineGridLayout.OnClickImageListener onClickImageListener){
            itemNineGridLayout.setIsShowAll(false);
            itemNineGridLayout.setOnClickImageListener(onClickImageListener);
            itemNineGridLayout.show();
        }


        public void setNumberView(String number) {
            this.numberView.setText("评论 " + number);
        }

        public void setLiveView(String live) {
            this.liveView.setText("赞 " + live);
        }

        @SuppressLint("WrongConstant")
        public void setContent(String content) {
            if (content != null && !content.equals("")) {
                contentView.setVisibility(View.VISIBLE);
                contentView.setText(content);
            }
        }

        public void setWebContent(String content){
            if (content != null && !content.equals("")
                    &&contentView.getText().toString().equals("")) {
                contentView.setVisibility(View.VISIBLE);
                contentView.setText(Html.fromHtml(content));
            }
        }

        public void setContentNameView(String contentName) {
            this.contentNameView.setText(contentName);
        }

        public void setModelNameView(String modelName) {
            this.modelNameView.setText(modelName);
        }

        public void setUsernameView(String username) {
            this.usernameView.setText(username);
        }

        public ImageView getAvatarView() {
            return avatarView;
        }

        public void setTimeText(String timeText) {
            this.timeText.setText(timeText);
        }

        @OnClick(R.id.item_content_top_avatar)
        public void onAvatarClick(View view) {
            onContentItemListener.onAvatarClick("");
        }

        @OnClick(R.id.item_content_top_model_name)
        public void onModelClick(View view) {
            onContentItemListener.onModelClick(1, 1);
        }

        public void setHtmlContent(String html) {
            if (html != null && !html.equals("")) {
                contentView.setVisibility(View.VISIBLE);
                contentView.setText(Html.fromHtml(html));
            }
        }
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder {
        public BaseViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class BottomViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.footer_loading_layout)
        public LinearLayout mLoadingLayout;
        @BindView(R.id.footer_end_layout)
        public TextView mEndLayout;
        @BindView(R.id.footer_error_layout)
        public TextView mErrorLayout;

        public BottomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void showRefresh(){
            mLoadingLayout.setVisibility(View.VISIBLE);
            mEndLayout.setVisibility(View.GONE);
            mErrorLayout.setVisibility(View.GONE);
        }
        public void showEnd(){
            mLoadingLayout.setVisibility(View.GONE);
            mEndLayout.setVisibility(View.VISIBLE);
            mErrorLayout.setVisibility(View.GONE);
        }
        public void showError(){
            mLoadingLayout.setVisibility(View.GONE);
            mEndLayout.setVisibility(View.GONE);
            mErrorLayout.setVisibility(View.VISIBLE);
        }
    }

    public static class TextViewHolder extends RecyclerView.ViewHolder {
        private TextView msgView;
        public TextViewHolder(View itemView) {
            super(itemView);
            msgView= (TextView) itemView;
            msgView.setGravity(Gravity.CENTER);
        }

        public TextView getMsgView() {
            return msgView;
        }

        public void setMsg(String msg) {
            this.msgView.setText(msg);
        }
    }


    public static class MyselfToolTextView extends RecyclerView.ViewHolder {

        @BindView(R.id.item_myself_tool_name)
        TextView mToolName;
        @BindView(R.id.item_myself_tool_message)
        TextView mToolMessage;
        @BindView(R.id.item_myself_tool_image)
        ImageView mToolImage;

        Context context;

        public MyselfToolTextView(View view, Context context) {
            super(view);
            this.context = context;
            ButterKnife.bind(this, view);
        }

        public void setToolMessage(String message) {
            mToolMessage.setText(message);
        }

        public void setToolName(String name) {
            mToolName.setText(name);
        }

        public void setToolMessageVisibility(int visibility) {
            mToolMessage.setVisibility(visibility);
        }

        public void setToolImageVisibility(int visibility) {
            mToolImage.setVisibility(visibility);
        }

        public void setToolImageDrawable(Drawable drawable) {
            mToolImage.setImageDrawable(drawable);
        }

        public void setToolImageString(String string) {
            Glide.with(context).load(string).into(mToolImage);
        }
    }

    public static TextView createTextView(Context context, int height, int width) {
        TextView textView = new TextView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                width, height);
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setPadding(24, 2, 2, 2);
        textView.setTextSize(14);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextColor(context.getColor(R.color.text_color));
        } else {
            textView.setTextColor(context.getResources().getColor(R.color.text_color));
        }
        textView.setBackgroundResource(R.color.view_background);
        return textView;
    }
}
