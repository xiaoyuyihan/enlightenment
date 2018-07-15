package com.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

/**
 * Created by lw on 2018/1/6.
 */

public class MediaService extends Service {

    public static final String DATA_AUDIO_DURATION = "DATA_AUDIO_DURATION";
    public static final String ACTION_MEDIA_SERVICE = "com.edit.media.service.action";

    public static final int SERVICE_MEDIA_STATE_CREATE = 0;
    public static final int SERVICE_MEDIA_STATE_PLAYING = 1;
    public static final int SERVICE_MEDIA_STATE_PAUSE = 2;
    public static final int SERVICE_MEDIA_STATE_STOP = 3;

    private MediaBinder mMediaBinder = new MediaBinder();
    private MediaPlayer mMediaPlayer;
    private String mPath;
    private static final String Tag = MediaService.class.getName();
    private int mPlayState = SERVICE_MEDIA_STATE_CREATE;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMediaBinder;
    }

    @Override
    public void onCreate() {
        if (mMediaPlayer == null)
            mMediaPlayer = new MediaPlayer();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public void setMediaDataSource(String path) {
        Log.d(Tag, "set path " + path);
        if (mPath != null && mPath.equals(path)) {
            onPauseAudio();
            sendDurationBroad(mMediaPlayer.getDuration());
        } else {
            mPath = path;
            initMediaDataSource(mPath);
        }
    }

    private void initMediaDataSource(String path) {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mPlayState = SERVICE_MEDIA_STATE_CREATE;
        }
        if (mPlayState == SERVICE_MEDIA_STATE_PLAYING || mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mMediaPlayer.stop();
        } else if (mPlayState == SERVICE_MEDIA_STATE_CREATE
                && mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        } else if (mPlayState == SERVICE_MEDIA_STATE_PAUSE) {
            mMediaPlayer.stop();
        }
        try {
            mMediaPlayer.setDataSource(path);
            // 使用系统的媒体音量控制
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                AudioAttributes attributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setFlags(AudioAttributes.FLAG_LOW_LATENCY)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                        .build();
                mMediaPlayer.setAudioAttributes(attributes);
            }
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    sendDurationBroad(mp.getDuration());
                    mp.start();
                    mPlayState = SERVICE_MEDIA_STATE_PLAYING;
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.start();
                }
            });
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    mediaPlayer.start();
                    return false;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            mMediaPlayer = null;

        }
    }

    public void sendDurationBroad(int duration) {
        Intent intent = new Intent();
        intent.setAction(ACTION_MEDIA_SERVICE);
        intent.putExtra(DATA_AUDIO_DURATION, duration);
        sendBroadcast(intent);
    }

    public void onStopAudio() {
        if (mMediaPlayer != null &&
                mPlayState != SERVICE_MEDIA_STATE_STOP) {
            if (mPlayState == SERVICE_MEDIA_STATE_PLAYING)
                mMediaPlayer.pause();
            mMediaPlayer.stop();
            mPlayState = SERVICE_MEDIA_STATE_STOP;
        }
    }

    public void onCloseAudio() {
        if (mMediaPlayer != null &&
                mPlayState != SERVICE_MEDIA_STATE_CREATE) {
            if (mPlayState == SERVICE_MEDIA_STATE_PLAYING) {
                mMediaPlayer.pause();
                mMediaPlayer.stop();
            }
            if (mPlayState == SERVICE_MEDIA_STATE_PAUSE)
                mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mPlayState = SERVICE_MEDIA_STATE_CREATE;
        }
    }

    public void onReleaseAudio(){
        if (mMediaPlayer != null &&
                mPlayState != SERVICE_MEDIA_STATE_CREATE) {
            if (mPlayState == SERVICE_MEDIA_STATE_PLAYING) {
                mMediaPlayer.pause();
                mMediaPlayer.stop();
            }
            if (mPlayState == SERVICE_MEDIA_STATE_PAUSE)
                mMediaPlayer.stop();
            mPlayState = SERVICE_MEDIA_STATE_CREATE;
        }
    }

    public void onSeekTo(int position) {
        if (mMediaPlayer != null)
            mMediaPlayer.seekTo(position);
    }

    public void onPauseAudio() {
        if (mMediaPlayer != null)
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                mPlayState = SERVICE_MEDIA_STATE_PAUSE;
            } else {
                mMediaPlayer.start();
                mPlayState = SERVICE_MEDIA_STATE_PLAYING;
            }
    }

    public void onResetAudio() {
        if (mMediaPlayer != null)
            mMediaPlayer.reset();
    }

    public int getAudioState() {
        return mPlayState;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class MediaBinder extends Binder {
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            return super.onTransact(code, data, reply, flags);
        }

        public MediaService getService() {
            return MediaService.this;
        }
    }
}
