package enlightenment.com.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;

import java.util.ArrayList;
import java.util.List;

import enlightenment.com.contents.Constants;
import enlightenment.com.module.ModuleFatherBean;
import enlightenment.com.tool.okhttp.OkHttpUtils;

/**
 * Created by lw on 2017/7/21.
 */

public class EnlightenmentApplication extends Application {
    private static OkHttpUtils mHttpUtils;
    private static EnlightenmentApplication app;
    private List<ModuleFatherBean> majorBeen;
    private List<ModuleFatherBean> orientationBeen;
    private String modules="";
    private HandlerThread mHandlerThread;

    @Override
    public void onCreate() {
        super.onCreate();
        app=this;
        getHandlerThread();
        mHttpUtils = OkHttpUtils.getInstance();
    }

    public OkHttpUtils getHttpUtils() {
        if (mHttpUtils==null){
            mHttpUtils = OkHttpUtils.getInstance();
        }
        return mHttpUtils;
    }
    public static EnlightenmentApplication getInstance(){
        return app;
    }
    public SharedPreferences getSharedPreferences(){
        return getSharedPreferences(Constants.Set.SET, Context.MODE_PRIVATE);
    }

    public String getString(String key){
        return getSharedPreferences().getString(key,null);
    }
    public void setStringShared(String name,String value){
        SharedPreferences sharedPreferences=getSharedPreferences();
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(name,value);
        editor.commit();
    }

    public void isBooleanShared(String name,boolean value){
        SharedPreferences sharedPreferences=getSharedPreferences();
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(name,value);
        editor.commit();
    }

    public void setMajorBeen(List<ModuleFatherBean> majorBeen) {
        this.majorBeen = majorBeen;
    }

    public List<ModuleFatherBean> getMajorBeen() {
        return majorBeen;
    }

    public void setModules(String modules) {
        this.modules = modules;
    }

    public String getModules() {
        return modules;
    }

    public void setOrientationBeen(List<ModuleFatherBean> orientationBeen) {
        this.orientationBeen = orientationBeen;
    }

    public List<ModuleFatherBean> getOrientationBeen() {
        return orientationBeen;
    }

    public HandlerThread getHandlerThread() {
        if (mHandlerThread==null){
            mHandlerThread=new HandlerThread("handler_thread",01);
            mHandlerThread.start();
        }
        return mHandlerThread;
    }
}
