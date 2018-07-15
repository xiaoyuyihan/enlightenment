package enlightenment.com.details;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import enlightenment.com.base.R;
import enlightenment.com.operationBean.CommentBean;
import enlightenment.com.tool.glide.GlideCircleTransform;

/**
 * Created by admin on 2018/3/30.
 */

public class CommentAdapter extends RecyclerView.Adapter {

    private List<CommentBean> commentBeans;
    private Context mCon;

    public CommentAdapter(List<CommentBean> commentBeans,Context mCon){
        this.commentBeans=commentBeans;
        this.mCon=mCon;
    }

    public void setCommentBeans(List<CommentBean> commentBeans) {
        this.commentBeans = commentBeans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView= LayoutInflater.from(mCon).inflate(R.layout.view_item_comment,parent,false);
        return new CommentViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommentViewHolder commentViewHolder=(CommentViewHolder)holder;
        CommentBean bean=commentBeans.get(position);
        Glide.with(mCon).load(bean.getAvatar())
                .transform(new GlideCircleTransform(mCon))
                .into(commentViewHolder.getAvatar());
        commentViewHolder.setContent(bean.getContent());
        commentViewHolder.setName(bean.getUsername());
        commentViewHolder.setTime(bean.getTime());
    }

    @Override
    public int getItemCount() {
        return commentBeans.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.view_item_comment_avatar)
        ImageView avatar;
        @BindView(R.id.view_item_comment_name)
        TextView nameTextView;
        @BindView(R.id.view_item_comment_content)
        TextView contentTextView;
        @BindView(R.id.view_item_comment_time)
        TextView timeTextView;

        public ImageView getAvatar() {
            return avatar;
        }

        public void setName(String name) {
            nameTextView.setText(name);
        }

        public void setContent(String content) {
            contentTextView.setText(content);
        }

        public void setTime(String time) {
            timeTextView.setText(time);
        }

        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
