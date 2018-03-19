package enlightenment.com.main;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import enlightenment.com.base.EnlightenmentApplication;
import enlightenment.com.contents.Constants;
import enlightenment.com.contents.HttpUrls;
import enlightenment.com.tool.okhttp.ModelUtil;
import enlightenment.com.mvp.BasePresenter;
import enlightenment.com.mvp.BaseView;
import enlightenment.com.tool.gson.GsonUtils;
import okhttp3.Call;

/**
 * Created by lw on 2017/9/4.
 */

public class MainPresenter<T extends MainView> extends BasePresenter {
    private static MainPresenter basePresenter;
    private ModelUtil mModel;
    private T mView;

    private ArrayList HomeNewList = new ArrayList();
    private ArrayList HomeHotList = new ArrayList();
    private ArrayList HomeLoveList = new ArrayList();

    private ArrayList FoundKnowledgeList = new ArrayList();
    private ArrayList FoundDiyList = new ArrayList();
    private ArrayList FoundHelpList = new ArrayList();
    private ArrayList FoundMyselfList = new ArrayList();
    private Handler mHandler = new Handler();

    private int startNumber = 1;
    private int number = 25;

    public static MainPresenter getInstance() {
        if (basePresenter == null) {
            basePresenter = new MainPresenter();
        }
        return basePresenter;
    }

    @Override
    public void BindView(BaseView view) {
        super.BindView(view);
        mView = (T) view;
    }

    @Override
    public void unBindView(BaseView baseView) {
        super.unBindView(baseView);
        mView = null;
    }

    public void upRefresh(Fragment fragment, SwipeRefreshLayout swipeRefreshLayout, RecyclerView.Adapter baseAdapter) {

        if (fragment instanceof HomeDynamicFragment) {
            Refresh(true, swipeRefreshLayout, ((HomeDynamicFragment) fragment).getTypeFragment(), baseAdapter);
        } else
            Refresh(true, swipeRefreshLayout, ((FoundDynamicFragment) fragment).getTypeFragment(), baseAdapter);
    }

    public void downRefresh(Fragment fragment, RecyclerView.Adapter baseAdapter) {
        if (fragment instanceof HomeDynamicFragment) {
            Refresh(false, null, ((HomeDynamicFragment) fragment).getTypeFragment(), baseAdapter);
        } else
            Refresh(false, null, ((FoundDynamicFragment) fragment).getTypeFragment(), baseAdapter);

    }

