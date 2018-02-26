package enlightenment.com.main;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import enlightenment.com.base.R;

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

        public void setNumberView(String number) {
            this.numberView.setText("评论 "+number);
        }

        public void setLiveView(String live) {
            this.liveView .setText("赞 "+live);
        }

        public void setContent(String content) {
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
