package enlightenment.com.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import enlightenment.com.base.R;

/**
 * Created by lw on 2017/7/27.
 */

public class HomeDynamicFragment extends Fragment implements MainView {

    public static final int FRAGMENT_NEW = 0;
    public static final int FRAGMENT_HOT = 1;
    public static final int FRAGMENT_LIVE = 2;

    private static HomeDynamicFragment newDynamicFragment;
    private static HomeDynamicFragment hotDynamicFragment;
    private static HomeDynamicFragment liveDynamicFragment;

    private MainPresenter mainPresenter;

    public static HomeDynamicFragment getInstance(int i) {
        switch (i) {
            case FRAGMENT_NEW:
                if (newDynamicFragment == null) {
                    newDynamicFragment = new HomeDynamicFragment(FRAGMENT_NEW);
                }
                return newDynamicFragment;
            case FRAGMENT_HOT:
                if (hotDynamicFragment == null) {
                    hotDynamicFragment = new HomeDynamicFragment(FRAGMENT_HOT);
                }
                return hotDynamicFragment;
            case FRAGMENT_LIVE:
                if (liveDynamicFragment == null) {
                    liveDynamicFragment = new HomeDynamicFragment(FRAGMENT_LIVE);
                }
                return liveDynamicFragment;
        }
        return newDynamicFragment;
    }

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout refreshLayout;
    private int typeFragment;
    private MainItemAdapter adapter;
    private boolean Falg = false;

    public HomeDynamicFragment(int typeFragment) {
        this.typeFragment = typeFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainPresenter = MainPresenter.getInstance();
        mainPresenter.BindView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_recycler);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_swipe_refresh);
        adapter = new MainItemAdapter(getActivity(),mainPresenter.getDataList(getTypeFragment()), R.layout.item_learn_text);
        //线性布局管理器
        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        //设置布局管理器
        mRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnScrollListener(new OnRecyclerScrollListener(recyclerViewLayoutManager, new OnRecyclerScrollListener.OnRefreshListener() {
            @Override
            public void Refresh() {
                mainPresenter.downRefresh(HomeDynamicFragment.this, adapter);
            }
        }));
        mRecyclerView.setNestedScrollingEnabled(false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!Falg) {
            Falg = !Falg;
            Refresh();
        }
    }

    public void Refresh() {
        if (Falg) {
            refreshLayout.setRefreshing(true);
            mainPresenter.upRefresh(this, refreshLayout, adapter);
        }
    }

    @Override
    public void swipeRefresh(SwipeRefreshLayout swipeRefreshLayout) {

    }

    @Override
    public Activity getMainActivity() {
        return getActivity();
    }

    public int getTypeFragment() {
        return typeFragment;
    }
}
