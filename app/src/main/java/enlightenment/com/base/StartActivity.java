package enlightenment.com.base;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.apache.http.conn.scheme.HostNameResolver;

import butterknife.ButterKnife;
import butterknife.InjectView;
import enlightenment.com.contents.Constants;
import enlightenment.com.main.MainActivity;
import enlightenment.com.service.MessageService;

/**
 * Created by lw on 2017/7/20.
 */

public class StartActivity extends AppActivity {

    private Handler mHandler;
    @InjectView(R.id.start_time)
    public TextView mStartView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.inject(this);
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
        Intent intent=new Intent(this, MessageService.class);
        intent.putExtra(MessageService.SERVICE_DATA_EXTRA,MessageService.ACTION_DETECT_MODULE_NEW);
        startService(intent);
    }

    private void startActivity() {
        Intent intent;
        if (EnlightenmentApplication.getInstance().getSharedPerferences().getBoolean(Constants.SHARED_IS_LOGIN, false)) {
            intent = new Intent(StartActivity.this, MainActivity.class);
        } else
            intent = new Intent(StartActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
