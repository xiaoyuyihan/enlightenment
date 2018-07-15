package enlightenment.com.tool.gson;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

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

    public static <T> T parseJsonWithToBean(String json, Class<T> type){
        Gson gson = new Gson();
        T bean = gson.fromJson(json,type);
        return bean;
    }

    public static String parseBeanToJson(ArrayList list){
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    public static boolean isJSONVaild(String  json){
        try{
            Gson gson = new Gson();
            gson.fromJson(json,Object.class);
            return true;
        }catch (JsonSyntaxException e){
            return false;
        }
    }
}
