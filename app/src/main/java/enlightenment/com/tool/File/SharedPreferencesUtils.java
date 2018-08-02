package enlightenment.com.tool.File;

import android.content.Context;

import enlightenment.com.contents.Constants;

/**
 * Created by admin on 2018/8/1.
 */

public class SharedPreferencesUtils {
    public static String getPreferences(Context context,String key){
        return context.getSharedPreferences(Constants.Set.SET, Context.MODE_PRIVATE)
                .getString(key, null);
    }
}
