package enlightenment.com.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import butterknife.BindView;
import butterknife.ButterKnife;
import enlightenment.com.base.AppActivity;
import enlightenment.com.base.R;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by admin on 2018/6/10.
 * 用户个人详情
 */

public class UserActivity extends AppActivity {

    /**
     * tab分类条目
     */
    @BindView(R.id.app_bar)
    Toolbar toolbar;

    @BindView(R.id.user_information_app_bar)
    AppBarLayout appBarLayout;

    @BindView(R.id.user_information_layout)
    RelativeLayout headLayout;

    @BindView(R.id.user_information_background)
    ImageView backgroundImage;

    /**
     * 折叠部分
     */
    @BindView(R.id.user_information_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_information;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);
        //用toolBar替换ActionBar
        setToolBarReplaceActionBar();

        //把title设置到CollapsingToolbarLayout上
        setTitleToCollapsingToolbarLayout();
        Glide.with(this).load(R.drawable.background_login)
                .bitmapTransform(new BlurTransformation(this, 15), new CenterCrop(this))
                .into(backgroundImage);
    }

    @Override
    protected void initData() {

    }

    /**
     * 用toolBar替换ActionBar
     */
    private void setToolBarReplaceActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，
     * 设置到Toolbar上则不会显示
     */
    private void setTitleToCollapsingToolbarLayout() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset <= -headLayout.getHeight() / 2) {
                    collapsingToolbarLayout.setPadding(0,6,0,0);
                    collapsingToolbarLayout.setTitle("潇雨易寒");
                    //使用下面两个CollapsingToolbarLayout的方法设置展开透明->折叠时你想要的颜色
                    collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
                    collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
                } else {
                    collapsingToolbarLayout.setPadding(0,0,0,0);
                    collapsingToolbarLayout.setTitle("");
                }
            }
        });
    }

    @Override
    protected void clearData() {

    }
}
