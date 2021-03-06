package enlightenment.com.tool.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by lw on 2017/8/31.
 */

public class FileUtils {

    /**
     * @param filePath
     * @param timeFlag true 时间戳，false GMT
     * @return -1 File为null;
     */
    public static String getCreateFileTime(String filePath, boolean timeFlag) {
        File file = new File(filePath);
        if (!file.exists()) {
            return "-1";
        }
        if (timeFlag) {
            return new SimpleDateFormat("yyyy-MM-dd")
                    .format(new Date(file.lastModified()));
        } else {
            return String.valueOf(file.lastModified());
        }
    }

    public static<T> void writeFileObject(String fileName, T data) {
        if (isFileNews(fileName)) {
            File file = new File(fileName);
            try {
                ObjectOutputStream inputStream = new ObjectOutputStream(new FileOutputStream(file));
                inputStream.writeObject(data);
                inputStream.flush();
                inputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeByteToFile(String fileName ,Byte b){
        if (isFileNews(fileName)){
            File file =new File(fileName);
            try {
                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(b);
                outputStream.flush();
                outputStream.close();
            }catch (FileNotFoundException e){

            }catch (IOException e){

            }
        }
    }

    public static boolean writeFileBitmap(String fileName, Bitmap data,int quality){
        if (isFileNews(fileName)){
            File file = new File(fileName);
            try {
                FileOutputStream inputStream = new FileOutputStream(file);
                data.compress(Bitmap.CompressFormat.JPEG,quality,inputStream);
                inputStream.flush();
                inputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static Bitmap readFileBitmap(String url) {
        try {
            InputStream inputStream = new FileInputStream(url);
            return BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean writeContentHtmlFile(String fileName, String data){
        if (isFileNews(fileName)){
            File file = new File(fileName);
            try {
                FileOutputStream inputStream = new FileOutputStream(file);
                inputStream.write(data.getBytes());
                inputStream.flush();
                inputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static<T> List<T> readFileObject(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            try {
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
                List<T> list= (List<T>) inputStream.readObject();
                return list;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static boolean isFile(String fileName){
        return new File(fileName).exists();
    }

    /**
     * 文件是否存在新建
     *
     * @param fileName
     * @return
     */
    public static boolean isFileNews(String fileName) {
        File file = new File(fileName);
        String folderFile = fileName.substring(0, fileName.lastIndexOf("/"));
        File folder = new File(folderFile);
        boolean falg;
        if (file.exists()) {
            file.delete();
        }
        if (!folder.exists()) {
            falg=folder.mkdirs();
            if (falg)
                Log.d("dd","");
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean removeFile(String fileName) {
        if (isFileNews(fileName)) {
            File file = new File(fileName);
            file.delete();
        }
        return true;
    }
}
