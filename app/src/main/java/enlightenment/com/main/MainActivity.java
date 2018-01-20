package enlightenment.com.main;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import enlightenment.com.base.AppActivity;
import enlightenment.com.base.EnlightenmentApplication;
import enlightenment.com.base.R;

/**
 * Created by lw on 2017/7/26.
 */

public class MainActivity extends AppActivity implements View.OnClickListener {

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private Fragment mDefaultFragment;
    private ImageView mHome;
    private ImageView mFound;
    private ImageView mMessage;
    private ImageView mMyself;
    private ImageView mMental;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        // 设置默认的Fragment
        setDefaultFragment();
    }

    private void init() {
        mHome = (ImageView) findViewById(R.id.image_main_home);
        mHome.setOnClickListener(this);
        mFound = (ImageView) findViewById(R.id.image_main_found);
        mFound.setOnClickListener(this);
        mMessage = (ImageView) findViewById(R.id.image_main_message);
        mMessage.setOnClickListener(this);
        mMyself = (ImageView) findViewById(R.id.image_main_myself);
        mMyself.setOnClickListener(this);
    }

    private void setDefaultFragment() {
        fragmentManager = getSupportFragmentManager();
        mDefaultFragment = HomeFragment.getInstanceFragment();
        transaction = fragmentManager.beginTransaction();//开启一个事务
        transaction.add(R.id.fragment_main, mDefaultFragment);
        transaction.commit();
    }

    public void setMainFragment(Fragment mainFragment) {
        if (mainFragment != null) {
            transaction = fragmentManager.beginTransaction();//开启一个事务
            if (!mainFragment.isAdded()){
                transaction.hide(mDefaultFragment).add(R.id.fragment_main,mainFragment).commit();
            }else
                transaction.hide(mDefaultFragment).show(mainFragment).commit();
            mDefaultFragment=mainFragment;
        }
    }

    @Override
    public void onClick(View v) {
        Fragment nextFragment = null;
        switch (v.getId()) {
            case R.id.image_main_home:
                nextFragment = HomeFragment.getInstanceFragment();
                break;
            case R.id.image_main_found:
                nextFragment = FoundFragment.getInstanceFragment();
                break;
            case R.id.image_main_message:
                nextFragment = MessageFragment.getInstanceFragment();
                break;
            case R.id.image_main_myself:
                nextFragment = MyselfFragment.getInstanceFragment();
                break;
        }
        setMainFragment(nextFragment);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                moveTaskToBack(true);
                break;
        }
        return false;
    }
}
