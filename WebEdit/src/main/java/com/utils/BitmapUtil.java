package com.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by lw on 2018/2/5.
 */

public class BitmapUtil {
    /**
     * 获取照片宽高比
     * @param path
     * @return
     */
    public static float getImageSizeAhead(String path) {
        if (path!=null){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            return (float) options.outWidth/(float) options.outHeight;
        }else
            return (float) 4/(float) 3;
    }
}
