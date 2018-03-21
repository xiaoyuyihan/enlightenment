package enlightenment.com.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by lw on 2017/9/18.
 */

public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            Toast.makeText(context, "当前无网络，请检查设备的网络连接", Toast.LENGTH_SHORT)
                    .show();
        }else {
            if (!activeNetworkInfo.isAvailable() || !activeNetworkInfo.isConnected()) {
                Toast.makeText(context, "当前网络不可用", Toast.LENGTH_SHORT).show();
            } else {
                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    Toast.makeText(context, "已连接上移动数据", Toast.LENGTH_SHORT)
                            .show();
                } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                    Toast.makeText(context, "已连接上WIFI数据", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }
    }
}
