package enlightenment.com.information;

import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import enlightenment.com.contents.HttpUrls;
import enlightenment.com.module.ModuleBean;
import enlightenment.com.mvp.BasePresenter;
import enlightenment.com.mvp.BaseView;
import enlightenment.com.operationBean.ColumnBean;
import enlightenment.com.tool.gson.GsonUtils;
import enlightenment.com.tool.okhttp.ModelUtil;

/**
 * Created by lw on 2018/2/27.
 */

public class InformationPresenter<T extends InformationView> extends BasePresenter {

    public static final int INFORMATION_TYPE_MAJOR=1;
    public static final int INFORMATION_TYPE_ORIENT=2;

    private static InformationPresenter informationPresenter;
    private T mView;
    private List<ColumnBean> columnBeanList;
    private List<ModuleBean> moduleBeanList;

    private Handler mHandler= new Handler();


    public static InformationPresenter getInstance() {
        if (informationPresenter == null) {
            informationPresenter = new InformationPresenter();
        }
        return informationPresenter;
    }

    public InformationPresenter() {
        super();
    }

    @Override
    public void BindView(BaseView view) {
        super.BindView(view);
        mView = (T) view;
    }

    @Override
    public void unBindView(BaseView baseView) {
        super.unBindView(baseView);
        if (mView.equals(baseView))
            mView = null;
        informationPresenter = null;
    }

    public void loadColumn() {
        if (columnBeanList == null)
            columnBeanList = new ArrayList<>();
        mModel.post(HttpUrls.HTTP_UTL_GET_USER_COLUMN, getTokenMap(), new ModelUtil.CallBack() {
            @Override
            public void onResponse(String response, int id) {
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        columnBeanList = GsonUtils.parseJsonArrayWithGson(
                                jsonArray.toString(), ColumnBean[].class);
                        Collections.sort(columnBeanList);
                        showColumnCheck();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void loadModel(int type) {
        if (moduleBeanList == null)
            moduleBeanList = new ArrayList<>();
        mModel.post(
                type==INFORMATION_TYPE_MAJOR?
                HttpUrls.Http_URL_DETECT_MAJOR:HttpUrls.Http_URL_DETECT_ORIENT,
                null,
                new ModelUtil.CallBack() {
            @Override
            public void onResponse(String response, int id) {
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        moduleBeanList = GsonUtils.parseJsonArrayWithGson(
                                jsonArray.toString(), ModuleBean[].class);
                        showModelDialog();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void showColumnCheck(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mView.showColumnCheck();
            }
        });
    }

    private void showModelDialog() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mView.showModelDialog();
            }
        });
    }

    public ArrayList<ModuleBean> getModuleBeanList() {
        return new ArrayList(moduleBeanList);
    }

    public ArrayList<ColumnBean> getColumnBeanList() {
        return new ArrayList(columnBeanList);
    }
}
