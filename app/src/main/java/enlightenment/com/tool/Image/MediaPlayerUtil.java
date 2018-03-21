package enlightenment.com.tool.Image;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by lw on 2018/3/12.
 */

public class MediaPlayerUtil {

    private static MediaPlayerUtil m;

    private int mPosition = 0;

    private MediaPlayer mediaPlayer;
    private ArrayList<String> mUrls = new ArrayList<>();
    private OnNextAudioListener onNextListener;

    private boolean isPrepare=false;

    public static MediaPlayerUtil getInstance() {
        if (m == null)
            m = new MediaPlayerUtil();
        return m;
    }

    public MediaPlayerUtil() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    public MediaPlayerUtil setUrls(ArrayList<String> mUrls) {
        this.mUrls = mUrls;
        isPrepare=false;
        return this;
    }

    public void onPlay() {
        play(mPosition);
    }

    public void setOnNextListener(OnNextAudioListener onNextListener) {
        this.onNextListener = onNextListener;
    }

    private void play(int position) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
        if (mUrls.size() <= 0)
            return;
        if (position >= mUrls.size()) {
            position = 0;
        }
        if (!isPrepare){
            // 通过异步的方式装载媒体资源
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(mUrls.get(position));
            } catch (IOException e) {
                e.printStackTrace();
                playNext();
            }
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // 装载完毕回调
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    playNext();
                    return false;
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    playNext();
                }
            });
            isPrepare=true;
        }else {
            mediaPlayer.start();
        }
    }

    public void playNext() {
        isPrepare=false;
        mPosition += 1;
        if (mPosition >= mUrls.size()) {
            mPosition = 0;
        } else
            play(mPosition);
        if (onNextListener!=null)
            onNextListener.onNext(mPosition);
    }

    public void onStop() {
        mediaPlayer.stop();
        mediaPlayer.release();
        isPrepare=false;
        mediaPlayer = null;
    }

    public void onPause(){
        mediaPlayer.pause();
    }

    public boolean isPlay(){
        return mediaPlayer.isPlaying();
    }

    public interface OnNextAudioListener{
        void onNext(int position);
    }
}
