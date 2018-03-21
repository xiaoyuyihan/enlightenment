package com.edit.custom;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.edit.EditActivity;
import com.edit.OnFragmentResultListener;
import com.edit.bean.EditBean;
import com.provider.utils.IntentBean;
import com.utils.MessageDialogFragment;
import com.utils.TypeConverUtil;
import com.webedit.HtmlEditActivity;
import com.webeditproject.R;
import com.webeditproject.R2;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lw on 2018/1/17.
 * 自定义文本样式
 */

public class CustomEditFragment extends Fragment implements OnFragmentResultListener {
    private static  CustomEditFragment mCustomFragment;
    public static CustomEditFragment getInstance() {
        if (mCustomFragment==null)
            mCustomFragment=new CustomEditFragment();
        return mCustomFragment;
    }
    
    RecyclerView mRecyclerView;

    private ArrayList<EditBean> mEditBeanList = new ArrayList<>();
    private CustomEditAdapter mEditAdapter;
    private LinearLayoutManager linearLayoutManager;
    MessageDialogFragment messageDialogFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_recycler_only, container, false);
        init();
        return mRecyclerView;
    }

    private void init() {
        int mType=getArguments().getInt(EditActivity.ACTIVITY_EDIT_TYPE);
        if (mType==EditActivity.ACTIVITY_EDIT_TYPE_CUSTOM){
            //mEditBeanList.addAll(IntentBean.getInstance().getData());
        }

        linearLayoutManager = new LinearLayoutManager(getActivity());
        mEditAdapter = new CustomEditAdapter();
        mRecyclerView.setPadding(12,12,12,12);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mEditAdapter);
        SimpleItemTouchHelperCallback mCallback = new SimpleItemTouchHelperCallback<>(mEditAdapter, mEditBeanList);
        mCallback.setSwipeState(new SimpleItemTouchHelperCallback.SwipeStateAlterHelper() {
            @Override
            public void onLeftMove(float moveX, RecyclerView.ViewHolder holder) {

            }

            @Override
            public void onRightMove(float moveX, RecyclerView.ViewHolder holder) {

            }

            @Override
            public void onMoveConsummation(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = mRecyclerView.getChildLayoutPosition(viewHolder.itemView);
                showDeleteDialog(position);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void showDeleteDialog(final int position) {
        messageDialogFragment = new MessageDialogFragment();
        messageDialogFragment.setOnMsgDialogClickListener(new MessageDialogFragment.OnMsgDialogClickListener() {
            @Override
            public void onSureBut() {
                mEditBeanList.remove(position);
                messageDialogFragment.dismiss();
                mEditAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancel() {
                messageDialogFragment.dismiss();
                mEditAdapter.notifyDataSetChanged();
            }
        });
        messageDialogFragment.show(getChildFragmentManager(), "MessageDialogFragment");
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1 && data != null) {
            mEditBeanList.add((EditBean) data.getParcelableExtra(HtmlEditActivity.HTML_EDIT_DATA_KEY));
        } else if (requestCode == 2) {
            ArrayList d = IntentBean.getInstance().getChecks();
            mEditBeanList.addAll(d);
            IntentBean.getInstance().getChecks().clear();
        } else if (resultCode == 3 && data != null) {
            EditBean editBean = (EditBean) data.getParcelableExtra(HtmlEditActivity.HTML_EDIT_DATA_KEY);
            int position = data.getIntExtra(HtmlEditActivity.HTML_EDIT_POSITION_KEY, -1);
            if (position != -1) {
                mEditBeanList.remove(position);
                mEditBeanList.add(position, editBean);
            }
        }
        mEditAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onSubject(Intent intent) {
        if (mEditBeanList.size()>0){
            //mEditBeanList 提交
            intent.putExtra(EditActivity.ACTIVITY_EDIT_TYPE,EditActivity.ACTIVITY_EDIT_TYPE_CUSTOM);
            IntentBean.getInstance().setData(mEditBeanList);
            return true;
        }else {
            return false;
        }
    }

    class CustomEditAdapter extends RecyclerView.Adapter {

        private LayoutInflater mInflater;

        public CustomEditAdapter() {
            mInflater = LayoutInflater.from(getActivity());
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder mViewHolder = null;
            switch (viewType) {
                case EditBean.TYPE_AUDIO:
                    mViewHolder = new AudioViewHolder(mInflater.inflate(R.layout.view_item_edit_audio, parent, false));
                    break;
                case EditBean.TYPE_PHOTO:
                    mViewHolder = new PhotoViewHolder(mInflater.inflate(R.layout.view_item_edit_photo, parent, false));
                    break;
                case EditBean.TYPE_TEXT:
                    mViewHolder = new TextViewHolder(mInflater.inflate(R.layout.view_item_edit_text, parent, false));
                    break;
                case EditBean.TYPE_VIDEO:
                    mViewHolder = new VideoViewHolder(mInflater.inflate(R.layout.view_item_edit_video, parent, false));
                    break;
            }
            return mViewHolder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            Log.d("EditAdapter", "onBindViewHolder");
            final EditBean currentBean = mEditBeanList.get(position);
            if (holder instanceof TextViewHolder) {
                TextViewHolder textViewHolder = (TextViewHolder) holder;
                textViewHolder.setHtml(currentBean.getHTML5());
                textViewHolder.setHtmlSize(currentBean.getHtmlTextSize());
                textViewHolder.setGravity(currentBean.getGravity());
                textViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), HtmlEditActivity.class);
                        intent.putExtra(HtmlEditActivity.HTML_EDIT_TYPE_KEY, HtmlEditActivity.HTML_EDIT_TYPE_REWRITE);
                        intent.putExtra(HtmlEditActivity.HTML_EDIT_DATA_KEY, currentBean);
                        intent.putExtra(HtmlEditActivity.HTML_EDIT_POSITION_KEY, position);
                        startActivityForResult(intent, HtmlEditActivity.HTML_EDIT_TYPE_REWRITE);
                    }
                });
            } else if (holder instanceof AudioViewHolder) {
                AudioViewHolder audioViewHolder = (AudioViewHolder) holder;
                audioViewHolder.setTopName(currentBean.getProviderName());
                audioViewHolder.setTime(TypeConverUtil.TimeMSToMin(currentBean.getTime()));
                audioViewHolder.setPlayClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getActivity() instanceof AudioPlayClick) {
                            ((AudioPlayClick) getActivity()).onClick(holder, currentBean.getPath());
                        }
                    }
                });
            } else if (holder instanceof VideoViewHolder) {

            } else if (holder instanceof PhotoViewHolder) {
                PhotoViewHolder photoViewHolder = (PhotoViewHolder) holder;
                photoViewHolder.setPhotoView(currentBean.getPath());
            }
        }

        @Override
        public int getItemViewType(int position) {
            return mEditBeanList.get(position).getType();
        }

        @Override
        public int getItemCount() {
            return mEditBeanList.size();
        }

    }

    class TextViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.view_item_edit_text)
        TextView mTextView;

        public TextViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setHtml(String mHtml) {
            Spanned html;
            if (Build.VERSION.SDK_INT >= 24) {
                html = Html.fromHtml(mHtml, Html.FROM_HTML_MODE_LEGACY);
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

    class VideoViewHolder extends RecyclerView.ViewHolder {

        public VideoViewHolder(View itemView) {
            super(itemView);
        }
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.view_item_edit_photo)
        ImageView mPhotoView;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setPhotoView(String bitmap) {
            Glide.with(CustomEditFragment.this).load(new File(bitmap)).into(mPhotoView);
        }
    }

    public class AudioViewHolder extends RecyclerView.ViewHolder {
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

    public interface AudioPlayClick {
        void onClick(RecyclerView.ViewHolder holder, String path);
    }
}
