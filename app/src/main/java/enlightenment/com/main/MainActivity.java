package enlightenment.com.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationManagerCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.utils.MessageDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enlightenment.com.base.AppActivity;
import enlightenment.com.base.EnlightenmentApplication;
import enlightenment.com.base.R;
import enlightenment.com.main.found.FoundFragment;
import enlightenment.com.main.home.HomeFragment;
import enlightenment.com.main.message.MessageFragment;
import enlightenment.com.main.myself.MyselfFragment;
import enlightenment.com.tool.device.CheckUtils;
import enlightenment.com.user.SettingActivity;

/**
 * Created by lw on 2017/7/26.
 */

public class MainActivity extends AppActivity implements View.OnClickListener,
        MessageDialogFragment.OnMsgDialogClickListener {

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private Fragment mDefaultFragment;
    @BindView(R.id.image_main_home)
    ImageView mHome;
    @BindView(R.id.image_main_found)
    ImageView mFound;
    @BindView(R.id.image_main_message)
    ImageView mMessage;
    @BindView(R.id.image_main_myself)
    ImageView mMyself;

    MessageDialogFragment messageDialogFragment;

    private static int ACTIVITY_IALOG_FLAG = 1;

    /**
     * 检测通知是否开启
     */
    private void checkNotification() {
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        if (!manager.areNotificationsEnabled()) {
            messageDialogFragment = new MessageDialogFragment();
            messageDialogFragment.setMessage("我们找不到通知你的方式，请给我们开启访问通知对权限");
            messageDialogFragment.setMessageImageVisibility(View.GONE);
            messageDialogFragment.setOnMsgDialogClickListener(this);
            messageDialogFragment.show(getSupportFragmentManager(), "MessageDialogFragment");
        }

        showCustomToast("通知开启：" + manager.areNotificationsEnabled());
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        setDefaultFragment();
        checkNotification();
        showCustomToast("悬浮窗开启：" + CheckUtils.checkFloatPermission(this));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        ((EnlightenmentApplication) getApplication()).updateDefaultNightMode();
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
            if (!mainFragment.isAdded()) {
                transaction.hide(mDefaultFragment).add(R.id.fragment_main, mainFragment).commit();
            } else
                transaction.hide(mDefaultFragment).show(mainFragment).commit();
            mDefaultFragment = mainFragment;
        }
    }

    @Override
    @OnClick({R.id.image_main_myself, R.id.image_main_home, R.id.image_main_found, R.id.image_main_message})
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
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                moveTaskToBack(true);
                break;
        }
        return false;
    }

    @Override
    public void onSureBut() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, ACTIVITY_IALOG_FLAG);
        if (messageDialogFragment != null)
            messageDialogFragment.dismiss();
        messageDialogFragment = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_IALOG_FLAG) {
            NotificationManagerCompat manager = NotificationManagerCompat.from(this);
            if (manager.areNotificationsEnabled()) {
                showCustomToast("没有打开通知，我们暂时无法通知你");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onCancel() {
        messageDialogFragment.dismiss();
    }
}
