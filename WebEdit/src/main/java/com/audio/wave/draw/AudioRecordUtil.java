package com.audio.wave.draw;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import android.media.AudioRecord;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;

import com.audio.wave.utils.AudioConversionUtil;
import com.audio.wave.view.WavaTimeView;

/**
 * 录音和写入文件使用了两个不同的线程，以免造成卡机现象
 * 录音波形绘制
 *
 * @author cokus
 */
public class AudioRecordUtil {

    private ArrayList<Short> inBuf = new ArrayList<Short>();//缓冲区数据
    private ArrayList<byte[]> write_data = new ArrayList<byte[]>();//写入文件数据
    public boolean isRecording = false;// 录音线程控制标记
    private boolean isWriting = false;// 录音线程控制标记
    private boolean isUIThread = true;

    public int rateX = 100;//控制多少帧取一帧
    public int baseLine = 0;// Y轴基线
    int recBufSize;
    private int marginRight = 30;//波形图绘制距离右边的距离
    private int draw_time = 1000 / 200;//两次绘图间隔的时间
    private float divider = 0.2f;//为了节约绘画时间，每0.2个像素画一个数据
    long c_time;
    private String savePcmPath;//保存pcm文件路径
    private String saveWavPath;//保存wav文件路径

    private int readSize;

    private Handler mMsgHandler;

    public void setMsgHandler(Handler mMsgHandler) {
        this.mMsgHandler = mMsgHandler;
    }

    /**
     * 开始录音
     *
     * @param audioRecord
     * @param recBufSize
     * @param sfv
     * @param audioName
     */
    public void Start(AudioRecord audioRecord, int recBufSize, WavaTimeView sfv
            , String audioName, String path, Callback callback) {
        isRecording = true;
        isWriting = true;
        this.recBufSize = recBufSize;
        savePcmPath = path + audioName + ".pcm";
        saveWavPath = path + audioName + ".wav";
        new Thread(new WriteRunnable()).start();//开线程写文件
        new RecordTask(audioRecord, recBufSize, sfv, callback).execute();
    }

    /**
     * 停止录音
     */
    public void Stop() {
        Log.e("test", "stop start");
        isRecording = false;
        isWriting = false;
        inBuf.clear();// 清除
    }

    /**
     * 清楚数据
     */
    public void clear() {
        if (inBuf != null) {
            inBuf.clear();// 清除
        }
    }


    /**
     * 异步录音程序
     *
     * @author cokus
     */
    class RecordTask extends AsyncTask<Object, Object, Object> {
        private int recBufSize;
        private AudioRecord audioRecord;
        private WavaTimeView sfv;// 画板
        private Callback callback;
        private boolean isStart = false;


        public RecordTask(AudioRecord audioRecord, int recBufSize,
                          WavaTimeView sfv, Callback callback) {
            this.audioRecord = audioRecord;
            this.recBufSize = recBufSize;
            this.sfv = sfv;
            this.callback = callback;
            inBuf.clear();// 清除  
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                short[] buffer = new short[recBufSize];
                audioRecord.startRecording();// 开始录制
                while (isRecording) {
                    // 从MIC保存数据到缓冲区  
                    readSize = audioRecord.read(buffer, 0,
                            recBufSize);
                    synchronized (inBuf) {
                        for (int i = 0; i < readSize; i += rateX) {
                            inBuf.add(buffer[i]);
                        }
                    }
                    publishProgress();
                    if (AudioRecord.ERROR_INVALID_OPERATION != readSize) {
                        synchronized (write_data) {
                            byte bys[] = new byte[readSize * 2];
                            //因为arm字节序问题，所以需要高低位交换
                            for (int i = 0; i < readSize; i++) {
                                byte ss[] = getBytes(buffer[i]);
                                bys[i * 2] = ss[0];
                                bys[i * 2 + 1] = ss[1];
                            }
                            write_data.add(bys);
                        }
                    }
                }
                audioRecord.stop();
                audioRecord.release();
                audioRecord = null;
                isWriting = false;
            } catch (Throwable t) {
                Message msg = new Message();
                msg.arg1 = -2;
                msg.obj = t.getMessage();
                callback.handleMessage(msg);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            long time = new Date().getTime();
            float size = (sfv.getWidth() - marginRight) / divider;
            if (time - c_time >= draw_time) {
                ArrayList<Short> buf;
                synchronized (inBuf) {
                    if (inBuf.size() == 0)
                        return;
                    while (inBuf.size() > size) {
                        inBuf.remove(0);
                    }
                    buf = (ArrayList<Short>) inBuf.clone();// 保存
                    SimpleDraw(buf);// 把缓冲区数据画出来
                }
                c_time = new Date().getTime();
            }
            super.onProgressUpdate(values);
        }


        public byte[] getBytes(short s) {
            byte[] buf = new byte[2];
            for (int i = 0; i < buf.length; i++) {
                buf[i] = (byte) (s & 0x00ff);
                s >>= 8;
            }
            return buf;
        }

        /**
         * 绘制指定区域
         *
         * @param buf      缓冲区
         */
        void SimpleDraw(ArrayList<Short> buf) {
            if (!isRecording)
                return;

            for (int i = 0; i < buf.size(); i++) {
                byte bus[] = getBytes(buf.get(i));
                buf.set(i, (short) ((0x0000 | bus[1]) << 8 | bus[0]));//高低位交换
            }
            sfv.updateCanvas(buf);
        }
    }


    /**
     * 异步写文件
     *
     * @author cokus
     */
    class WriteRunnable implements Runnable {
        @Override
        public void run() {
            try {
                FileOutputStream fos2wav = null;
                File file2pcm = null;
                File file2wav = null;
                try {
                    file2pcm = new File(savePcmPath);
                    file2wav = new File(saveWavPath);

                    if (file2pcm.exists()) {
                        file2pcm.delete();
                    }
                    if (file2wav.exists()) {
                        file2wav.delete();
                    }
                    file2wav.createNewFile();
                    file2pcm.createNewFile();
                    fos2wav = new FileOutputStream(file2pcm);// 建立一个可存取字节的文件
                } catch (Exception e) {
                    e.printStackTrace();
                }
                while (isWriting || write_data.size() > 0) {
                    byte[] buffer = null;
                    synchronized (write_data) {
                        if (write_data.size() > 0) {
                            buffer = write_data.get(0);
                            write_data.remove(0);
                        }
                    }
                    try {
                        if (buffer != null) {
                            fos2wav.write(buffer);
                            Log.d("audio", buffer.toString());
                            fos2wav.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                fos2wav.flush();
                fos2wav.close();
                AudioConversionUtil p2w = new AudioConversionUtil();//将pcm格式转换成wav 其实就尼玛加了一个44字节的头信息
                p2w.decodePCMToWAVFiles(savePcmPath, saveWavPath);
                file2pcm.delete();
                if (isUIThread) {
                    if (mMsgHandler != null) {
                        synchronized (mMsgHandler) {
                            Message message = new Message();
                            message.what = 1;
                            mMsgHandler.sendMessage(message);
                        }
                    }
                }
            } catch (Throwable t) {

            }
        }
    }

}   
