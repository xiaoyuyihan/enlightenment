package enlightenment.com.main.home;


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
import android.widget.TextView;

import com.edit.EditActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import enlightenment.com.base.AppActivity;
import enlightenment.com.base.EnlightenmentApplication;
import enlightenment.com.base.R;
import enlightenment.com.main.found.FoundDynamicFragment;
import enlightenment.com.main.MainPresenter;
import enlightenment.com.main.MainView;
import enlightenment.com.question.QuestionActivity;
import enlightenment.com.search.SearchActivity;
import enlightenment.com.view.AgainNestedScrollView;
import enlightenment.com.view.CarouselView;

/**
 * Created by lw on 2017/7/26.
 */

public class HomeFragment extends Fragment implements MainView {
    private static HomeFragment messageFragment;

    public static Fragment getInstanceFragment() {
        if (messageFragment == null) {
            messageFragment = new HomeFragment();
        }
        return messageFragment;
    }

    private View view;
    @Nullable
    @BindView(R.id.fragment_home_tabLayout)
    public TabLayout mTabLayout;
    @Nullable
    @BindView(R.id.fragment_home_viewpager)
    ViewPager mViewGroup;

    private String[] mTabStrings;
    private MessageFragmentPagerAdapter mPagerAdapter;
    @BindView(R.id.home_rooling)
    CarouselView mRollingLayout;
    @Nullable
    @BindView(R.id.fragment_main_toolbar_layout)
    CollapsingToolbarLayout mToolBarLayout;

    private MainPresenter mainPresenter;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        mainPresenter = MainPresenter.getInstance();
        mainPresenter.BindView(this);
        return view;
    }

    private void initView() {
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
                Object position = tab.getTag();
                if (position != null) {
                    HomeDynamicFragment.getInstance(new Integer(position.toString())).Refresh();
                }
            }
        });
    }

    @OnClick(R.id.small_search_bar)
    public void onSearchBarClick(View v) {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.fragment_home_learn)
    public void onCreateLearn(View view) {
        Intent intent = new Intent(getActivity(), EditActivity.class);
        intent.putExtra(EditActivity.ACTIVITY_EDIT_TYPE, EditActivity.ACTIVITY_EDIT_TYPE_CREATE);
        intent.putExtra(EditActivity.ACTIVITY_MODEL_TYPE, EditActivity.ACTIVITY_MODEL_TYPE_LEARN);
        startActivity(intent);
    }

    @OnClick(R.id.fragment_home_create)
    public void onCreateCreate(View view) {
        Intent intent = new Intent(getActivity(), EditActivity.class);
        intent.putExtra(EditActivity.ACTIVITY_EDIT_TYPE, EditActivity.ACTIVITY_EDIT_TYPE_CREATE);
        intent.putExtra(EditActivity.ACTIVITY_MODEL_TYPE, EditActivity.ACTIVITY_MODEL_TYPE_CREATE);
        startActivity(intent);
    }

    @OnClick(R.id.fragment_home_question)
    public void onCreateQuestion(View view) {
        Intent intent = new Intent(getActivity(), QuestionActivity.class);
        startActivity(intent);
    }


    @Override
    public void showToast(String message) {
        getAppActivity().showCustomToast(message);
    }

    @Override
    public Object getObj() {
        return null;
    }

    public static class MessageFragmentPagerAdapter extends FragmentPagerAdapter {
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
            View view = LayoutInflater.from(EnlightenmentApplication.getInstance())
                    .inflate(R.layout.item_found_tab, null);
            TextView name = (TextView) view.findViewById(R.id.item_tab_name);
            TextView number = (TextView) view.findViewById(R.id.item_tab_number);
            name.setText(mTabStrings[position]);
            number.setText("12");
            return view;
        }
    }

    public AppActivity getAppActivity() {
        return (AppActivity) getActivity();
    }

}
