package enlightenment.com.tool.okhttp.callback;

import android.content.Intent;

import java.io.IOException;

import enlightenment.com.base.EnlightenmentApplication;
import enlightenment.com.recevicer.UserTokenReceiver;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by zhy on 15/12/14.
 */
public abstract class StringCallback extends Callback<String> {
    @Override
    public String parseNetworkResponse(Response response, int id) throws IOException {
        return response.body().string();
    }

    @Override
    public void onError(Call call, Exception e, int id, int code) {
        if (code == 401) {
            Intent intent = new Intent(UserTokenReceiver.APP_RECEIVER_LOGIN_ERROR);
            intent.putExtra(UserTokenReceiver.RECEIVER_MSG_FLAG, UserTokenReceiver.APP_TOKEN_ERROR);
            intent.putExtra(UserTokenReceiver.RECEIVER_MSG_DATA, "正在检修与服务器的通信");
            EnlightenmentApplication.getInstance().sendBroadcast(intent);
        }
    }
}
