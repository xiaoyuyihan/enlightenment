package enlightenment.com.mvp;

import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;

import java.util.Map;

import enlightenment.com.base.EnlightenmentApplication;
import enlightenment.com.contents.Constants;
import enlightenment.com.details.DetailsView;
import enlightenment.com.tool.okhttp.ModelUtil;

/**
 * Created by lw on 2017/7/21.
 */

public abstract class BasePresenter<T extends BaseView> {
    public ModelUtil mModel;
    public T mView;

    /**
     * 绑定View
     *
     * @param view
     */
    public void BindView(T view) {
        mView = view;
    }

    /**
     * 解除引用
     */
    public void unBindView(BaseView baseView) {
        if (mView !=null && mView.equals(baseView)) {
            mView = null;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
    }

    public void onStart() {
        mModel = ModelUtil.getInstance();
    }

    public void onRestart() {
    }

    public void onResume() {
    }

    public void onPause() {
    }

    public void onStop() {
        mModel = null;
    }

    public void onDestroy() {

    }

    public Map getTokenMap(){
        Map<String ,String > map=new ArrayMap<>();
        map.put("token",
                EnlightenmentApplication.getInstance().getString(Constants.Set.SET_USER_TOKEN));
        return map;
    }
}
