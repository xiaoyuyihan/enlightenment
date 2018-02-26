package enlightenment.com.main;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.edit.EditActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import enlightenment.com.base.EnlightenmentApplication;
import enlightenment.com.base.R;
import enlightenment.com.establish.learn.LearnActivity;
import enlightenment.com.search.SearchActivity;
import enlightenment.com.view.AgainNestedScrollView;
import enlightenment.com.view.CarouselView;

/**
 * Created by lw on 2017/7/26.
 */

public class HomeFragment extends Fragment implements MainView,View.OnClickListener{
    private static HomeFragment messageFragment;

    public static Fragment getInstanceFragment() {
        if (messageFragment == null) {
            messageFragment = new HomeFragment();
        }
        return messageFragment;
    }

    private View view;
    public TabLayout mTabLayout;
    private ViewPager mViewGroup;
    private TextView mSearch;

    private String[] mTabStrings;
    private int[] mTabId;
    private MessageFragmentPagerAdapter mPagerAdapter;
    private AgainNestedScrollView nestedScrollView;
    private HomeDynamicFragment currentFragment;
    private SwipeRefreshLayout refreshLayout;
    private CarouselView mRollingLayout;

    @BindView(R.id.fragment_main_toolbar_layout)
    CollapsingToolbarLayout mToolBarLayout;

    private MainPresenter mainPresenter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainPresenter = MainPresenter.getInstance();
        mainPresenter.BindView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_home, container, false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    private void initView() {
        View learnButton=view.findViewById(R.id.fragment_home_learn);
        learnButton.setOnClickListener(this);
        View createButton=view.findViewById(R.id.fragment_home_create);
        createButton.setOnClickListener(this);
        View questionButton=view.findViewById(R.id.fragment_home_question);
        questionButton.setOnClickListener(this);
        mTabLayout = (TabLayout) view.findViewById(R.id.fragment_home_tabLayout);
        mViewGroup = (ViewPager) view.findViewById(R.id.fragment_home_viewpager);
        mSearch = (TextView) view.findViewById(R.id.small_search_bar);
        mSearch.setOnClickListener(this);
        mRollingLayout=(CarouselView)view.findViewById(R.id.home_rooling);
        mTabStrings = getResources().getStringArray(R.array.main_home_tob);
        /**
         * getFragmentManager()是所在fragment 父容器的碎片管理，
         * getChildFragmentManager()是在fragment  里面子容器的碎片管理。
         */
        mPagerAdapter = new MessageFragmentPagerAdapter(getChildFragmentManager(), mTabStrings, 1);
        mViewGroup.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewGroup);
        mPagerAdapter.setTabLayout(mTabLayout);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Object position=tab.getTag();
                if (position!=null){
                    HomeDynamicFragment.getInstance(new Integer(position.toString())).Refresh();
                }
            }
        });
    }

    @Override
    public void swipeRefresh(SwipeRefreshLayout swipeRefreshLayout) {

    }

    @Override
    public Activity getMainActivity() {
        return null;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.small_search_bar:
                intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.fragment_home_learn:
                intent = new Intent(getActivity(), EditActivity.class);
                startActivity(intent);
                break;
            case R.id.fragment_home_create:
                break;
            case R.id.fragment_home_question:
                break;
        }
    }

    static class MessageFragmentPagerAdapter extends FragmentPagerAdapter {
        String[] mTabStrings;
        int type;
        private TabLayout tabLayout = null;

        public MessageFragmentPagerAdapter(FragmentManager fm, String[] mTabStrings, int type) {
            super(fm);
            this.mTabStrings = mTabStrings;
            this.type = type;
        }

        @Override
        public Fragment getItem(final int position) {
            if (tabLayout != null) {
                tabLayout.getTabAt(position).setTag(position);
            }
            if (type == 1) {
                HomeDynamicFragment dynamicFragment = HomeDynamicFragment.getInstance(position);
                return dynamicFragment;
            } else {
                return FoundDynamicFragment.getInstance(position + 3);
            }
        }

        public void setTabLayout(TabLayout tabLayout) {
            this.tabLayout = tabLayout;
        }

        @Override
        public int getCount() {
            return mTabStrings.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabStrings[position];
        }

        public View getTabView(int position) {
            View view = LayoutInflater.from(EnlightenmentApplication.getInstance()).inflate(R.layout.item_found_tab, null);
            TextView name = (TextView) view.findViewById(R.id.item_tab_name);
            TextView number = (TextView) view.findViewById(R.id.item_tab_number);
            name.setText(mTabStrings[position]);
            number.setText("12");
            return view;
        }
    }


}
