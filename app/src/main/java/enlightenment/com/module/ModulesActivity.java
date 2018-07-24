package enlightenment.com.module;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import enlightenment.com.base.AppActivity;
import enlightenment.com.base.R;

/***
 * @type 模块modules  学习，创造的模块，还有个人创造的学习栏目
 */
public class ModulesActivity extends AppActivity {

    public static final int MODULES_TYPE_LEARN = 1;       //学习
    public static final int MODULES_TYPE_CREATE = 2;      //创建
    public static final int MODULES_TYPE_LEARN_USER = 5;  //学习模块中个人栏目
    public static final int MODULES_TYPE_CREATE_USER = 6; //创造模块中的个人栏目
    public static final int MODULES_TYPE_LEARN_CHILD = 3; //学习子模块
    public static final int MODULES_TYPE_CREATE_CHILD = 4;//创造子模块

    public static final String MODULES_TYPE_FLAG = "MODULES_TYPE_FLAG";  //学习 or 创建
    public static final String MODULES_FLAG = "MODULES_FLAG";       //父标志
    public static final String MODULES_CHILD_FLAG = "MODULES_CHILD_FLAG"; //子标志
    public static final String MODULES_USER_FLAG = "MODULES_USER_FLAG";  //用户栏目标志

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modules;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void clearData() {

    }

}
