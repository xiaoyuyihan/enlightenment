package enlightenment.com.tool.okhttp.builder;

import enlightenment.com.tool.okhttp.OkHttpUtils;
import enlightenment.com.tool.okhttp.request.OtherRequest;
import enlightenment.com.tool.okhttp.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
