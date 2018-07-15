package enlightenment.com.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatDelegate;

import java.util.List;

import enlightenment.com.contents.Constants;
import enlightenment.com.module.ModuleBean;
import enlightenment.com.tool.okhttp.OkHttpUtils;

/**
 * Created by lw on 2017/7/21.
 */

public class EnlightenmentApplication extends Application {
    private static OkHttpUtils mHttpUtils;
    private static EnlightenmentApplication app;
    private List<ModuleBean> majorBeen;
    private List<ModuleBean> orientationBeen;
    private String modules="";
    private HandlerThread mHandlerThread;

    @Override
    public void onCreate() {
        super.onCreate();
        app=this;
        getHandlerThread();
        isDefaultNightMode();
        mHttpUtils = OkHttpUtils.getInstance();
    }

    private void isDefaultNightMode() {
        if (!getSetSharedPreferences(Constants.Set.SET_NIGHT_MODE,false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    public void updateDefaultNightMode(){
        isDefaultNightMode();
    }

    public boolean getSetSharedPreferences(String key,boolean flag){
        return getSharedPreferences().getBoolean(key,flag);
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

    public void setMajorBeen(List<ModuleBean> majorBeen) {
        this.majorBeen = majorBeen;
    }

    public List<ModuleBean> getMajorBeen() {
        return majorBeen;
    }

    public void setModules(String modules) {
        this.modules = modules;
    }

    public String getModules() {
        return modules;
    }

    public void setOrientationBeen(List<ModuleBean> orientationBeen) {
        this.orientationBeen = orientationBeen;
    }

    public List<ModuleBean> getOrientationBeen() {
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
