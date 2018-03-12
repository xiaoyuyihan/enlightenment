package enlightenment.com.main;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import enlightenment.com.base.R;
import enlightenment.com.tool.Image.MediaPlayerUtil;
import enlightenment.com.view.NineGridLayout.ItemNineGridLayout;

/**
 * Created by lw on 2017/9/6.
 */

public class ItemViewHolder {
    public static class TextViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.item_content_top_avatar)
        ImageView avatarView;
        @BindView(R.id.item_content_top_username)
        TextView usernameView;
        @BindView(R.id.item_content_top_model_name)
        TextView modelNameView;
        @BindView(R.id.item_content_center_name)
        TextView contentNameView;
        @BindView(R.id.item_content_center_content)
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
        LinearLayout aduioLayout;
        @BindView(R.id.item_content_audio_play)
        ImageView playImage;
        @BindView(R.id.item_content_audio_name)
        TextView nameText;

        private boolean isloadAudio=false;

        private ArrayList<String> mAudioList=new ArrayList<>();

        public TextViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setType(String type) {
            if (type.equals("0")){
                typeView.setText("学习");
            }else if (type.equals("1")){
                typeView.setText("创造");
            }
        }

        public void setAudio(String urls, final MediaPlayerUtil.OnNextAudioListener onNextAudioListener){
            aduioLayout.setVisibility(View.VISIBLE);
            mAudioList.addAll(Arrays.asList(urls.split(",")));
            setAudioNameText(0);
            playImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isloadAudio){
                        isloadAudio=!isloadAudio;
                        MediaPlayerUtil.getInstance().setUrls(mAudioList)
                                .setOnNextListener(onNextAudioListener);
                    }
                    if (MediaPlayerUtil.getInstance().isPlay()){
                        MediaPlayerUtil.getInstance().onPause();
                    }else {
                        MediaPlayerUtil.getInstance().onPlay();
                    }
                }
            });
        }

        public void setAudioNameText(int position){
            String[] name=mAudioList.get(position).split("/");
            nameText.setText(name[name.length-1]);
        }

        public void setImages(String urls){
            itemNineGridLayout.setIsShowAll(true);
            itemNineGridLayout.setUrlList(Arrays.asList(urls.split(",")));
        }

        public void setNumberView(String number) {
            this.numberView.setText("评论 "+number);
        }

        public void setLiveView(String live) {
            this.liveView .setText("赞 "+live);
        }

        public void setContent(String content) {
            contentView.setVisibility(View.VISIBLE);
            contentView.setText(Html.fromHtml(content));
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
    }
    public static class ImageViewHolder extends RecyclerView.ViewHolder{

        public ImageViewHolder(View itemView) {
            super(itemView);
        }
    }
    public static class LoadViewHolder extends RecyclerView.ViewHolder{

        public LoadViewHolder(View itemView) {
            super(itemView);
        }
    }
}
