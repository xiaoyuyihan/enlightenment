package enlightenment.com.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import enlightenment.com.base.R;
import enlightenment.com.details.ContentDetailsActivity;
import enlightenment.com.tool.okhttp.builder.GetBuilder;
import enlightenment.com.view.Dialog.ImageShowDialog;
import enlightenment.com.view.NineGridLayout.ItemNineGridLayout;

/**
 * Created by lw on 2017/7/27.
 */

public class HomeDynamicFragment extends Fragment implements MainView,
        SwipeRefreshLayout.OnRefreshListener, OnContentItemListener{

    public static final int FRAGMENT_NEW = 0;
    public static final int FRAGMENT_HOT = 1;
    public static final int FRAGMENT_LIVE = 2;

    private static HomeDynamicFragment newDynamicFragment;
    private static HomeDynamicFragment hotDynamicFragment;
    private static HomeDynamicFragment liveDynamicFragment;

    private MainPresenter mainPresenter;

    private Handler mHandler = new Handler();

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
    private SwipeRefreshLayout mSwipeRefresh;
    private int typeFragment;
    private MainItemAdapter adapter;
    //是否请求中
    private boolean flag = false;

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
        View view = inflater.inflate(R.layout.fragment_scroll_recycler, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_recycler);
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.fragment_swipe_refresh);
        mSwipeRefresh.setOnRefreshListener(this);
        adapter = new MainItemAdapter(getActivity(), mainPresenter.getDataList(getTypeFragment()),
                R.layout.item_content_view);
        adapter.setOnClickImageListener(new ItemNineGridLayout.OnClickImageListener() {
            @Override
            public void onClickImage(String url) {
                ImageShowDialog imageShowDialog=new ImageShowDialog();
                Bundle bundle=new Bundle();
                bundle.putString(ImageShowDialog.IMAGE_SHOW_DATA,url);
                imageShowDialog.setArguments(bundle);
                imageShowDialog.show(getFragmentManager(),"ImageShowDialog");
            }
        });
        adapter.setOnContentItemListener(this);
        //线性布局管理器
        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        //设置布局管理器
        mRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        //添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.divider_item_decoration));
        mRecyclerView.addItemDecoration(divider);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Refresh();
    }

    public void Refresh() {
        mainPresenter.upRefresh(this, mSwipeRefresh, adapter);
    }

    @Override
    public void swipeRefresh(SwipeRefreshLayout swipeRefreshLayout) {

    }

    public int getTypeFragment() {
        return typeFragment;
    }

    @Override
    public void onRefresh() {
        Refresh();
    }

    @Override
    public void showToast(String message) {

    }

    @Override
    public void onItemClick(ContentBean contentBean) {
        Intent intent=new Intent(getActivity(), ContentDetailsActivity.class);
        intent.putExtra(ContentDetailsActivity.CONTENT_EXTRA_DATA,contentBean);
        startActivity(intent);
    }

    @Override
    public void onAvatarClick() {

    }

    @Override
    public void onModelClick() {

    }
}
