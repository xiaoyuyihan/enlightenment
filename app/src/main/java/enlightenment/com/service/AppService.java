package enlightenment.com.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.NotificationCompat;

import enlightenment.com.base.R;

/**
 * Created by admin on 2018/7/21.
 */

public abstract class AppService extends Service {

    public static int NOTIFICATION_ACTION_SUBJECT = 1001;
    public static int NOTIFICATION_ACTION_DOWN=1002;

    public Handler mMainHandler = new Handler();
    public HandlerThread mServiceThread = new HandlerThread("UploadServiceThread");
    public Handler mThreadHandler;
    NotificationManager mNotifyManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mServiceThread.start();
        mThreadHandler = new Handler(mServiceThread.getLooper());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mServiceThread.quit();
    }


    public void showNotification(int smallIcon, String contentTitle, String content, int ID) {
        //NotificationManager 是通知管理类，它是一个系统服务。调用 NotificationManager 的 notify() 方法可以向系统发送通知。
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //实例化NotificationCompat.Builde并设置相关属性
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"service")
                //设置小图标
                .setSmallIcon(smallIcon)
                //设置通知标题
                .setContentTitle(contentTitle)
                //设置通知内容
                .setContentText(content)
                .setOngoing(true)
                .setProgress(0, 0, true);
        Notification notification = builder.build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        //通过builder.build()方法生成Notification对象,并发送通知,id=1
        startForeground(ID, notification);// 开始前台服务
    }

    public void deleteNotification(int ID){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(ID);
        }
        stopForeground(true);
    }

}
