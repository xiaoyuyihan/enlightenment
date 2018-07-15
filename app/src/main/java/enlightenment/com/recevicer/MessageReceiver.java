package enlightenment.com.recevicer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by admin on 2018/5/19.
 */

public class MessageReceiver extends BroadcastReceiver {
    public static final String RECEIVER_MSG_FLAG = "RECEIVER_MSG_FLAG";
    public static final String RECEIVER_MSG_DATA = "RECEIVER_MSG_DATA";
    public static final String APP_RECEIVER_MESSAGE = "enlightenment.app.receiver.message";
    public static final int APP_MESSAGE_USER = 2;     //得到用户发送的消息
    public static final int APP_MESSAGE_SYSTEM_LIKE = 3;   //系统推送 喜欢
    public static final int APP_MESSAGE_SYSTEM_REWARD = 4;    //打赏
    public static final int APP_MESSAGE_SYSTEM_FOLLOW = 5;    //关注
    public static final int APP_MESSAGE_SYSTEM_SUBSCRIPTION = 6;  //订阅
    @Override
    public void onReceive(Context context, Intent intent) {

            final int flag = intent.getIntExtra(RECEIVER_MSG_FLAG,0);
            switch (flag){
                case APP_MESSAGE_USER:
                    break;
                case APP_MESSAGE_SYSTEM_LIKE:
                    break;
                case APP_MESSAGE_SYSTEM_REWARD:
                    break;
                case APP_MESSAGE_SYSTEM_FOLLOW:
                    break;
                case APP_MESSAGE_SYSTEM_SUBSCRIPTION:
                    break;

            }
    }
}
