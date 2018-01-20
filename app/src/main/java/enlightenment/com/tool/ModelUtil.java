package enlightenment.com.tool;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import enlightenment.com.base.EnlightenmentApplication;
import enlightenment.com.mvp.BaseModel;
import enlightenment.com.tool.okhttp.OkHttpUtils;
import enlightenment.com.tool.okhttp.callback.FileCallBack;
import enlightenment.com.tool.okhttp.callback.StringCallback;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;

/**
 * Created by lw on 2017/7/21.
 */

public class ModelUtil implements BaseModel {
    private static ModelUtil model;
    private OkHttpUtils mHttpUtil;
    private Handler mHandler=new Handler(Looper.getMainLooper());

    public static ModelUtil getInstance() {
        if (model == null){
            model= new ModelUtil();
        }
        return model;
    }
    public ModelUtil(){
        mHttpUtil= EnlightenmentApplication.getInstance().getHttpUtils();
    }

    public void get(String url,CallBack m){
        get(url,null,m);
    }
    public void get(String url,Map params,final CallBack m){
        OkHttpUtils
                .get()
                .url(url)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(final Call call, final Exception e, final int id) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                m.onException(call,e,id);
                            }
                        });
                    }

                    @Override
                    public void onResponse(final String response, final int id) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                m.onResponse(response, id);
                            }
                        });
                    }
                });
    }

    public void post(String url,Map params,final CallBack m){
        OkHttpUtils
                .post()
                .url(url)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(final Call call, final Exception e, final int id) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                m.onException(call,e,id);
                            }
                        });
                    }

                    @Override
                    public void onResponse(final String response, final int id) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                m.onResponse(response, id);
                            }
                        });
                    }
                });
    }

    public<T> void postJSON(String url,T params, final CallBack m){
        OkHttpUtils
                .postString()
                .url(url)
                .content(new Gson().toJson(params))
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(final Call call, final Exception e, final int id) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                m.onException(call,e,id);
                            }
                        });
                    }

                    @Override
                    public void onResponse(final String response, final int id) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                m.onResponse(response, id);
                            }
                        });
                    }
                });
    }

    public void postFile(String url, File file,StringCallback m){
        OkHttpUtils
                .postFile()
                .url(url)
                .file(file)
                .build()
                .execute(m);
    }

    public void postForm(String url, Map<String,ArrayList<File>> file, Map params, final CallBack m){
        OkHttpUtils.post()//
                .files(file)
                .url(url)
                .params(params)
                .addHeader("Content-Type","application/x-jpg")
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(final Call call, final Exception e, final int id) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                m.onException(call,e,id);
                            }
                        });
                    }

                    @Override
                    public void onResponse(final String response, final int id) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                m.onResponse(response, id);
                            }
                        });
                    }
                });
    }

    public void postForm(String url, String key,File file, Map params, final CallBack m){
        OkHttpUtils.post()//
                .addFile(key,file)
                .url(url)
                .params(params)
                .addHeader("Content-Type","application/x-jpg")
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(final Call call, final Exception e, final int id) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                m.onException(call,e,id);
                            }
                        });
                    }

                    @Override
                    public void onResponse(final String response, final int id) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                m.onResponse(response, id);
                            }
                        });
                    }
                });
    }

    public void downFile(String url,String FileName,String File){
        OkHttpUtils//
                .get()//
                .url(url)//
                .build()//
                .execute(new FileCallBack(File,FileName) {

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(File response, int id) {

                    }
                });
    }
   public static abstract class CallBack{
        public void onException(Call call, final Exception e, int id){
            //保存错误
        }
        public abstract void onResponse(String response, int id);
    }
}
