package enlightenment.com.tool.Imagecompression;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.HandlerThread;
import android.util.ArrayMap;

import enlightenment.com.tool.File.FileUtils;


/**
 * Created by admin on 2018/7/19.
 * 图片压缩基本思路
 * 1。获取图片分辨率，进行分辨率级别压缩
 * 2。进行尺寸级别压缩
 * 3。损失像素级别压缩
 */

public class ImageCompression {

    public static final int COMPRESSION_TYPE_HDPI = 1;
    public static final int COMPRESSION_TYPE_XHDPI = 2;
    public static final int COMPRESSION_TYPE_XXHDPI = 3;

    private static final int HDPI_width = 480;
    private static final int HDPI_height = 800;

    private static final int XHDPI_width = 720;
    private static final int XHDPI_height = 1280;

    private static final int XXHDPI_width = 1080;
    private static final int XXHDPI_height = 1920;

    private int curWidth = XHDPI_width;
    private int curHeight = XHDPI_height;
    private int quality = 50;


    private static ImageCompression compression;

    private HandlerThread mThread;
    private ArrayMap<String, OnCompressionListener> mComData;

    public static ImageCompression getInstance() {
        if (compression == null)
            compression = new ImageCompression();
        return compression;
    }

    public ImageCompression() {
        mComData = new ArrayMap<>();
    }

    private Bitmap onScaledBitmap(Bitmap bitmap) {
        int height = bitmap.getHeight();//4026
        int width = bitmap.getWidth();
        int scaled = 0;
        if (height > width) {
            //竖直图片
            if (width > curWidth)
                scaled = width / curWidth;
            else
                return bitmap;
        } else {
            if (height > curWidth)
                scaled = height / curWidth;
            else
                return bitmap;
        }
        return Bitmap.createScaledBitmap(bitmap, width / scaled, height / scaled, true);
    }

    private boolean checkUrl(String url) {
        return com.utils.FileUtils.isFile(url);
    }

    public ImageCompression addCompressionUrl(String url, OnCompressionListener onCompressionListener) {
        mComData.put(url, onCompressionListener);
        return this;
    }

    public ImageCompression setCompressionTypeDP(int compressionType) {
        switch (compressionType) {
            case COMPRESSION_TYPE_HDPI:
                curHeight = HDPI_height;
                curWidth = HDPI_width;
                quality = 40;
                break;
            case COMPRESSION_TYPE_XHDPI:
                curHeight = XHDPI_height;
                curWidth = XHDPI_width;
                quality = 50;
                break;
            case COMPRESSION_TYPE_XXHDPI:
                curHeight = XXHDPI_height;
                curWidth = XXHDPI_width;
                quality = 60;
                break;
        }

        return this;
    }

    public ImageCompression setQuality(int quality) {
        this.quality = quality;
        return this;
    }

    public void start() {
        if (!mThread.isAlive()) {
            mThread = new HandlerThread("imageCompressionThread") {
                @Override
                public void run() {
                    compressionImageThread();
                }
            };
            mThread.start();
        }
    }

    public interface OnCompressionListener {
        void onCompression(String oldUrl, String url);
    }

    private void compressionImageThread() {
        while (mComData != null && mComData.size() > 0) {
            String url = mComData.keyAt(0);
            OnCompressionListener compressionListener = mComData.get(url);
            compressionImage(url, compressionListener);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mComData.remove(url, compressionListener);
            } else {
                mComData.remove(url);
            }
        }
    }

    public void compressionImage(String url, OnCompressionListener compressionListener) {
        if (checkUrl(url)) {
            Bitmap bitmap = FileUtils.readFileBitmap(url);
            if (bitmap == null)
                if (compressionListener != null)
                    compressionListener.onCompression(url, null);
            bitmap = onScaledBitmap(bitmap);
            String curUrl = com.utils.FileUtils.getPhotoFilePath();
            FileUtils.writeFileBitmap(curUrl, bitmap, quality);
            if (compressionListener != null)
                compressionListener.onCompression(url, curUrl);
            bitmap.recycle();
            bitmap = null;
        } else {
            if (compressionListener != null)
                compressionListener.onCompression(url, url);
        }
    }
}
