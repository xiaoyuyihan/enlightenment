package enlightenment.com.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.BindView;
import enlightenment.com.contents.Constants;
import enlightenment.com.contents.HttpUrls;
import enlightenment.com.main.MainActivity;
import enlightenment.com.service.DownloadService;
import enlightenment.com.tool.okhttp.ModelUtil;
import enlightenment.com.tool.gson.TransformationUtils;
import okhttp3.Call;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        startMessageService();
        init();
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

    /***
     * 每次获取token
     */
    private void init() {
        isLogin=EnlightenmentApplication.getInstance().getSharedPreferences()
                .getBoolean(Constants.Set.SET_USER_IS, false);
        String phone=EnlightenmentApplication.getInstance().getSharedPreferences()
                .getString(Constants.Set.SET_USER_NAME,null);
        String password=EnlightenmentApplication.getInstance().getSharedPreferences()
                .getString(Constants.Set.SET_USER_PASSWORD,null);
        if (phone==null||password==null)
            isLogin=false;
        if (isLogin){
            Intent intent=new Intent(this, DownloadService.class);
            intent.putExtra(DownloadService.SERVICE_DATA_EXTRA, DownloadService.ACTION_DETECT_REQUEST_TOKEN);
            intent.putExtra(DownloadService.SERVICE_REQUEST_TOKEN_PHONE,phone);
            intent.putExtra(DownloadService.SERVICE_REQUEST_TOKEN_PASSWORD,password);
            startService(intent);
        }
    }

    private void startMessageService() {
        Intent intent=new Intent(this, DownloadService.class);
        intent.putExtra(DownloadService.SERVICE_DATA_EXTRA, DownloadService.ACTION_DETECT_MODULE_NEW);
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
