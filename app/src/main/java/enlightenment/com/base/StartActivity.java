package enlightenment.com.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.BindView;
import enlightenment.com.contents.Constants;
import enlightenment.com.main.MainActivity;
import enlightenment.com.service.SerMessageService;

/**
 * Created by admin on 2017/7/20.
 * 启动动画
 */

public class StartActivity extends AppActivity {

    private Handler mHandler;
    @BindView(R.id.start_time)
    public TextView mStartView;

    private boolean isLogin;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_start;
    }

    @Override
    protected void initData() {
        isLogin = EnlightenmentApplication.getInstance().getSharedPreferences()
                .getBoolean(Constants.Set.SET_USER_IS, false);
        /*String phone = EnlightenmentApplication.getInstance().getSharedPreferences()
                .getString(Constants.Set.SET_USER_NAME, null);
        String password = EnlightenmentApplication.getInstance().getSharedPreferences()
                .getString(Constants.Set.SET_USER_PASSWORD, null);
        if (phone == null || password == null)
            isLogin = false;
        if (isLogin) {
            Intent intent = new Intent(this, SerMessageService.class);
            intent.putExtra(SerMessageService.SERVICE_DATA_EXTRA, SerMessageService.ACTION_DETECT_REQUEST_TOKEN);
            intent.putExtra(SerMessageService.SERVICE_REQUEST_TOKEN_PHONE, phone);
            intent.putExtra(SerMessageService.SERVICE_REQUEST_TOKEN_PASSWORD, password);
            startService(intent);
        }*/
    }

    protected void init() {
        ButterKnife.bind(this);
        startMessageService();
        mStartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity();
                mHandler.removeMessages(1);
            }
        });
        mHandler = new Handler() {
            int time = 4;

            @Override
            public void handleMessage(Message msg) {
                time--;
                if (time < 0) {
                    startActivity();
                } else {
                    mStartView.setText(time + 1 + "秒后");
                    mHandler.sendEmptyMessageDelayed(1, 1000);
                }
            }
        };
        mHandler.sendEmptyMessage(1);
    }

    private void startMessageService() {
        Intent intent = new Intent(this, SerMessageService.class);
        intent.putExtra(SerMessageService.SERVICE_DATA_EXTRA, SerMessageService.ACTION_DETECT_MODULE_NEW);
        startService(intent);
    }

    private void startActivity() {
        Intent intent;
        if (isLogin) {
            intent = new Intent(StartActivity.this, MainActivity.class);
        } else
            intent = new Intent(StartActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
