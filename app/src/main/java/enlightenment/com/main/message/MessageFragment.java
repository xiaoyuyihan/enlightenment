package enlightenment.com.main.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import enlightenment.com.base.R;

/**
 * Created by lw on 2017/7/26.
 */
public class MessageFragment extends Fragment{
    private static MessageFragment messageFragment;
    public static Fragment getInstanceFragment() {
        if (messageFragment==null){
            messageFragment=new MessageFragment();
        }
        return messageFragment;
    }

    @BindView(R.id.fragment_message_top_text)
    TextView mTopText;
    @BindView(R.id.fragment_message_top_tab)
    TabLayout mTabLayout;
    @BindView(R.id.fragment_message_top_pager)
    ViewPager mViewPage;

    String[] mMessageStrings;
    int[] mMessageRes = {R.drawable.ic_message_plan,R.drawable.ic_message_notice};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_message,container,false);
        ButterKnife.bind(this,view);
        init();
        return view;
    }

    private void init() {
        mMessageStrings = getResources().getStringArray(R.array.message_test);

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < mMessageStrings.length; i++) {
            TabLayout.Tab mTab = mTabLayout.newTab();
            mTabLayout.addTab(mTab);
        }
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return mMessageStrings.length;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return new MessagePlanFragment();
                    case 1:
                        return new MessageNoticeFragment();
                    case 2:
                        return new MessageNewsFragment();
                }
                return new MessagePlanFragment();
            }

        };
        mViewPage.setAdapter(fragmentPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPage);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            ImageView imageView = new ImageView(getActivity());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(
                    48,
                    48));
            imageView.setImageDrawable(getResources().getDrawable(mMessageRes[i]));
            tab.setCustomView(imageView);
            tab.setTag(mMessageStrings[i]);
        }
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mTopText.setText(tab.getTag().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
