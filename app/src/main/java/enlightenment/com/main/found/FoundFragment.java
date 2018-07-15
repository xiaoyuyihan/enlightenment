package enlightenment.com.main.found;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import enlightenment.com.base.R;
import enlightenment.com.main.MainActivity;
import enlightenment.com.main.home.HomeFragment;
import enlightenment.com.module.ModulesSelectFragment;

/**
 * Created by lw on 2017/7/26.
 */
public class FoundFragment extends Fragment implements TabLayout.OnTabSelectedListener{
    private static FoundFragment foundFragment;
    public static Fragment getInstanceFragment() {
        if (foundFragment == null) {
            foundFragment = new FoundFragment();
        }
        return foundFragment;
    }
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ImageView mMoveImage;
    private String[] mTabStrings;
    private HomeFragment.MessageFragmentPagerAdapter mPagerAdapter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_main_found,container,false);
        mTabLayout=(TabLayout)view.findViewById(R.id.fragment_found_tabLayout);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mViewPager=(ViewPager)view.findViewById(R.id.fragment_found_viewpager);
        mMoveImage=(ImageView)view.findViewById(R.id.fragment_found_move);
        mMoveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity=getActivity();
                if (activity instanceof MainActivity){
                    ((MainActivity)activity).setMainFragment(ModulesSelectFragment.getInstance());
                }
            }
        });
        mTabStrings=getResources().getStringArray(R.array.main_found_top);
        /**
         * getFragmentManager()是所在fragment 父容器的碎片管理，
         * getChildFragmentManager()是在fragment  里面子容器的碎片管理。
         */
        mPagerAdapter=new HomeFragment.MessageFragmentPagerAdapter(
                getChildFragmentManager(),mTabStrings,2);
        mPagerAdapter.setTabLayout(mTabLayout);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            tab.setCustomView(mPagerAdapter.getTabView(i));
        }
        mTabLayout.addOnTabSelectedListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TextView)mTabLayout.getTabAt(0).getCustomView().findViewById(R.id.item_tab_name))
                .setTextColor(getResources().getColor(R.color.colorAccent));
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Object position=tab.getTag();
        if (position!=null){
            FoundDynamicFragment.getInstance(new Integer(position.toString())+3).Refresh();
        }
        ((TextView)tab.getCustomView().findViewById(R.id.item_tab_name))
                .setTextColor(getResources().getColor(R.color.colorAccent));
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        ((TextView)tab.getCustomView().findViewById(R.id.item_tab_name))
                .setTextColor(getResources().getColor(R.color.grey01));
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
