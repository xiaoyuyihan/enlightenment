package enlightenment.com.main.found;

import android.content.Context;
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
import enlightenment.com.main.MainPresenter;
import enlightenment.com.main.MainView;
import enlightenment.com.main.OnRecyclerScrollListener;
import enlightenment.com.main.mainAdapter.FoundItemAdapter;

/**
 * Created by lw on 2017/9/4.
 */

public class FoundDynamicFragment extends Fragment implements MainView {
    public static final int FRAGMENT_KONWLEDGE = 3;
    public static final int FRAGMENT_DIY = 4;
    public static final int FRAGMENT_HELP = 5;
    public static final int FRAGMENT_MYSELF = 6;

    private static FoundDynamicFragment knowledgeDynamicFragment;
    private static FoundDynamicFragment diyDynamicFragment;
    private static FoundDynamicFragment helpDynamicFragment;
    private static FoundDynamicFragment myselfDynamicFragment;

    public static FoundDynamicFragment getInstance(int i) {
        switch (i) {
            case FRAGMENT_KONWLEDGE:
                if (knowledgeDynamicFragment == null) {
                    knowledgeDynamicFragment = new FoundDynamicFragment(FRAGMENT_KONWLEDGE);
                }
                return knowledgeDynamicFragment;
            case FRAGMENT_DIY:
                if (diyDynamicFragment == null) {
                    diyDynamicFragment = new FoundDynamicFragment(FRAGMENT_DIY);
                }
                return diyDynamicFragment;
            case FRAGMENT_HELP:
                if (helpDynamicFragment == null) {
                    helpDynamicFragment = new FoundDynamicFragment(FRAGMENT_DIY);
                }
                return helpDynamicFragment;
            case FRAGMENT_MYSELF:
                if (myselfDynamicFragment == null) {
                    myselfDynamicFragment = new FoundDynamicFragment(FRAGMENT_MYSELF);
                }
                return myselfDynamicFragment;
        }
        return knowledgeDynamicFragment;
    }

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout refreshLayout;

    private MainPresenter mainPresenter;
    private FoundItemAdapter adapter;

    public int getTypeFragment() {
        return typeFragment;
    }

    private int typeFragment;

    public FoundDynamicFragment(int type) {
        typeFragment = type;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_recycler);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_swipe_refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mainPresenter.downRefresh(getTypeFragment(), refreshLayout,adapter);
            }
        });
        adapter = new FoundItemAdapter(getActivity());
        //线性布局管理器
        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        //设置布局管理器
        mRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.addOnScrollListener(new OnRecyclerScrollListener(recyclerViewLayoutManager, new OnRecyclerScrollListener.OnRefreshListener() {
            @Override
            public void Refresh() {
                mainPresenter.upRefresh(getTypeFragment(), adapter);
            }
        }));
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainPresenter = MainPresenter.getInstance();
        mainPresenter.BindView(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mainPresenter.unBindView(this);
    }

    public void Refresh() {
        refreshLayout.setRefreshing(true);
        mainPresenter.downRefresh(getTypeFragment(), refreshLayout, adapter);
    }

    @Override
    public void showToast(String message) {

    }

    @Override
    public Object getObj() {
        return null;
    }

    public static class FoundViewHolder extends RecyclerView.ViewHolder{

        public FoundViewHolder(View itemView) {
            super(itemView);
        }
    }
}