    private void Refresh(final boolean Flag, final SwipeRefreshLayout swipeRefreshLayout, final int type, final RecyclerView.Adapter baseAdapter) {
        ModelUtil.getInstance().get(getUrl(Flag, type),
                new ModelUtil.CallBack() {
                    @Override
                    public void onResponse(String result, int id) {
                        if (result != null)
                            try {
                                JSONArray jsonArray = new JSONObject(result).getJSONArray("data");
                                final List list = GsonUtils.parseJsonArrayWithGson(jsonArray.toString(), ContentBean[].class);
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        addDataList(list, type, Flag,baseAdapter);
                                        baseAdapter.notifyDataSetChanged();
                                        if (swipeRefreshLayout != null)
                                            swipeRefreshLayout.setRefreshing(false);
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                    }

                    @Override
                    public void onException(Call call, Exception e, int id) {
                        if (swipeRefreshLayout != null)
                            swipeRefreshLayout.setRefreshing(false);
                        super.onException(call, e, id);
                    }
                });
    }

    private String getUrl(boolean flag, int type) {
        String url = null;
        switch (type) {
            case HomeDynamicFragment.FRAGMENT_NEW:
                if (flag)
                    startNumber = 1;
                else
                    startNumber = HomeNewList.size() + 1;
                url = HttpUrls.HTTP_URL_HOME_NEW + "?page=" + startNumber;
                break;
            case HomeDynamicFragment.FRAGMENT_HOT:
                if (flag)
                    startNumber = 1;
                else
                    startNumber = HomeHotList.size() + 1;
                url = HttpUrls.HTTP_URL_HOME_HOT + "?page=" + startNumber;
                break;
            case HomeDynamicFragment.FRAGMENT_LIVE:
                if (flag)
                    startNumber = 1;
                else
                    startNumber = HomeLoveList.size() + 1;
                url = HttpUrls.HTTP_URL_HOME_LOVE + "?page=" + startNumber;
                break;
            case FoundDynamicFragment.FRAGMENT_KONWLEDGE:
                startNumber = FoundKnowledgeList.size() + 1;
                url = HttpUrls.HTTP_URL_FOUND_KONWLEDGE + "?page=" + startNumber;
                break;
            case FoundDynamicFragment.FRAGMENT_DIY:
                if (flag)
                    startNumber = 1;
                else
                    startNumber = FoundDiyList.size() + 1;
                url = HttpUrls.HTTP_URL_FOUND_DIY + "?page=" + startNumber;
                break;
            case FoundDynamicFragment.FRAGMENT_HELP:
                if (flag)
                    startNumber = 1;
                else
                    startNumber = FoundHelpList.size() + 1;
                url = HttpUrls.HTTP_URL_FOUND_HELP + "?page=" + startNumber;
                break;
            case FoundDynamicFragment.FRAGMENT_MYSELF:
                if (flag)
                    startNumber = 1;
                else
                    startNumber = FoundMyselfList.size() + 1;
                url = HttpUrls.HTTP_URL_FOUND_MYSELF + "?page=" + startNumber;
                break;
        }
        return url+"&token="+ EnlightenmentApplication.getInstance().getString(Constants.Set.SET_USER_TOKEN);
    }

    //记住item的ID
    private void addDataList(List data, int type, boolean Flag, RecyclerView.Adapter baseAdapter) {
        switch (type) {
            case HomeDynamicFragment.FRAGMENT_NEW:
                if (Flag) {
                    HomeNewList.clear();
                    HomeNewList.addAll(data);
                } else
                    HomeNewList.addAll(data);
                break;
            case HomeDynamicFragment.FRAGMENT_HOT:
                if (Flag) {
                    HomeHotList.clear();
                    HomeHotList.addAll(data);
                } else
                    HomeHotList.addAll(data);
                break;
            case HomeDynamicFragment.FRAGMENT_LIVE:
                if (Flag) {
                    HomeLoveList.clear();
                    HomeLoveList.addAll(data);
                } else
                    HomeLoveList.addAll(data);
                break;
            case FoundDynamicFragment.FRAGMENT_KONWLEDGE:
                if (Flag) {
                    FoundKnowledgeList.clear();
                    FoundKnowledgeList.addAll(data);
                } else
                    FoundKnowledgeList.addAll(data);
                break;
            case FoundDynamicFragment.FRAGMENT_DIY:
                if (Flag) {
                    FoundDiyList.clear();
                    FoundDiyList.addAll(data);
                } else
                    FoundDiyList.addAll(data);
                break;
            case FoundDynamicFragment.FRAGMENT_HELP:
                if (Flag) {
                    FoundHelpList.clear();
                    FoundHelpList.addAll(data);
                } else
                    FoundHelpList.addAll(data);
                break;
            case FoundDynamicFragment.FRAGMENT_MYSELF:
                if (Flag) {
                    FoundMyselfList.clear();
                    FoundMyselfList.addAll(data);
                } else
                    FoundMyselfList.addAll(data);
                break;
        }
    }

    public ArrayList getDataList(int type) {
        switch (type) {
            case HomeDynamicFragment.FRAGMENT_NEW:
                return HomeNewList;
            case HomeDynamicFragment.FRAGMENT_HOT:
                return HomeHotList;
            case HomeDynamicFragment.FRAGMENT_LIVE:
                return HomeLoveList;

            case FoundDynamicFragment.FRAGMENT_KONWLEDGE:
                return new ArrayList();
            case FoundDynamicFragment.FRAGMENT_DIY:
                return FoundDiyList;
            case FoundDynamicFragment.FRAGMENT_HELP:
                return FoundHelpList;
            case FoundDynamicFragment.FRAGMENT_MYSELF:
                return FoundMyselfList;
        }
        return new ArrayList();
    }
}
