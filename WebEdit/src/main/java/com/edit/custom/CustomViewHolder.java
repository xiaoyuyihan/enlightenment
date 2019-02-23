package com.edit.custom;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.webeditproject.R;
import com.webeditproject.R2;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 2018/6/15.
 */

public class CustomViewHolder {

    public static class TextViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.view_item_edit_text)
        TextView mTextView;

        public TextViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setHtml(String mHtml) {
            Spanned html;
            if (Build.VERSION.SDK_INT >= 24) {
                html = Html.fromHtml("<big>"+mHtml+">", Html.FROM_HTML_MODE_LEGACY);
            } else {
                html = Html.fromHtml(mHtml);
            }
            if (html.length() > 2)
                html = (Spanned) html.subSequence(0, html.length() - 2);
            mTextView.setText(html);
        }

        public void setHtmlSize(int size) {
            mTextView.setTextSize(size);
        }

        public void setGravity(String gravity) {
            if (gravity.equals("left")) {
                mTextView.setGravity(Gravity.LEFT);
            } else if (gravity.equals("center")) {
                mTextView.setGravity(Gravity.CENTER);
            } else if (gravity.equals("right")) {
                mTextView.setGravity(Gravity.RIGHT);
            }
        }
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {

        public VideoViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.view_item_edit_photo)
        ImageView mPhotoView;
        Context context;

        public PhotoViewHolder(View itemView, Context context) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.context = context;
        }

        public void setPhotoView(String bitmap) {
            Glide.with(context).load(new File(bitmap)).placeholder(R.drawable.ic_banner_default).into(mPhotoView);
        }

        public void setWebPhotoView(String url){
            Glide.with(context).load(url).placeholder(R.drawable.ic_banner_default).into(mPhotoView);
        }
    }

    public static class AudioViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.view_item_edit_audio_play)
        ImageView mPlay;
        @BindView(R2.id.view_item_edit_audio_top)
        TextView mTopName;
        @BindView(R2.id.view_item_edit_audio_seekbar)
        SeekBar mSeekBar;
        @BindView(R2.id.view_item_edit_audio_current_time)
        TextView mCurrentTime;
        @BindView(R2.id.view_item_edit_audio_time)
        TextView mTime;

        public AudioViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setPlayClick(View.OnClickListener onClickListener) {
            mPlay.setOnClickListener(onClickListener);
        }

        public void setTime(String time) {
            mTime.setText(time);
        }

        public void setSeekBar(int prosition) {
            this.mSeekBar.setProgress(prosition);
        }

        public TextView getCurrentTime() {
            return mCurrentTime;
        }

        public SeekBar getSeekBar() {
            return mSeekBar;
        }

        public void setTopName(String name) {
            mTopName.setText(name);
        }
    }

}
