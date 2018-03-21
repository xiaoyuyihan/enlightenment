package enlightenment.com.tool.okhttp;

/**
 * @author ：程序员小冰
 * @新浪微博 ：http://weibo.com/mcxiaobing
 * @GitHub: https://github.com/QQ986945193
 * @CSDN博客: http://blog.csdn.net/qq_21376985
 */

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;


public abstract class OkHttpBaseCallback<T> {


    public Type mType;

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        //通过反射获取当前类表示的实体(类,接口,基本类型或void)的直接父类的Type,
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        //获得超类的泛型参数的实际类型
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }


    public OkHttpBaseCallback() {
        mType = getSuperclassTypeParameter(getClass());
    }


    public abstract void onBeforeRequest(Request request);


    public abstract void onFailure(Request request, Exception e);


    /**
     * 请求成功时调用此方法
     *
     * @param response
     */
    public abstract void onResponse(Response response);

    /**
     * 状态码大于200，小于300 时调用此方法
     *
     * @param response
     * @param t
     * @throws IOException
     */
    public abstract void onSuccess(Response response, T t);

    /**
     * 状态码400，404，403，500等时调用此方法
     *
     * @param response
     * @param code
     * @param e
     */
    public abstract void onError(Response response, int code, Exception e);

}