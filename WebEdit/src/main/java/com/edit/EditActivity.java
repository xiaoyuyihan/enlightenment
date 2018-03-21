package com.edit;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.edit.automatic.AutomaticFragment;
import com.edit.custom.CustomEditFragment;
import com.provider.utils.ContentProviderUtils;
import com.provider.utils.IntentBean;
import com.provider.view.ProviderActivity;
import com.service.MediaService;
import com.utils.TypeConverUtil;
import com.webedit.HtmlEditActivity;
import com.webeditproject.R;
import com.webeditproject.R2;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lw on 2018/1/2.
 */

public class EditActivity extends BaseActivity implements CustomEditFragment.AudioPlayClick,
        OnBroadcastReceiverListener {

    public static final String ACTIVITY_EDIT_TYPE="ACTIVITY_EDIT_TYPE";
    public static final String ACTIVITY_EDIT_TEXT="ACTIVITY_EDIT_TEXT";
    public static final int ACTIVITY_EDIT_TYPE_CREATE=0;
    public static final int ACTIVITY_EDIT_TYPE_CUSTOM=1;
    public static final int ACTIVITY_EDIT_TYPE_AUTOMTIC=2;

    @BindView(R2.id.layout_edit_common)
    RelativeLayout mMainView;
    @BindView(R2.id.edit_common_top_back)
    TextView mBackView;
    @BindView(R2.id.edit_common_top_next)
    TextView mNextView;
    @BindView(R2.id.edit_common_top_menu)
    FloatingActionsMenu mFloatingActionsMenu;
    @BindView(R2.id.edit_common_menu_audio)
    View mAudio;
    @BindView(R2.id.edit_common_menu_video)
    View mVideo;
    @BindView(R2.id.edit_common_menu_photo)
    View mPhoto;
    @BindView(R2.id.edit_common_menu_text)
    View mText;
    @BindView(R2.id.edit_common_custom)
    ImageView mCustom;
    @BindView(R2.id.edit_common_automatic)
    ImageView mAutomatic;

    private Timer mTimer = new Timer();

    private MediaService mediaService;
    private DurationBroadcastReceiver broadcastReceiver = new DurationBroadcastReceiver(this);

    private SeekBar mViewHolderBar;
    private TextView mAudioTextView;
    private Handler mMainHandler = new Handler();

    private FragmentManager fragmentManager;
    private Fragment currentFragment;

    private int mType;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mediaService = ((MediaService.MediaBinder) service).getService();
            if (mediaService.getMediaPlayer().isPlaying())
                mediaService.sendDurationBroad(mediaService.getMediaPlayer().getDuration());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mediaService = null;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_common);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        init();
    }

    private void init() {
        mType = getIntent().getExtras().getInt(EditActivity.ACTIVITY_EDIT_TYPE,0);
        if (mType==EditActivity.ACTIVITY_EDIT_TYPE_CREATE
                ||mType==EditActivity.ACTIVITY_EDIT_TYPE_CUSTOM){
            mCustom.performClick();
        }else {
            mAutomatic.performClick();
        }
        IntentBean.getInstance().clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onStartService();
        IntentFilter intentFilter = new IntentFilter();
        //设置接收广播的类型
        intentFilter.addAction(MediaService.ACTION_MEDIA_SERVICE);
        //调用Context的registerReceiver（）方法进行动态注册
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(connection);
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (currentFragment instanceof OnFragmentResultListener) {
            ((OnFragmentResultListener) currentFragment).onFragmentResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R2.id.edit_common_top_back)
    public void onBack(View v) {
        finish();
    }

    @OnClick(R2.id.edit_common_automatic)
    public void onAutomatic(View view) {
        mText.setVisibility(View.GONE);
        currentFragment = AutomaticFragment.getInstance();
        currentFragment.setArguments(newBundle());
        mAutomatic.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.powderBlue)));
        mCustom.setBackgroundTintList(null);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.edit_common_fragment, currentFragment).commit();
    }

    private Bundle newBundle() {
        Bundle bundle=new Bundle();
        bundle.putInt(EditActivity.ACTIVITY_EDIT_TYPE,mType);
        if (mType==EditActivity.ACTIVITY_EDIT_TYPE_AUTOMTIC){
            String text=getIntent().getExtras().getString(EditActivity.ACTIVITY_EDIT_TEXT,"");
            bundle.putString(EditActivity.ACTIVITY_EDIT_TEXT,text);
        }
        return bundle;
    }

    @OnClick(R2.id.edit_common_custom)
    public void onCustom(View view) {
        mText.setVisibility(View.VISIBLE);
        currentFragment = CustomEditFragment.getInstance();
        currentFragment.setArguments(newBundle());
        mCustom.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.powderBlue)));
        mAutomatic.setBackgroundTintList(null);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragmentManager.findFragmentById(R.id.edit_common_fragment) == null) {
            transaction.add(R.id.edit_common_fragment, currentFragment).commit();
        } else
            transaction.replace(R.id.edit_common_fragment, currentFragment).commit();
    }

    @OnClick(R2.id.edit_common_top_next)
    public void onNextActivity(View v) {
        Intent intent = new Intent("com.information.INFORMATION");
        boolean falg=true;
        if (currentFragment instanceof OnFragmentResultListener)
            falg=((OnFragmentResultListener) currentFragment).onSubject(intent);
        if (falg){
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(this,"文本或资源不能为空",Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick({R2.id.edit_common_menu_photo, R2.id.edit_common_menu_video, R2.id.edit_common_menu_audio})
    public void onProviderActivity(View v) {
        Intent intent = null;
        int i = v.getId();
        if (i == R.id.edit_common_menu_audio) {
            intent = new Intent("com.provider.ACTION_PROVIDER");
            intent.putExtra(ProviderActivity.TYPE_KEY, ContentProviderUtils.TYPE_AUDIO);

        } else if (i == R.id.edit_common_menu_photo) {
            intent = new Intent("com.camera.ACTION_START_CAMERA");
            intent.putExtra("CAMERA_OPEN_TYPE", ContentProviderUtils.TYPE_PHOTO);
            intent.putExtra(ProviderActivity.TYPE_KEY, ContentProviderUtils.TYPE_PHOTO);

        } else if (i == R.id.edit_common_menu_video) {
            intent = new Intent("com.camera.ACTION_START_CAMERA");
            intent.putExtra("CAMERA_OPEN_TYPE", ContentProviderUtils.TYPE_VIDEO);
            intent.putExtra(ProviderActivity.TYPE_KEY, ContentProviderUtils.TYPE_VIDEO);

        }
        startActivityForResult(intent, 2);
        mFloatingActionsMenu.performClick();
    }

    @OnClick(R2.id.edit_common_menu_text)
    public void onHtmlEditActivity(View v) {
        Intent intent = new Intent(this, HtmlEditActivity.class);
        startActivityForResult(intent, 1);
    }

    public void onStartService() {
        Intent intent = new Intent(this, MediaService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    @Override
    public void onReceiver(Intent intent) {
        mViewHolderBar.setMax(intent.getIntExtra(MediaService.DATA_AUDIO_DURATION, 0));
        TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                final int current = mediaService.getMediaPlayer().getCurrentPosition();
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
        mTimer.schedule(mTimerTask, 0, 10);
    }

    @Override
    public void onClick(RecyclerView.ViewHolder holder, String path) {
        if (holder instanceof CustomEditFragment.AudioViewHolder) {
            mViewHolderBar = ((CustomEditFragment.AudioViewHolder) holder).getSeekBar();
            mAudioTextView = ((CustomEditFragment.AudioViewHolder) holder).getCurrentTime();
        }
        mediaService.setMediaDataSource(path);
    }

    class DurationBroadcastReceiver extends BroadcastReceiver {

        private OnBroadcastReceiverListener onBroadcastReceiverListener;

        public DurationBroadcastReceiver(OnBroadcastReceiverListener onBroadcastReceiverListener) {
            this.onBroadcastReceiverListener = onBroadcastReceiverListener;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            onBroadcastReceiverListener.onReceiver(intent);
        }
    }
}
