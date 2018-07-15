package com.audio;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaFormat;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.audio.wave.draw.AudioRecordUtil;
import com.audio.wave.utils.AudioConversionUtil;
import com.audio.wave.utils.SamplePlayer;
import com.audio.wave.utils.SoundFile;
import com.audio.wave.view.WavaTimeView;
import com.audio.wave.view.WaveformView;
import com.edit.bean.EditBean;
import com.provider.utils.ContentProviderUtils;
import com.provider.utils.IntentBean;
import com.provider.view.LoadingDialog;
import com.provider.view.ProviderActivity;
import com.utils.AppActivity;
import com.utils.AppPermissionsDispatcher;
import com.utils.FileUtils;
import com.webeditproject.R;
import com.webeditproject.R2;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;

/**
 * Created by admin on 2018/6/20.
 */

public class AudioActivity extends AppActivity implements
        AudioConversionUtil.OnCompleteListener {

    public static final int ACTIVITY_PROVIDER_CODE = 2;

    public static final int AUDIO_STATE_RESULT_INITIAL = 1; //初始
    public static final int AUDIO_STATE_RESULT_START = 2;   //开始录音
    public static final int AUDIO_STATE_RESULT_STOP = 3;    //录音结束
    public static final int AUDIO_STATE_RESULT_PLAY = 4;    //录音播放
    public static final int AUDIO_STATE_RESULT_PAUSE = 5;   //录音暂停

    public static final int AUDIO_STATE_PLAY_INITIAL = 6;   //初始
    public static final int AUDIO_STATE_PLAY_START = 7;     //开始播放
    public static final int AUDIO_STATE_PLAY_STOP = 8;      //停止播放

    private static final int FREQUENCY = 16000;// 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    private static final int CHANNELCONGIFIGURATION = AudioFormat.CHANNEL_IN_MONO;// 设置单声道声道
    private static final int AUDIOENCODING = AudioFormat.ENCODING_PCM_16BIT;// 音频数据格式：每个样本16位
    public final static int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;// 音频获取源
    private int recBufSize;// 录音最小buffer大小
    private AudioRecord audioRecord;
    private AudioRecordUtil audioRecordUtil;

    private Thread mLoadSoundFileThread;
    SoundFile mSoundFile;
    boolean mLoadingKeepGoing;
    SamplePlayer mPlayer;
    AudioConversionUtil audioCodec;

    private String mFileName = String.valueOf(System.currentTimeMillis());//文件名

    private boolean isRecordFile = true;
    private boolean isPlay = false;
    private int curState = 0;

    private ArrayList<EditBean> audioList = new ArrayList<>();

    @BindView(R2.id.audio_wave_sur)
    WavaTimeView waveSfv;
    @BindView(R2.id.audio_wave_view)
    WaveformView waveView;
    @BindView(R2.id.audio_play_state)
    ImageView mPlayView;
    @BindView(R2.id.audio_record_state)
    ImageView mRecordView;
    @BindView(R2.id.audio_file_text)
    TextView mFileView;

    private long startTime = 0;

    private LoadingDialog loadingDialog;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    initWaveView(audioList.get(0).getPath());
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        ButterKnife.bind(this);
        if (waveSfv != null) {
            waveSfv.setLine_off(42);
        }
        waveView.setLine_offset(42);
        initPermission();
        updateAudioView();
        FileUtils.isCreateFile(FileUtils.getAudioParentFilePath() + mFileName + ".wav");
    }

    /**
     * 初始化权限
     */
    public void initPermission() {
        AppPermissionsDispatcher.initAudioWithCheck(this);
    }

    @Override
    @NeedsPermission({Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE})
    public void initAudio() {
        recBufSize = AudioRecord.getMinBufferSize(FREQUENCY,
                CHANNELCONGIFIGURATION, AUDIOENCODING);// 录音组件
        audioRecord = new AudioRecord(AUDIO_SOURCE,// 指定音频来源，这里为麦克风
                FREQUENCY, // 16000HZ采样频率
                CHANNELCONGIFIGURATION,// 录制通道
                AUDIO_SOURCE,// 录制编码格式
                recBufSize);// 录制缓冲区大小 //先修改
    }

    @Override
    public void initCamera() {

    }

    @Override
    @OnPermissionDenied({Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE})
    public void showRecordDenied() {
        Toast.makeText(this, "拒绝录音权限我们将无法欣赏你的声音", Toast.LENGTH_LONG).show();
    }

    @Override
    @OnNeverAskAgain({Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE})
    public void onRecordNeverAskAgain() {
        new AlertDialog.Builder(this)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: 2016/11/10 打开系统设置权限
                        dialog.cancel();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage("您已经禁止了录音权限,是否现在去开启")
                .show();
    }

    @Override
    @OnShowRationale({Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE})
    public void showRationaleForRecord(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage("是否开启录音权限")
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_PROVIDER_CODE) {
            ArrayList<EditBean> d = IntentBean.getInstance().getChecks();
            if (d != null && d.size() > 0) {
                isRecordFile = false;
                curState = AUDIO_STATE_PLAY_INITIAL;
                setResult(ACTIVITY_PROVIDER_CODE);
                finish();
                /*audioList.clear();
                audioList.addAll(0, d);
                mFileView.setText("文件名" + d.get(0).getProviderName());*/
                //loadAudioFile();
            } else {
                isRecordFile = true;
                curState = AUDIO_STATE_RESULT_INITIAL;
            }
            updateAudioView();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadAudioFile() {
        //showLoadingDialog();
        audioCodec = AudioConversionUtil.newInstance();
        audioCodec.setIOPath(audioList.get(0).getPath(), FileUtils.getAudioCurFilePath());
        audioCodec.setOnCompleteListener(this);
        audioCodec.prepare();
        audioCodec.startAsync();
    }

    /**
     * 根据类型跟新视图
     */
    private void updateAudioView() {
        if (isRecordFile) {
            initRecordView();
        } else {
            initPlayView();
        }
    }

    private void initPlayView() {
        curState = AUDIO_STATE_PLAY_INITIAL;
        mRecordView.setVisibility(View.INVISIBLE);
        waveSfv.setVisibility(View.INVISIBLE);
        waveView.setVisibility(View.VISIBLE);
        mPlayView.setVisibility(View.VISIBLE);
        mPlayView.setImageDrawable(getDrawable(R.drawable.ic_audio_play));
    }

    private void initRecordView() {
        curState = AUDIO_STATE_RESULT_INITIAL;
        mPlayView.setVisibility(View.INVISIBLE);
        waveSfv.setVisibility(View.VISIBLE);
        waveView.setVisibility(View.INVISIBLE);
        mRecordView.setVisibility(View.VISIBLE);
        mRecordView.setImageDrawable(getDrawable(R.drawable.ic_audio_start));
    }

    //文件选择
    @OnClick(R2.id.audio_project)
    public void onStartProject(View view) {
        stopAudioPlayer();
        Intent intent = new Intent("com.provider.ACTION_PROVIDER");
        intent.putExtra(ProviderActivity.TYPE_KEY, ContentProviderUtils.TYPE_AUDIO);
        startActivityForResult(intent, ACTIVITY_PROVIDER_CODE);
    }

    //保存
    @OnClick(R2.id.audio_send)
    public void onSave(View view) {
        stopAudioPlayer();
        IntentBean.getInstance().setChecks(audioList);
        setResult(ACTIVITY_PROVIDER_CODE);
        finish();
    }

    @OnClick(R2.id.audio_back)
    public void onBack(View view) {
        stopAudioPlayer();
        finish();
    }

    @OnClick(R2.id.audio_delete)
    public void onDelete(View view) {
        stopAudioPlayer();
        mFileView.setText("文件名");
        isRecordFile = true;
        isPlay = false;
        updateAudioView();
        FileUtils.deleteFile(audioList.get(0).getPath());
        mFileName = String.valueOf(System.currentTimeMillis());
        audioList.clear();
        if (curState == AUDIO_STATE_RESULT_STOP || curState == AUDIO_STATE_RESULT_PLAY ||
                curState == AUDIO_STATE_RESULT_PAUSE) {

        }
        waveSfv.cleatDrawWave();
        audioRecordUtil.clear();
    }

    private void stopAudioPlayer() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            mPlayer.release();
            mPlayer = null;
            mSoundFile = null;
        }
    }

    @OnClick(R2.id.audio_play_state)
    public void onPlayClick(View view) {
        switch (curState) {
            case AUDIO_STATE_PLAY_INITIAL:
                curState = AUDIO_STATE_PLAY_START;
                onPlay(0);//播放 从头开始播放
                mPlayView.setImageDrawable(getDrawable(R.drawable.ic_audio_close));
                break;
            case AUDIO_STATE_PLAY_START:
                curState = AUDIO_STATE_PLAY_STOP;
                onAudioPause();
                mPlayView.setImageDrawable(getDrawable(R.drawable.ic_audio_close));
                break;
            case AUDIO_STATE_PLAY_STOP:
                curState = AUDIO_STATE_PLAY_START;
                onAudioPause();
                mPlayView.setImageDrawable(getDrawable(R.drawable.ic_audio_play));
                break;
        }
    }

    @OnClick(R2.id.audio_record_state)
    public void onRecordClick(View view) {
        switch (curState) {
            case AUDIO_STATE_RESULT_INITIAL:
                curState = AUDIO_STATE_RESULT_START;
                mRecordView.setImageDrawable(getDrawable(R.drawable.ic_audio_close));
                initAudio();
                startAudio();
                startTime = System.currentTimeMillis();
                mFileView.setText("文件名：" + mFileName + ".mav");
                break;
            case AUDIO_STATE_RESULT_START:
                curState = AUDIO_STATE_RESULT_STOP;
                mRecordView.setImageDrawable(getDrawable(R.drawable.ic_audio_play));
                audioRecordUtil.Stop();
                audioList.add(EditBean.newInstance().setType(EditBean.TYPE_AUDIO)
                        .setPath(FileUtils.getAudioParentFilePath() + mFileName + ".wav")
                        .setProviderName(mFileName)
                        .setTime(String.valueOf(System.currentTimeMillis() - startTime)));
                showLoadingDialog();
                break;
            case AUDIO_STATE_RESULT_STOP:
                if (FileUtils.isFile(audioList.get(0).getPath()) && isPlay) {
                    onPlay(0);//播放 从头开始播放
                    curState = AUDIO_STATE_RESULT_PLAY;
                    mRecordView.setImageDrawable(getDrawable(R.drawable.ic_audio_close));
                }
                break;
            case AUDIO_STATE_RESULT_PLAY:
                mPlayer.pause();
                curState = AUDIO_STATE_RESULT_PAUSE;
                mRecordView.setImageDrawable(getDrawable(R.drawable.ic_audio_close));
                break;

            case AUDIO_STATE_RESULT_PAUSE:
                onAudioPause();
                curState = AUDIO_STATE_RESULT_PLAY;
                mRecordView.setImageDrawable(getDrawable(R.drawable.ic_audio_play));
                break;
        }
    }

    private void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog();
            loadingDialog.show(getSupportFragmentManager(), "LoadingDialog");
        }
    }

    /**
     * 开始录音
     */
    private void startAudio() {
        audioRecordUtil = new AudioRecordUtil();
        audioRecordUtil.baseLine = waveSfv.getHeight() / 2;
        audioRecordUtil.setMsgHandler(mHandler);
        audioRecordUtil.Start(audioRecord, recBufSize, waveSfv, mFileName,
                FileUtils.getAudioParentFilePath(), new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        return true;
                    }
                });
    }

    private void initWaveView(String fileUrl) {
        loadFromFile(fileUrl);
    }

    /**
     * 载入wav文件显示波形
     */
    private void loadFromFile(String fileUrl) {
        final File mFile = new File(fileUrl);
        mLoadingKeepGoing = true;
        // Load the sound file in a background thread
        mLoadSoundFileThread = new Thread() {
            @Override
            public void run() {
                try {
                    mSoundFile = SoundFile.create(mFile.getPath(), null);
                    if (mSoundFile == null) {
                        return;
                    }
                    if (mPlayer != null) {
                        mPlayer.stop();
                        mPlayer = null;
                    }
                    mPlayer = new SamplePlayer(mSoundFile, mHandler);
                } catch (final Exception e) {
                    e.printStackTrace();
                    return;
                }
                if (mLoadingKeepGoing) {
                    Runnable runnable = new Runnable() {
                        public void run() {
                            finishOpeningSoundFile();
                            waveSfv.setVisibility(View.INVISIBLE);
                            waveView.setVisibility(View.VISIBLE);
                            if (isRecordFile) {
                                isPlay = true;
                            } else {

                            }
                            if (loadingDialog != null) {
                                loadingDialog.dismiss();
                                loadingDialog = null;
                            }
                        }
                    };
                    runOnUiThread(runnable);
                }
            }
        };
        mLoadSoundFileThread.start();
    }

    /**
     * waveview载入波形完成
     */
    private void finishOpeningSoundFile() {
        waveView.setSoundFile(mSoundFile);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float mDensity = metrics.density;
        waveView.recomputeHeights(mDensity);
    }

    private int mPlayStartMsec;
    private int mPlayEndMsec;
    private final int UPDATE_WAV = 100;

    private synchronized void onPlay(int startPosition) {
        if (mPlayer == null)
            return;
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            updateTime.removeMessages(UPDATE_WAV);
        }
        mPlayer.setOnCompletionListener(new SamplePlayer.OnCompletionListener() {
            @Override
            public void onCompletion() {
                waveView.setPlayback(-1);
                updateDisplay();
                updateTime.removeMessages(UPDATE_WAV);
                Toast.makeText(getApplicationContext(), "播放完成", Toast.LENGTH_LONG).show();
                if (isRecordFile) {
                    curState = AUDIO_STATE_RESULT_STOP;
                    mRecordView.setImageDrawable(getDrawable(R.drawable.ic_audio_play));
                } else {
                    curState = AUDIO_STATE_PLAY_INITIAL;
                    mPlayView.setImageDrawable(getDrawable(R.drawable.ic_audio_play));
                }
            }
        });
        mPlayStartMsec = waveView.pixelsToMillisecs(startPosition);
        mPlayEndMsec = waveView.pixelsToMillisecsTotal();
        mPlayer.play();
        mPlayer.seekTo(mPlayStartMsec);
        Message msg = new Message();
        msg.what = UPDATE_WAV;
        updateTime.sendMessage(msg);
    }

    private synchronized void onAudioPause() {
        if (mPlayer == null)
            return;
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            updateTime.removeMessages(UPDATE_WAV);
        }
        if (mPlayer != null && mPlayer.isPaused()) {
            if (isRecordFile) {
                curState = AUDIO_STATE_RESULT_PLAY;
            } else
                curState = AUDIO_STATE_PLAY_START;
            mPlayer.play();
            Message msg = new Message();
            msg.what = UPDATE_WAV;
            updateTime.sendMessage(msg);
        }

    }

    Handler updateTime = new Handler() {
        public void handleMessage(Message msg) {
            updateDisplay();
            updateTime.sendMessageDelayed(new Message(), 10);
        }
    };

    /**
     * 更新upd
     * ateview 中的播放进度
     */
    private void updateDisplay() {
        if (mPlayer != null) {
            int now = mPlayer.getCurrentPosition();// nullpointer
            int frames = waveView.millisecsToPixels(now);
            waveView.setPlayback(frames);//通过这个更新当前播放的位置
            if (now >= mPlayEndMsec) {
                waveView.setPlayFinish(1);
                if (mPlayer != null && mPlayer.isPlaying()) {
                    mPlayer.pause();
                    updateTime.removeMessages(UPDATE_WAV);
                }
            } else {
                waveView.setPlayFinish(0);
            }
        }
        waveView.invalidate();//刷新真个视图
    }

    @Override
    public void completed(final String path) {
        audioCodec.release();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                audioList.get(0).setPath(path);
                initWaveView(audioList.get(0).getPath());
            }
        });
    }
}
