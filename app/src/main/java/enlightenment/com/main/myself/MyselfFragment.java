package enlightenment.com.main.myself;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.edit.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enlightenment.com.base.AppActivity;
import enlightenment.com.base.R;
import enlightenment.com.main.ItemViewHolder;
import enlightenment.com.tool.device.DisplayUtils;
import enlightenment.com.user.SettingActivity;
import enlightenment.com.user.UserActivity;
import enlightenment.com.view.GridDividerItemDecoration;
import enlightenment.com.view.PopupWindow.CusPopupWindow;

/**
 * Created by lw on 2017/7/26.
 */
public class MyselfFragment extends Fragment implements View.OnTouchListener,
        CusPopupWindow.OnCusPopupListener {
    private static MyselfFragment messageFragment;

    public static Fragment getInstanceFragment() {
        if (messageFragment == null) {
            messageFragment = new MyselfFragment();
        }
        return messageFragment;
    }

    @BindView(R.id.fragment_myself_stub)
    LinearLayout mAutographLayout;
    @BindView(R.id.fragment_myself_top)
    RelativeLayout mMyselfTopLayout;
    @BindView(R.id.fragment_myself_autograph_btn)
    ImageView mMyselfAutographBtn;
    @BindView(R.id.fragment_myself_autograph_text)
    TextView mMyselfAutographText;
    @BindView(R.id.fragment_myself_head_portrait)
    ImageView mHeadPortrait;
    @BindView(R.id.fragment_myself_username)
    TextView mUsername;
    @BindView(R.id.fragment_myself_position)
    TextView mPosition;
    @BindView(R.id.fragment_myself_info_follow_number)
    TextView mFollowNumber;
    @BindView(R.id.fragment_myself_info_concern_number)
    TextView mConcernNumber;
    @BindView(R.id.fragment_myself_info_day_number)
    TextView mDayNumber;
    @BindView(R.id.fragment_myself_tab)
    TabLayout mTabLayout;
    @BindView(R.id.fragment_myself_content_tool)
    ImageView mMyselfContentTool;
    @BindView(R.id.fragment_myself_content_viewpager)
    ViewPager mMyselfViewPager;
    @BindView(R.id.fragment_myself_tool)
    ImageView mMyselfTool;

    RecyclerView mAutographRecycler;
    EditText mAutographEdit;
    ImageView mAutographSend;

    private FragmentPagerAdapter fragmentPagerAdapter;
    private RecyclerView.Adapter mAutographAdapter;
    private String[] mTabStrings;
    private String[] mAutographStrings;
    View view;

    private int mIsScrollFlag = SCROLL_CLOSE;

    private float mDownY;
    private float mLastY;

    public static final int SCROLL_OPEN = 1;            // 完全打开
    public static final int SCROLL_UP = 2;              // 向上滑动中
    public static final int SCROLL_DOWN = 3;            // 向下滑动中
    public static final int SCROLL_CLOSE = 4;           // 完全关闭

    private CusPopupWindow mUserToolPopupWindow = null;
    private CusPopupWindow mToolPopupWindow = null;
    private String[] myselfTools;
    private String[] columnTools;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_myself, container, false);
        ButterKnife.bind(this, view);
        view.setOnTouchListener(this);
        init();
        initTab();
        return view;
    }

    private void init() {
        myselfTools = getActivity().getResources()
                .getStringArray(R.array.main_myself_tool);
        columnTools = getResources().getStringArray(R.array.main_myself_type_tool);
        mAutographRecycler = (RecyclerView) mAutographLayout.findViewById(R.id.myself_autograph_recycler);
        mAutographEdit = (EditText) mAutographLayout.findViewById(R.id.myself_autograph_edit);
        mAutographSend = (ImageView) mAutographLayout.findViewById(R.id.myself_autograph_send);
        initAutograph();
    }

    private void initTab() {
        mTabStrings = getResources().getStringArray(R.array.main_found_top);
        mAutographStrings = getResources().getStringArray(R.array.myself_auto_test);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < mTabStrings.length; i++) {
            TabLayout.Tab mTab = mTabLayout.newTab();
            mTabLayout.addTab(mTab);
        }
        fragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return mTabStrings.length;
            }

            @Override
            public Fragment getItem(int position) {
                return new MyselfColumnFragment();
            }

        };
        mMyselfViewPager.setAdapter(fragmentPagerAdapter);
        mTabLayout.setupWithViewPager(mMyselfViewPager);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            TextView textView = new TextView(getActivity());
            textView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setText(mTabStrings[i]);
            textView.setTextColor(getResources().getColor(R.color.grey01));
            textView.setTextSize(12);
            tab.setCustomView(textView);
        }

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView textView = (TextView) tab.getCustomView();
                textView.setTextColor(
                        getResources().getColor(R.color.grey_green));
                TextPaint tp = textView.getPaint();
                tp.setFakeBoldText(true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView textView = (TextView) tab.getCustomView();
                textView.setTextColor(
                        getResources().getColor(R.color.grey01));
                TextPaint tp = textView.getPaint();
                tp.setFakeBoldText(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int height = mAutographLayout.getMeasuredHeight();
                view.scrollTo(0, height);
            }
        });
    }

    @OnClick(R.id.fragment_myself_autograph_btn)
    public void onAutographBtn(View v) {
        if (mIsScrollFlag == SCROLL_OPEN) {
            view.scrollTo(0, mAutographLayout.getHeight());
            mIsScrollFlag = SCROLL_CLOSE;
        } else if (mIsScrollFlag == SCROLL_CLOSE) {
            view.scrollTo(0, 0);
            mIsScrollFlag = SCROLL_OPEN;
        }
    }

    @OnClick(R.id.fragment_myself_tool)
    public void onMyselfTool(View v) {
        if (mUserToolPopupWindow == null) {
            mUserToolPopupWindow = CusPopupWindow.Builder.getInstance(getActivity())
                    .setView(R.layout.fragment_recycler_only)
                    .setBackground(new ColorDrawable(getResources().getColor(R.color.grey00)))
                    .setWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                    .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                    .setFocusable(false)
                    .setOnCusPopupLinener(this).builder();
            mUserToolPopupWindow.showAsDropDown(v, 0, -v.getHeight() / 2, Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        } else {
            mUserToolPopupWindow.dismiss();
            mUserToolPopupWindow = null;
        }
    }

    @OnClick(R.id.fragment_myself_content_tool)
    public void onContentTool(View v) {
        if (mToolPopupWindow == null) {
            mToolPopupWindow = CusPopupWindow.Builder.getInstance(getActivity())
                    .setView(R.layout.fragment_recycler_only)
                    .setBackground(new ColorDrawable(getResources().getColor(R.color.grey00)))
                    .setWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                    .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                    .setFocusable(false)
                    .setOnCusPopupLinener(new CusPopupWindow.OnCusPopupListener() {
                        @Override
                        public void onCusPopupListener(View view) {
                            RecyclerView mToolRecycler = (RecyclerView) view;
                            mToolRecycler.setPadding(2, 2, 2, 2);
                            mToolRecycler.setBackgroundResource(R.color.gray);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            mToolRecycler.setLayoutManager(layoutManager);
                            mToolRecycler.setAdapter(new RecyclerView.Adapter() {
                                @Override
                                public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                                    ItemViewHolder.BaseViewHolder textViewHolder =
                                            new ItemViewHolder.BaseViewHolder(
                                                    ItemViewHolder.createTextView(getActivity(),
                                                            DisplayUtils.dp2px(getActivity(), 36),
                                                            DisplayUtils.dp2px(getActivity(), 84)));
                                    return textViewHolder;
                                }

                                @Override
                                public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
                                    ((TextView) holder.itemView).setText(columnTools[position]);
                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            onContentToolClick(position);
                                        }
                                    });
                                }

                                @Override
                                public int getItemCount() {
                                    return columnTools.length;
                                }
                            });
                        }
                    })
                    .builder();
            mToolPopupWindow.showAsDropDown(v, -v.getWidth(), 4,
                    Gravity.LEFT | Gravity.BOTTOM);
        } else {
            mToolPopupWindow.dismiss();
            mToolPopupWindow = null;
        }
    }

    private void onContentToolClick(int position) {
        if (mToolPopupWindow != null) {
            mToolPopupWindow.dismiss();
            mToolPopupWindow = null;
        }
    }

    private void initAutograph() {
        mAutographAdapter = new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                TextView textView = new TextView(getActivity());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(layoutParams);
                textView.setTextSize(12);
                textView.setPadding(12, 6, 12, 6);
                textView.setBackgroundResource(R.drawable.background_autograph);
                ItemViewHolder.BaseViewHolder textViewHolder = new ItemViewHolder.BaseViewHolder(textView);
                return textViewHolder;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((TextView) holder.itemView).setText(mAutographStrings[position]);
                int len = position % 4;
                int left = (len + 1) * 36;
                LinearLayout.LayoutParams layoutParams = ((LinearLayout.LayoutParams) holder.itemView.getLayoutParams());
                layoutParams.setMargins(left, 24, 0, 24);
                holder.itemView.setLayoutParams(layoutParams);
            }

            @Override
            public int getItemCount() {
                return mAutographStrings.length;
            }
        };
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mAutographRecycler.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider_item_autograph));
        mAutographRecycler.addItemDecoration(divider);
        mAutographRecycler.setAdapter(mAutographAdapter);
    }

    @Override
    public boolean onTouch(final View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = mDownY = motionEvent.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float move = motionEvent.getY() - mDownY;
                // down
                if (move > 0 && mIsScrollFlag != SCROLL_OPEN) {
                    if (Math.abs(move) < mAutographLayout.getHeight()) {
                        view.scrollBy(0, (int) -(motionEvent.getY() - mLastY));
                        mIsScrollFlag = SCROLL_DOWN;
                    } else {
                        mIsScrollFlag = SCROLL_OPEN;
                    }

                }
                // up
                else if (move < 0 && mIsScrollFlag != SCROLL_CLOSE) {
                    if (Math.abs(move) < mAutographLayout.getHeight()) {
                        view.scrollBy(0, (int) -(motionEvent.getY() - mLastY));
                        mIsScrollFlag = SCROLL_UP;
                    } else {
                        mIsScrollFlag = SCROLL_CLOSE;
                    }
                }
                Log.d(this.getClass().getName(), String.valueOf(motionEvent.getY() - mLastY));
                mLastY = motionEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                float moveUp = motionEvent.getY() - mDownY;
                // 滑动距离超过
                if (Math.abs(moveUp) >= mAutographLayout.getHeight() / 2) {
                    if (mIsScrollFlag == SCROLL_DOWN) {
                        //down
                        //startValueAnimator(mAutographLayout.getHeight() - moveUp, 0);
                        view.scrollTo(0, 0);
                        mIsScrollFlag = SCROLL_OPEN;
                    } else if (mIsScrollFlag == SCROLL_UP) {
                        //up
                        //startValueAnimator(moveUp, mAutographLayout.getHeight());
                        view.scrollTo(0, mAutographLayout.getHeight());
                        mIsScrollFlag = SCROLL_CLOSE;
                    }
                } else {
                    if (mIsScrollFlag == SCROLL_DOWN) {
                        //up
                        //startValueAnimator(mAutographLayout.getHeight() - moveUp,
                        //      mAutographLayout.getHeight());
                        view.scrollTo(0, mAutographLayout.getHeight());
                        mIsScrollFlag = SCROLL_CLOSE;
                    } else if (mIsScrollFlag == SCROLL_UP) {
                        //down
                        //startValueAnimator(moveUp, 0);
                        view.scrollTo(0, 0);
                        mIsScrollFlag = SCROLL_OPEN;
                    }
                }
                Log.d(this.getClass().getName(), "Flag:" + mIsScrollFlag);
                break;
        }
        return true;
    }

    @Override
    public void onCusPopupListener(View view) {
        RecyclerView mToolRecycler = (RecyclerView) view;
        mToolRecycler.setPadding(2, 2, 2, 2);
        mToolRecycler.setBackgroundResource(R.color.gray);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                getActivity(), 2, GridLayoutManager.HORIZONTAL, false);
        mToolRecycler.setLayoutManager(gridLayoutManager);
        mToolRecycler.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                ItemViewHolder.MyselfToolTextView toolTextView = new ItemViewHolder.MyselfToolTextView(
                        LayoutInflater.from(getActivity()).inflate(
                                R.layout.item_myself_tool, parent, false)
                        , getActivity());
                return toolTextView;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
                ItemViewHolder.MyselfToolTextView toolTextView = (ItemViewHolder.MyselfToolTextView) holder;
                if (position == 0) {
                    toolTextView.setToolImageVisibility(View.VISIBLE);
                }
                toolTextView.setToolName(myselfTools[position]);
                toolTextView.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onToolClick(position);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return myselfTools.length;
            }
        });
    }

    private void onToolClick(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(getActivity(), UserActivity.class));
                break;
            case 5:
                startActivity(new Intent(getActivity(), SettingActivity.class));
        }
        mUserToolPopupWindow.dismiss();
        mUserToolPopupWindow = null;
    }
}
