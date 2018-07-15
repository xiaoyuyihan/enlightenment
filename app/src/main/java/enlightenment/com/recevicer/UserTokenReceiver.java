package enlightenment.com.recevicer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import enlightenment.com.base.EnlightenmentApplication;
import enlightenment.com.contents.Constants;
import enlightenment.com.service.SerMessageService;

/**
 * Created by admin on 2018/5/19.
 */

public abstract class UserTokenReceiver extends BroadcastReceiver {
    public static final String RECEIVER_MSG_FLAG = "RECEIVER_MSG_FLAG";
    public static final String RECEIVER_MSG_DATA = "RECEIVER_MSG_DATA";
    public static final String APP_RECEIVER_LOGIN_ERROR = "enlightenment.app.receiver.login_error";
    public static final int APP_LOGIN_ERROR = 1;      //登陆失败，提示用户并回到登陆页面

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getIntExtra(RECEIVER_MSG_FLAG, 0)
                == APP_LOGIN_ERROR) {
            String msgData = intent.getStringExtra(RECEIVER_MSG_DATA);
            tryNextLogin(context, msgData);
            if (checkTokenTimeOut()) {

            }
        }
    }

    private boolean checkTokenTimeOut() {
        String tokenStr = EnlightenmentApplication.getInstance().getSharedPreferences()
                .getString(Constants.Set.SET_USER_TOKEN_TIME, null);
        String cycleStr = EnlightenmentApplication.getInstance().getSharedPreferences()
                .getString(Constants.Set.SET_USER_TOKEN_LONG, null);
        if (tokenStr == null || cycleStr == null || tokenStr.equals("") || cycleStr.equals("") || tokenStr.length() < 10) {
            return true;
        }
        try {
            tokenStr = tokenStr.substring(0, 10);
            long tokenTime = Long.valueOf(tokenStr);
            int cycle = Integer.valueOf(cycleStr);
            long currentTime = System.currentTimeMillis() / 1000;
            long curCycle = currentTime - tokenTime - 10;
            if (curCycle > cycle) {
                return true;
            } else
                return false;
        } catch (Exception e) {
            return true;
        }
    }

    protected void tryNextLogin(Context context, String msg) {
        Intent intent = new Intent(context, SerMessageService.class);
        String phone = EnlightenmentApplication.getInstance().getSharedPreferences()
                .getString(Constants.Set.SET_USER_NAME, null);
        String password = EnlightenmentApplication.getInstance().getSharedPreferences()
                .getString(Constants.Set.SET_USER_PASSWORD, null);
        if (phone != null && password != null && !phone.equals("") && !password.equals("")) {
            intent.putExtra(SerMessageService.SERVICE_DATA_EXTRA, SerMessageService.ACTION_DETECT_REQUEST_TOKEN);
            intent.putExtra(SerMessageService.SERVICE_REQUEST_TOKEN_PHONE, phone);
            intent.putExtra(SerMessageService.SERVICE_REQUEST_TOKEN_PASSWORD, password);
            context.startService(intent);
        } else {
            onLoginErrorCallback(msg);
        }
    }

    public abstract void onLoginErrorCallback(String msg);
}
