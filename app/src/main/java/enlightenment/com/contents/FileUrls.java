package enlightenment.com.contents;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import enlightenment.com.base.EnlightenmentApplication;

/**
 * Created by lw on 2017/8/18.
 */

public class FileUrls {
    public static String public_path = ""; // 公共路径
    public static String private_path = ""; // 私有路径
    static {
        public_path = Environment.getExternalStorageDirectory().getPath()
                + "/jueta";
        private_path = EnlightenmentApplication.getInstance().
                getFilesDir().getAbsolutePath();
    }
    public static String PATH_CONTENTS = private_path+"/contents/";
    public static String PATH_PHOTO=public_path+"/photo/";
    public static String PATH_APP_MODULES = PATH_CONTENTS+"modules.txt";
}
