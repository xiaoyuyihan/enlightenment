package enlightenment.com.tool.device;

import android.content.Context;
import android.media.MediaCodecInfo;
import android.net.ConnectivityManager;

import enlightenment.com.base.EnlightenmentApplication;

/**
 * Created by lw on 2017/8/3.
 */

public class SystemState {
    public static boolean isNetworkState(){
        ConnectivityManager connManager = (ConnectivityManager) EnlightenmentApplication.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }

        return false;
    }
}
