package enlightenment.com.main;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import enlightenment.com.base.EnlightenmentApplication;
import enlightenment.com.contents.Constants;
import enlightenment.com.contents.HttpUrls;
import enlightenment.com.main.found.FoundDynamicFragment;
import enlightenment.com.main.home.HomeDynamicFragment;
import enlightenment.com.main.home.HomeFragment;
import enlightenment.com.operationBean.ContentBean;
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
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private int startNumber = 1;
    private int number = 5;

    private boolean isRefresh = false;

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
        isRefresh=false;
    }

    @Override
    public void unBindView(BaseView baseView) {
        super.unBindView(baseView);
        mView = null;
        basePresenter = null;
    }

    /**
     * 向上滑动刷新
     *
     * @param type
     * @param baseAdapter
     */
    public void upRefresh(int type, RecyclerView.Adapter baseAdapter) {
        Refresh(false, null, type, baseAdapter);
    }

    /**
     * 向下滑动刷新
     *
     * @param type
     * @param swipeRefreshLayout
     * @param baseAdapter
     */
    public void downRefresh(int type, SwipeRefreshLayout swipeRefreshLayout, RecyclerView.Adapter baseAdapter) {
        Refresh(true, swipeRefreshLayout, type, baseAdapter);
    }

    private void Refresh(final boolean flag, final Object layout,
                         final int type, final RecyclerView.Adapter baseAdapter) {
        isRefresh=true;
        ModelUtil.getInstance().get(getUrl(flag, type),
                new ModelUtil.CallBack() {
                    @Override
                    public void onResponse(String result, int id) {
                        if (result != null)
                            try {
                                JSONArray jsonArray = new JSONObject(result).getJSONArray("data");
                                final List data = GsonUtils.parseJsonArrayWithGson(jsonArray.toString(), ContentBean[].class);
                                if (flag) {
                                    onDownDataRefresh(data, flag, (SwipeRefreshLayout) layout, type, baseAdapter);
                                } else {
                                    onUpDataRefresh(data, flag, type, baseAdapter);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                onRefreshError(flag, baseAdapter);
                            } catch (Exception e) {
                                Log.d("MainPresenter", e.getMessage());
                                onRefreshError(flag, baseAdapter);
                            }

                    }

                    @Override
                    public void onException(Call call, Exception e, int id) {
                        super.onException(call, e, id);
                        if (e.toString().contains("401"))
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Refresh(flag, layout, type, baseAdapter);
                                }
                            },500);
                        onRefreshError(flag, baseAdapter);
                    }
                });
    }

    /**
     * 刷新错误
     *
     * @param flag
     * @param baseAdapter
     */
    private void onRefreshError(final boolean flag, final RecyclerView.Adapter baseAdapter) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                MainItemAdapter itemAdapter = (MainItemAdapter) baseAdapter;
                if (flag) {
                    itemAdapter.setHeaderCount("休息中，请再试一下");
                } else
                    itemAdapter.updataBottomViewType(MainItemAdapter.ITEM_BOTTOM_TYPE_FLAG_ERROR);

            }
        });
    }

    /**
     * @param data
     * @param flag
     * @param type
     * @param baseAdapter
     */
    private void onUpDataRefresh(final List data, final boolean flag,
                                 final int type, final RecyclerView.Adapter baseAdapter) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                isRefresh=false;
                ((MainItemAdapter) baseAdapter).setHeaderCount(null);
                //没有数据，代表滑动到底
                if (data.size() > 0 && addDataList(data, type, flag))
                    baseAdapter.notifyItemRangeChanged(baseAdapter.getItemCount(), data.size());
                else {
                    ((MainItemAdapter) baseAdapter).updataBottomViewType(MainItemAdapter.ITEM_BOTTOM_TYPE_FLAG_END);
                    baseAdapter.notifyItemChanged(baseAdapter.getItemCount()-1);
                }
            }
        });
    }

    /**
     * @param data
     * @param flag
     * @param swipeRefreshLayout
     * @param type
     * @param baseAdapter
     */
    private void onDownDataRefresh(final List data, final boolean flag,
                                   final SwipeRefreshLayout swipeRefreshLayout, final int type,
                                   final RecyclerView.Adapter baseAdapter) {
        //开始刷新，不可能数据为空
        if (data.size() == 0)
            onRefreshError(flag, baseAdapter);
        else {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    isRefresh=false;
                    ((MainItemAdapter) baseAdapter).setHeaderCount(null);
                    if (addDataList(data, type, flag))
                        baseAdapter.notifyItemRangeChanged(0,data.size()-1);
                    if (swipeRefreshLayout != null)
                        swipeRefreshLayout.setRefreshing(false);
                }
            });
        }
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
        return url + "&token=" + EnlightenmentApplication.getInstance().getString(Constants.Set.SET_USER_TOKEN);
    }

    //记住item的ID
    private boolean addDataList(List data, int type, boolean Flag) {
        switch (type) {
            case HomeDynamicFragment.FRAGMENT_NEW:
                if (Flag) {
                    HomeNewList.clear();
                    HomeNewList.addAll(data);
                    return true;
                } else if (!HomeNewList.containsAll(data)) {
                    HomeNewList.addAll(data);
                    return true;
                } else
                    return false;
            case HomeDynamicFragment.FRAGMENT_HOT:
                if (Flag) {
                    HomeHotList.clear();
                    HomeHotList.addAll(data);
                    return true;
                } else if (!HomeHotList.containsAll(data)) {
                    HomeHotList.addAll(data);
                    return true;
                } else
                    return false;
            case HomeDynamicFragment.FRAGMENT_LIVE:
                if (Flag) {
                    HomeLoveList.clear();
                    HomeLoveList.addAll(data);
                    return true;
                } else if (!HomeLoveList.containsAll(data)) {
                    HomeLoveList.addAll(data);
                    return true;
                } else
                    return false;
            case FoundDynamicFragment.FRAGMENT_KONWLEDGE:
                if (Flag) {
                    FoundKnowledgeList.clear();
                    FoundKnowledgeList.addAll(data);
                    return true;
                } else if (!FoundKnowledgeList.containsAll(data)) {
                    FoundKnowledgeList.addAll(data);
                    return true;
                } else
                    return false;
            case FoundDynamicFragment.FRAGMENT_DIY:
                if (Flag) {
                    FoundDiyList.clear();
                    FoundDiyList.addAll(data);
                    return true;
                } else if (!FoundDiyList.containsAll(data)) {
                    FoundDiyList.addAll(data);
                    return true;
                } else
                    return false;
            case FoundDynamicFragment.FRAGMENT_HELP:
                if (Flag) {
                    FoundHelpList.clear();
                    FoundHelpList.addAll(data);
                    return true;
                } else if (!FoundHelpList.containsAll(data)) {
                    FoundHelpList.addAll(data);
                    return true;
                } else
                    return false;
            case FoundDynamicFragment.FRAGMENT_MYSELF:
                if (Flag) {
                    FoundMyselfList.clear();
                    FoundMyselfList.addAll(data);
                    return true;
                } else if (!FoundMyselfList.containsAll(data)) {
                    FoundMyselfList.addAll(data);
                    return true;
                } else
                    return false;
        }
        return false;
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

    public boolean isRefresh() {
        return isRefresh;
    }
}
