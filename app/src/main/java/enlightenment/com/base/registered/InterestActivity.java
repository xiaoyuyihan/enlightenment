package enlightenment.com.base.registered;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import enlightenment.com.base.AppActivity;
import enlightenment.com.base.EnlightenmentApplication;
import enlightenment.com.base.PhoneValidationActivity;
import enlightenment.com.base.R;
import enlightenment.com.base.basePresenter;
import enlightenment.com.base.baseView;
import enlightenment.com.module.ModuleChildBean;
import enlightenment.com.module.ModuleBean;
import enlightenment.com.view.CircularLayout;
import enlightenment.com.view.CircularTextView;

/**
 * Created by lw on 2017/8/15.
 * 偏好页面
 */

public class InterestActivity extends AppActivity implements baseView,
        View.OnClickListener {

    @BindView(R.id.top_left_image)
    public ImageView topLeft;
    @BindView(R.id.top_right_text)
    public TextView topRight;
    @BindView(R.id.top_center_text)
    public TextView topCenter;
    @BindView(android.R.id.tabhost)
    public TabHost mTobTab;
    @BindView(R.id.interest_circular_study)
    public CircularLayout circularStudyLayout;
    @BindView(R.id.interest_circular_create)
    public CircularLayout circularCreateLayout;
    @BindView(R.id.interest_view_loading)
    public LinearLayout loadingLayout;

    private String models = "";

    private List<ModuleBean> moduleFatherBeen;
    private List<ModuleBean> createFatherBeen;
    private basePresenter mPresenter;
    private CircularAdapter circularAdapter;
    private CircularAdapter createAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_interest;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        topLeft.setOnClickListener(this);
        topRight.setOnClickListener(this);
        topCenter.setText("选择兴趣点");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter = basePresenter.getInstance();
        mPresenter.BindView(this);
        mPresenter.onStart();
        mTobTab.setup();
        mTobTab.addTab(mTobTab.newTabSpec("tab1").setIndicator("学习栏目").setContent(R.id.tab1));
        mTobTab.addTab(mTobTab.newTabSpec("tab2").setIndicator("创造栏目").setContent(R.id.tab2));
        setStudyInterestView();
        setCreateInterestView();
    }

    private void setCreateInterestView() {
        createFatherBeen = EnlightenmentApplication.getInstance().getOrientationBeen();
        createAdapter = new CircularAdapter(createFatherBeen, this,
                new CircularAdapter.OnClickListener() {

                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onclick(int i, View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setExitTransition(null);
                            getWindow().setEnterTransition(null);
                        }
                        Intent intent = new Intent(InterestActivity.this, InterestDetailsActivity.class);
                        ArrayList<ModuleChildBean> data = createFatherBeen.get(i).getChildBeen();
                        intent.putExtra(InterestDetailsActivity.EXTRA_OBJECT, data);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ActivityOptions options =
                                    ActivityOptions.makeSceneTransitionAnimation(InterestActivity.this, view, view.getTransitionName());
                            startActivityForResult(intent, 1, options.toBundle());
                        } else {
                            startActivityForResult(intent, 1);
                        }
                    }
                });
        if (createFatherBeen.size() <= 0) {
            loadingLayout.setVisibility(View.VISIBLE);
            circularCreateLayout.setVisibility(View.GONE);
            mPresenter.obtainModule();
        } else {
            loadingLayout.setVisibility(View.GONE);
            circularCreateLayout.setVisibility(View.VISIBLE);
        }
        circularCreateLayout.setAdapter(createAdapter);
        circularCreateLayout.requestLayout();
    }

    private void setStudyInterestView() {
        moduleFatherBeen = EnlightenmentApplication.getInstance().getMajorBeen();
        circularAdapter = new CircularAdapter(moduleFatherBeen, this,
                new CircularAdapter.OnClickListener() {

                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onclick(int i, View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setExitTransition(null);
                            getWindow().setEnterTransition(null);
                        }
                        Intent intent = new Intent(InterestActivity.this, InterestDetailsActivity.class);
                        ArrayList<ModuleChildBean> data = moduleFatherBeen.get(i).getChildBeen();
                        intent.putExtra(InterestDetailsActivity.EXTRA_OBJECT, data);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ActivityOptions options =
                                    ActivityOptions.makeSceneTransitionAnimation(InterestActivity.this, view, view.getTransitionName());
                            startActivityForResult(intent, 1, options.toBundle());
                        } else {
                            startActivityForResult(intent, 1);
                        }
                    }
                });
        if (moduleFatherBeen.size() <= 0) {
            loadingLayout.setVisibility(View.VISIBLE);
            circularStudyLayout.setVisibility(View.GONE);
            mPresenter.obtainModule();
        } else {
            loadingLayout.setVisibility(View.GONE);
            circularStudyLayout.setVisibility(View.VISIBLE);
        }
        circularStudyLayout.setAdapter(circularAdapter);
        circularStudyLayout.requestLayout();
    }

    @Override
    public void onClick(View view) {
        EnlightenmentApplication.getInstance().setModules(models);
        switch (view.getId()) {
            case R.id.top_left_image:
                startNextActivity(PhoneValidationActivity.class);
                break;
            case R.id.top_right_text:
                startNextActivity(RegisteredActivity.class);
                break;
        }
    }

    @Override
    public void showToast(String message) {

    }

    @Override
    public Object getObj() {
        return null;
    }

    @Override
    public void startNextActivity(Class name) {
        Intent intent = new Intent(this, name);
        intent.putExtra(PhoneValidationActivity.TYPE_EXTES, PhoneValidationActivity.TYPE_REGISTER);
        startActivity(intent);
        finish();
    }

    @Override
    public void requestException() {
        showToast("请求不到数据，请检测一下网络信号");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //多次重复添加兴趣点
        if (requestCode == 1) {
            String modelsResult = data.getExtras().getString(
                    InterestDetailsActivity.EXTRA_OBJECT);
            if (!modelsResult.equals("")) {
                if (models.equals(""))
                    models = models + modelsResult;
                else
                    models = models + "," + modelsResult;
            }
        }
    }

    public void setModuleFatherBeen(List moduleFatherBeen) {
        if (moduleFatherBeen != null && moduleFatherBeen.size() > 0) {
            this.moduleFatherBeen = moduleFatherBeen;
            circularAdapter.notifyDataSetChanged();
            loadingLayout.setVisibility(View.GONE);
            circularStudyLayout.setVisibility(View.VISIBLE);
        } else
            showToast("sorry，数据暂时有问题，请跳过这个页面");
    }

    public void setCreateFatherBeen(List<ModuleBean> createFatherBeen) {
        if (createFatherBeen != null && createFatherBeen.size() > 0) {
            this.createFatherBeen = createFatherBeen;
            createAdapter.notifyDataSetChanged();
            loadingLayout.setVisibility(View.GONE);
            circularCreateLayout.setVisibility(View.VISIBLE);
        } else
            showToast("sorry，数据暂时有问题，请跳过这个页面");
    }

    public static class CircularAdapter extends BaseAdapter {
        List<ModuleBean> moduleFatherBeen;
        Activity appActivity;
        LayoutInflater mInflater;
        OnClickListener onClickLinster;

        public CircularAdapter(List<ModuleBean> moduleFatherBeen, Activity appActivity, OnClickListener clickLinster) {
            this.moduleFatherBeen = moduleFatherBeen;
            this.appActivity = appActivity;
            this.mInflater = LayoutInflater.from(appActivity);
            this.onClickLinster = clickLinster;
        }

        @Override
        public int getCount() {
            return moduleFatherBeen.size();
        }

        @Override
        public ModuleBean getItem(int i) {
            return moduleFatherBeen.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            final CircularTextView textView = (CircularTextView) mInflater.inflate(R.layout.item_button, viewGroup, false);
            textView.setText(getItem(i).getName());

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickLinster.onclick(i, view);
                }
            });
            return textView;
        }

        public interface OnClickListener {
            void onclick(int i, View view);
        }
    }
}
