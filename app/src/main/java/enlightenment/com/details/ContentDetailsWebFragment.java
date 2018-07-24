package enlightenment.com.details;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.edit.EditActivity;
import com.edit.OnBroadcastReceiverListener;
import com.edit.bean.WebContentBean;
import com.edit.custom.CustomViewHolder;
import com.google.gson.JsonSyntaxException;
import com.service.MediaService;
import com.utils.MessageDialogFragment;
import com.utils.TypeConverUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enlightenment.com.base.R;
import enlightenment.com.contents.FileUrls;
import enlightenment.com.operationBean.ContentBean;
import enlightenment.com.operationBean.CommentBean;
import enlightenment.com.tool.File.FileUtils;
import enlightenment.com.tool.device.CheckUtils;
import enlightenment.com.tool.glide.GlideCircleTransform;
import enlightenment.com.tool.gson.GsonUtils;
import enlightenment.com.view.FloatWindow;

/**
 * Created by lw on 2018/3/16.
 */

public class ContentDetailsWebFragment extends ContentDetailsFragment implements
        ContentEditAdapter.ContentItemClick, OnBroadcastReceiverListener,
        MessageDialogFragment.OnMsgDialogClickListener {

    //web
    @BindView(R.id.view_content_details_web_recycler)
    RecyclerView mContentRecycler;

    //bottom
    @BindView(R.id.view_content_details_bottom_arrow)
    ImageView mContentArrow;
    @BindView(R.id.view_content_details_bottom_name)
    TextView mContentUsername;
    @BindView(R.id.view_content_details_bottom_column)
    TextView mContentColumn;
    @BindView(R.id.view_content_details_bottom_model)
    TextView mContentModel;
    @BindView(R.id.view_content_details_bottom_follow)
    TextView mContentFollow;
    @BindView(R.id.view_content_details_bottom_reward)
    TextView mContentReward;
    @BindView(R.id.view_content_details_bottom_recycler)
    RecyclerView mCommentRecycler;

    private CommentAdapter mCommentAdapter;

    private View mContentView;
    private ContentBean mContent;
    private ContentEditAdapter mAdapter;

    private Timer mTimer;

    private MediaService mediaService;
    private EditActivity.DurationBroadcastReceiver broadcastReceiver;

    private SeekBar mViewHolderBar;
    private TextView mAudioTextView;
    private Handler mMainHandler;
    private ServiceConnection connection;
    private List<WebContentBean> mDate;

    private MessageDialogFragment messageDialogFragment;

    private TimerTask mTimerTask = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.view_content_details_web, container, false);
        ButterKnife.bind(this, mContentView);
        init();
        updateReward(mContentFollow);
        return mContentView;
    }

    private void init() {
        Glide.with(this).load(mContent.getAvatar())
                .transform(new GlideCircleTransform(getActivity()))
                .into(mContentArrow);
        mAdapter = new ContentEditAdapter(getActivity(),
                mDate,
                mContent.getName());
        mAdapter.setContentItemClick(this);
        mContentUsername.setText(mContent.getUsername());
        mContentModel.setText(CheckUtils.getModelName(mContent));
        mContentColumn.setText(mContent.getColumnName());
        mContentRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mContentRecycler.setAdapter(mAdapter);
        mContentRecycler.setNestedScrollingEnabled(false);
    }

    private void createShowData() {
        try {
            mDate = GsonUtils.parseJsonArrayWithGson(
                    mContent.getContent().toString(), WebContentBean[].class);
        } catch (JsonSyntaxException e) {
            createShowData();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onStartService();
        IntentFilter intentFilter = new IntentFilter();
        //设置接收广播的类型
        intentFilter.addAction(MediaService.ACTION_MEDIA_SERVICE);
        //调用Context的registerReceiver（）方法进行动态注册
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void clearData() {

    }

    @Override
    protected void initData() {
        initObject();
    }

    private void initObject() {
        if (mTimer == null)
            mTimer = new Timer();
        if (mMainHandler == null)
            mMainHandler = new Handler();
        if (connection == null)
            connection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    mediaService = ((MediaService.MediaBinder) service).getService();
                    //if (mediaService.getMediaPlayer().isPlaying())
                    //mediaService.sendDurationBroad(mediaService.getMediaPlayer().getDuration());
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    mediaService = null;
                }
            };
        if (broadcastReceiver == null)
            broadcastReceiver = new EditActivity.DurationBroadcastReceiver(this);
        mContent = getArguments().getParcelable(ContentDetailsActivity.CONTENT_EXTRA_DATA);
        createShowData();
    }

    public void onStartService() {
        Intent intent = new Intent(getActivity(), MediaService.class);
        getActivity().bindService(intent, connection, getActivity().BIND_AUTO_CREATE);
    }

    @Override
    public void onPause() {
        super.onPause();
        showFloatWindow();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null)
            mTimer.cancel();
        if (mTimerTask != null)
            mTimerTask.cancel();
        getActivity().unbindService(connection);
        getActivity().unregisterReceiver(broadcastReceiver);
        mediaService = null;
        broadcastReceiver = null;
        mTimer = null;
        mTimerTask = null;
    }

    private void showFloatWindow() {
        if (CheckUtils.checkFloatPermission(getActivity()) &&
                mediaService != null &&
                mediaService.getAudioState() == MediaService.SERVICE_MEDIA_STATE_PLAYING) {
            FloatWindow floatWindow = FloatWindow.newInstance(mContent.getId(), mediaService);
            floatWindow.showFloatWindow();
            floatWindow = null;
        } else
            mediaService.onCloseAudio();
    }

    //文章作者
    @OnClick({R.id.view_content_details_arrow, R.id.view_content_details_bottom_name})
    public void onUsername(View v) {
        if (getActivity() instanceof onContentDetailsListener)
            ((onContentDetailsListener) getActivity()).onUsername();
    }

    //栏目
    @OnClick(R.id.view_content_details_bottom_column)
    public void onColumn(View v) {
        if (getActivity() instanceof onContentDetailsListener)
            ((onContentDetailsListener) getActivity()).onColumn();
    }

    //模块
    @OnClick(R.id.view_content_details_bottom_model)
    public void onModel(View v) {
        if (getActivity() instanceof onContentDetailsListener)
            ((onContentDetailsListener) getActivity()).onModel();
    }

    //关注
    @OnClick(R.id.view_content_details_bottom_follow)
    public void onFollow(View v) {
        if (getActivity() instanceof onContentDetailsListener)
            ((onContentDetailsListener) getActivity()).onFollow();
        updateReward(mContentFollow);
    }

    //打赏
    @OnClick(R.id.view_content_details_bottom_reward)
    public void onReward(View v) {
        if (getActivity() instanceof onContentDetailsListener)
            ((onContentDetailsListener) getActivity()).onReward();
    }

    @SuppressLint("ResourceAsColor")
    private void updateReward(TextView textView) {
        if (Boolean.valueOf(mContent.getIsAtten())) {
            textView.setBackground(getActivity().getDrawable(R.drawable.background_un_interest_module));
            textView.setText("取关");
            textView.setTextColor(R.color.mainTopColor);
        } else {
            textView.setBackground(getActivity().getDrawable(R.drawable.background_interest_module));
            textView.setText("关注");
            textView.setTextColor(R.color.white);
        }
    }

    @Override
    void updateComments(List<CommentBean> commentBeans) {
        if (mCommentAdapter == null) {
            mCommentAdapter = new CommentAdapter(commentBeans, getActivity());
            mCommentRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            mCommentRecycler.setAdapter(mCommentAdapter);
            mCommentRecycler.setNestedScrollingEnabled(false);
        } else {
            mCommentAdapter.setCommentBeans(commentBeans);
            mCommentAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onPhotoClick(String path) {

    }

    @Override
    public void onAudioClick(RecyclerView.ViewHolder holder, String path) {
        if (holder instanceof CustomViewHolder.AudioViewHolder) {
            mViewHolderBar = ((CustomViewHolder.AudioViewHolder) holder).getSeekBar();
            mAudioTextView = ((CustomViewHolder.AudioViewHolder) holder).getCurrentTime();
        }
        if (!CheckUtils.checkFloatPermission(getActivity())) {
            messageDialogFragment = new MessageDialogFragment();
            messageDialogFragment.setMessage("某些功能需要您的悬浮窗支持一下，点击开启权限");
            messageDialogFragment.setMessageImageVisibility(View.GONE);
            messageDialogFragment.setOnMsgDialogClickListener(this);
            messageDialogFragment.show(getChildFragmentManager(), "MessageDialogFragment");
        }
        FloatWindow.releaseWindowPlay();
        mediaService.setMediaDataSource(path);
    }

    /**
     * 广播回调
     *
     * @param intent
     */

    @Override
    public void onReceiver(Intent intent) {
        if (mViewHolderBar != null) {
            mViewHolderBar.setMax(intent.getIntExtra(MediaService.DATA_AUDIO_DURATION, 0));
            if (mTimerTask != null) {
                mTimerTask.cancel();
                mTimerTask = null;
            }
            mTimerTask = createMediaTask();
            mTimer.schedule(mTimerTask, 0, 10);
        }
    }

    @Override
    public void onSureBut() {
        CheckUtils.applyFloatPermission(getActivity());
        onCancel();
    }

    @Override
    public void onCancel() {
        if (messageDialogFragment != null) {
            messageDialogFragment.dismiss();
            messageDialogFragment = null;
        }
    }

    private TimerTask createMediaTask() {
        return new TimerTask() {
            @Override
            public void run() {
                if (mediaService == null)
                    return;
                if (mediaService.getMediaPlayer() == null) {
                    return;
                }
                int position;
                try {
                    position = mediaService.getMediaPlayer().getCurrentPosition();
                } catch (IllegalStateException e) {
                    position = 0;
                }
                final int current = position;
                mViewHolderBar.setProgress(current);
                mViewHolderBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        mediaService.onSeekTo(seekBar.getProgress());
                    }
                });
                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAudioTextView.setText(TypeConverUtil.TimeMSToMin(current));
                    }
                });
            }
        };
    }
}
