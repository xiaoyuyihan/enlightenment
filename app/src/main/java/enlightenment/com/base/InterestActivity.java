package enlightenment.com.base;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import enlightenment.com.module.ModuleChildBean;
import enlightenment.com.module.ModuleFatherBean;
import enlightenment.com.view.CircularLayout;
import enlightenment.com.view.CircularTextView;

/**
 * Created by lw on 2017/8/15.
 */

public class InterestActivity extends AppActivity implements baseView,
        View.OnClickListener {
    private LayoutInflater mInflater;
    @InjectView(R.id.top_left_image)
    public ImageView topLeft;
    @InjectView(R.id.top_right_text)
    public TextView topRight;
    @InjectView(R.id.top_center_text)
    public TextView topCenter;
    @InjectView(R.id.interest_circular)
    public CircularLayout circularLayout;
    @InjectView(R.id.interest_view_loading)
    public LinearLayout loadingLayout;

    private String models = "";

    private List<ModuleFatherBean> moduleFatherBeen;
    private basePresenter mPresenter;
    private CircularAdapter circularAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);
        ButterKnife.inject(this);
        topLeft.setOnClickListener(this);
        topRight.setOnClickListener(this);
        topCenter.setText("选择兴趣点");

        mInflater = LayoutInflater.from(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter = basePresenter.getInstance();
        mPresenter.BindView(this);
        mPresenter.onStart();
        moduleFatherBeen=EnlightenmentApplication.getInstance().getModuleFatherBeen();
        circularAdapter =new CircularAdapter(moduleFatherBeen, this, new CircularAdapter.OnClickListener() {

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
        if (moduleFatherBeen.size()<=0){
            loadingLayout.setVisibility(View.VISIBLE);
            circularLayout.setVisibility(View.GONE);
            mPresenter.obtainModule();
        }else {
            loadingLayout.setVisibility(View.GONE);
            circularLayout.setVisibility(View.VISIBLE);
        }
        circularLayout.setAdapter(circularAdapter);
        circularLayout.requestLayout();
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
    public void startNextActivity(Class name) {
        Intent intent = new Intent(this, name);
        intent.putExtra(PhoneValidationActivity.TYPE_EXTES, PhoneValidationActivity.TYPE_REGISTER);
        startActivity(intent);
        finish();
    }

    @Override
    public AppCompatActivity getMainActivity() {
        return this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //多次重复添加兴趣点
        if (requestCode == 1) {
            String modelsResult=data.getExtras().getString(
                    InterestDetailsActivity.EXTRA_OBJECT);
            if (!modelsResult.equals("")) {
               if(models.equals(""))
                   models=models+modelsResult;
                else
                   models=models+","+modelsResult;
            }
        }
    }

    public void setModuleFatherBeen(List moduleFatherBeen){
        if (moduleFatherBeen!=null&&moduleFatherBeen.size()>0){
            this.moduleFatherBeen=moduleFatherBeen;
            circularAdapter.notifyDataSetChanged();
            loadingLayout.setVisibility(View.GONE);
            circularLayout.setVisibility(View.VISIBLE);
        }else
            showToast("sorry，数据暂时有问题，请跳过这个页面");
    }

    public static class CircularAdapter extends BaseAdapter {
        List<ModuleFatherBean> moduleFatherBeen;
        Activity appActivity;
        LayoutInflater mInflater;
        OnClickListener onClickLinster;

        public CircularAdapter(List<ModuleFatherBean> moduleFatherBeen, Activity appActivity, OnClickListener clickLinster) {
            this.moduleFatherBeen = moduleFatherBeen;
            this.appActivity=appActivity;
            this.mInflater=LayoutInflater.from(appActivity);
            this.onClickLinster=clickLinster;
        }

        @Override
        public int getCount() {
            return moduleFatherBeen.size();
        }

        @Override
        public ModuleFatherBean getItem(int i) {
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
                    onClickLinster.onclick(i,view);
                }
            });
            return textView;
        }
        public interface OnClickListener{
            void onclick(int i,View view);
        }
    }
}
