package enlightenment.com.tool.gson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lw on 2017/8/31.
 */

public class GsonUtils {
    /***
     *
     * @param jsonData
     * @param type
     * @param <T>
     * @return  将Json数组解析成相应的映射对象列表
     */
    public static <T> List<T> parseJsonArrayWithGson(String jsonData,
                                                     Class<T[]> type) {
        Gson gson = new Gson();
        T[] result = gson.fromJson(jsonData,type);
        return Arrays.asList(result);
    }

}
