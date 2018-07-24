package com.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lw on 2017/12/24.
 */

public class FileUtils {

    public static String getFileParent() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/enlighten/";
    }

    public static String getVideoFileParentPath() {
        return getFileParent() + "video/";
    }

    public static String getVideoFilePath() {
        String path = getVideoFileParentPath() + System.currentTimeMillis() + ".mp4";
        filePathToFile(path);
        return path;
    }

    public static String getPhotoFileParentPath() {
        return getFileParent() + "image/";
    }

    public static String getPhotoFilePath() {
        return getPhotoFileParentPath() + System.currentTimeMillis() + ".jpg";
    }

    public static String getAudioFileParentPath() {
        return getFileParent() + "audio/";
    }

    public static String getAudioFilePath() {
        return getAudioFileParentPath()+ System.currentTimeMillis() + ".mp3";
    }

    public static String getAudioParentFilePath() {
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/data/audio/";
        File f = new File(filePath);
        if (!f.exists()) {
            f.mkdirs();
            return filePath;
        }
        return filePath;
    }

    public static String getAudioCurFilePath() {
        String file = getAudioParentFilePath() + System.currentTimeMillis() + ".wav";
        File f = new File(file);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return file;
        }
        return file;
    }

    public static boolean deleteFile(String path) {
        File file = new File(path);
        if (isFile(file)) {
            file.delete();
            return true;
        }
        return false;
    }

    public static boolean isFile(String path) {
        return isFile(new File(path));
    }

    public static boolean isCreateFile(String path) {
        File file = new File(path);
        if (!isFile(file)) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static boolean isFile(File path) {
        return path.exists();
    }

    public static File filePathToFile(String path) {
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static void insertProvider(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

}
