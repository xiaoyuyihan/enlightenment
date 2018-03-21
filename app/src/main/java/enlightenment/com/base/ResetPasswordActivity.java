package enlightenment.com.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;

import butterknife.ButterKnife;
import butterknife.BindView;
import enlightenment.com.contents.Constants;
import enlightenment.com.contents.HttpUrls;
import enlightenment.com.tool.device.SystemState;
import enlightenment.com.tool.okhttp.OkHttpUtils;

/**
 * Created by lw on 2017/8/22.
 * 修改密码
 */

public class ResetPasswordActivity extends AppActivity implements baseView,View.OnClickListener{

    @BindView(R.id.top_left_image)
    public ImageView topLeft;
    @BindView(R.id.top_right_text)
    public TextView topRight;
    @BindView(R.id.top_center_text)
    public TextView topCenter;
    @BindView(R.id.reset)
    public CircularProgressButton reset;
    @BindView(R.id.reset_password)
    public TextInputEditText inputPassword;
    @BindView(R.id.reset_password_next)
    public TextInputEditText InputPasswordNext;

    private basePresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
        topLeft.setOnClickListener(this);
        topCenter.setText("重置密码");
        topRight.setTextColor(getResources().getColor(R.color.mainTopColor));
        reset.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter=basePresenter.getInstance();
        mPresenter.BindView(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.reset:
                if (reset.getProgress() == -1) {
                    reset.setProgress(0);
                    return;
                }
                if (!SystemState.isNetworkState()){
                    Toast.makeText(this,"无网络连接",Toast.LENGTH_SHORT).show();
                }
                reset.setProgress(50);
                String password=inputPassword.getText().toString();
                if (password.equals(InputPasswordNext.getText().toString())){
                    mPresenter.resetPassword(new LoginActivity.LoginBean(Constants.phoneCode,password));
                }else
                    showToast("两次密码不一致");
                break;
            case R.id.top_left_image:
                startNextActivity(PhoneValidationActivity.class);
                break;
        }

    }

    @Override
    public void showToast(String message) {
        reset.setErrorText(message);
        reset.setProgress(-1);
    }

    @Override
    public void startNextActivity(Class name) {
        Intent intent = new Intent(this, name);
        intent.putExtra(PhoneValidationActivity.TYPE_EXTES,PhoneValidationActivity.TYPE_FOTGET);
        startActivity(intent);
        finish();
    }

    @Override
    public void requestException() {
        showToast("请求不到数据，请检测一下网络信号");
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unBindView(this);
    }
}
