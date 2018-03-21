package enlightenment.com.main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enlightenment.com.base.R;
import enlightenment.com.tool.Image.MediaPlayerUtil;
import enlightenment.com.view.Dialog.ImageShowDialog;
import enlightenment.com.view.NineGridLayout.ItemNineGridLayout;

/**
 * Created by lw on 2017/9/6.
 */

public class ItemViewHolder {
    public static class TextViewHolder extends RecyclerView.ViewHolder {

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

        private boolean isloadAudio = false;

        private ArrayList<String> mAudioList = new ArrayList<>();

        public TextViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            ButterKnife.bind(this, itemView);
        }

        public View getItemView() {
            return itemView;
        }

        public void setType(String type) {
            if (type.equals("0")) {
                typeView.setText("学习");
            } else if (type.equals("1")) {
                typeView.setText("创造");
            }
        }

        public void setAudio(String urls, final MediaPlayerUtil.OnNextAudioListener onNextAudioListener) {
            audioLayout.setVisibility(View.VISIBLE);
            mAudioList.addAll(Arrays.asList(urls.split(",")));
            setAudioNameText(0);
            playImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isloadAudio) {
                        isloadAudio = !isloadAudio;
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
        }

        public void setNumberView(String number) {
            this.numberView.setText("评论 " + number);
        }

        public void setLiveView(String live) {
            this.liveView.setText("赞 " + live);
        }

        public void setContent(String content) {
            if (content != null && !content.equals("")) {
                contentView.setVisibility(View.VISIBLE);
                contentView.setText(content);
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
        public void onAvatarClick(View view){
            onContentItemListener.onAvatarClick();
        }

        @OnClick(R.id.item_content_top_model_name)
        public void onModelClick(View view){
            onContentItemListener.onModelClick();
        }
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class LoadViewHolder extends RecyclerView.ViewHolder {

        public LoadViewHolder(View itemView) {
            super(itemView);
        }
    }
}
