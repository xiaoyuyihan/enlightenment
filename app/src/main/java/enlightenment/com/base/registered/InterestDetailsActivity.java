package enlightenment.com.base.registered;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.BindView;
import enlightenment.com.base.AppActivity;
import enlightenment.com.base.R;
import enlightenment.com.module.ModuleChildBean;
import enlightenment.com.view.FlowLayout;

/**
 * Created by lw on 2017/8/15.
 * 偏好选择详情页面
 */

public class InterestDetailsActivity extends AppActivity implements View.OnClickListener {
    public static String EXTRA_OBJECT = "data";

    private ArrayList<ModuleChildBean> moduleChildBeen;
    private String module = "";

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.cv_add)
    CardView cvAdd;
    @BindView(R.id.interest_flow)
    FlowLayout mFlowLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_interest_details;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ShowEnterAnimation();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateRevealClose();
            }
        });
    }

    @Override
    protected void initData() {
        moduleChildBeen = getIntent().getExtras().getParcelableArrayList(EXTRA_OBJECT);
        setChildView();
    }

    @Override
    protected void clearData() {
        if (moduleChildBeen != null) {
            moduleChildBeen.clear();
            moduleChildBeen = null;
        }
    }

    private void setChildView() {
        for (ModuleChildBean childBean : moduleChildBeen) {
            TextView textView = new TextView(this);
            textView.setTag(childBean);
            textView.setText(childBean.getName());
            ViewGroup.MarginLayoutParams m = new ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            m.setMargins(18, 12, 18, 2);
            textView.setBackground(getResources().getDrawable(R.drawable.background_interest_module));
            textView.setLayoutParams(m);
            textView.setOnClickListener(this);
            mFlowLayout.addView(textView);
        }
    }

    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }


        });
    }

    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, fab.getWidth() / 2, cvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, cvAdd.getHeight(), fab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                fab.setImageResource(R.drawable.plus);
                InterestDetailsActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                setResult(1, new Intent().putExtra(EXTRA_OBJECT, module));
            }
        });
        mAnimator.start();
    }

    @Override
    public void onBackPressed() {
        animateRevealClose();
        finish();
    }

    @Override
    public void onClick(View view) {
        Object bean = view.getTag();
        if (bean != null) {
            ModuleChildBean childBean = (ModuleChildBean) bean;
            if (module.contains(String.valueOf(childBean.getIdentity()))) {
                module = module.replaceAll("," + childBean.getIdentity() + "|" + childBean.getIdentity(), "");
                view.setBackground(getResources().getDrawable(R.drawable.background_interest_module));
            } else {
                if (module.equals("")) {
                    module = module + String.valueOf(childBean.getIdentity());
                } else
                    module = module + "," + String.valueOf(childBean.getIdentity());
                view.setBackground(getResources().getDrawable(R.drawable.background_interest_module_false));
            }
        }
    }
}
