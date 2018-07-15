package enlightenment.com.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import enlightenment.com.base.registered.InterestActivity;
import enlightenment.com.contents.Constants;
import enlightenment.com.tool.device.CheckUtils;
import enlightenment.com.tool.device.SystemState;
import enlightenment.com.view.CircularProgressButton.CircularProgressButton;

/***
 * 登录页面
 */
public class LoginActivity extends AppActivity implements View.OnClickListener, baseView {

    @BindView(R.id.login_top_registered)
    public TextView mRegistered;
    @BindView(R.id.login)
    public CircularProgressButton mLogin;
    @BindView(R.id.login_forget_password)
    public TextView mForgetPassword;
    @BindView(R.id.login_username)
    public EditText mUsername;
    @BindView(R.id.login_password)
    public EditText mPassword;
    @BindView(R.id.login_qq)
    public ImageView mLoginQQ;
    @BindView(R.id.login_sina)
    public ImageView mLoginSina;
    @BindView(R.id.login_wx)
    public ImageView mLoginWx;


    private basePresenter mPresenter;

    private boolean isLogin;

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter = basePresenter.getInstance();
        mPresenter.BindView(this);
        mPresenter.onStart();
        String phone = getSharedPreferences().getString(Constants.Set.SET_USER_NAME, null);
        if (phone != null) {
            mUsername.setText(phone);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        mRegistered.setOnClickListener(this);
        mLogin.setIndeterminateProgressMode(true);
        mLogin.setOnClickListener(this);
        mForgetPassword.setOnClickListener(this);
        mLoginQQ.setOnClickListener(this);
        mLoginSina.setOnClickListener(this);
        mLoginWx.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        isLogin = EnlightenmentApplication.getInstance().getSharedPreferences()
                .getBoolean(Constants.Set.SET_USER_IS, false);
        String phone = EnlightenmentApplication.getInstance().getSharedPreferences()
                .getString(Constants.Set.SET_USER_NAME, null);
        String password = EnlightenmentApplication.getInstance().getSharedPreferences()
                .getString(Constants.Set.SET_USER_PASSWORD, null);
        if (phone == null || password == null)
            isLogin = false;
        if (isLogin) {
            mUsername.setText(phone);
            mPassword.setText(password);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.login_top_registered:
                intent = new Intent(LoginActivity.this, PhoneValidationActivity.class);
                intent.putExtra(PhoneValidationActivity.TYPE_EXTES, PhoneValidationActivity.TYPE_REGISTER);
                startActivity(intent);
                finish();
                break;
            case R.id.login:
                login();
                break;
            case R.id.login_forget_password:
                intent = new Intent(LoginActivity.this, PhoneValidationActivity.class);
                intent.putExtra(PhoneValidationActivity.TYPE_EXTES, PhoneValidationActivity.TYPE_FOTGET);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void login() {
        String phone = mUsername.getText().toString();
        String password = mPassword.getText().toString();
        if (mLogin.getProgress() == -1) {
            mLogin.setProgress(0);
            return;
        }
        if (!CheckUtils.isPhone(phone)) {
            showToast("手机号格式错误，请检查后再进行操作");
            return;
        }
        if (password.equals("")) {
            showToast("请输入密码");
            return;
        }
        if (SystemState.isNetworkState()) {
            mLogin.setProgress(50);
            LoginBean bean = new LoginBean(phone, password);
            mPresenter.executeLogin(bean);
        } else {
            showToast("网络未打开");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unBindView(this);
        mPresenter.onStop();
    }

    @Override
    public void showToast(String message) {
        mLogin.setErrorText(message);
        mLogin.setProgress(-1);
    }

    @Override
    public Object getObj() {
        return null;
    }

    @Override
    public void startNextActivity(Class name) {
        Intent intent = new Intent(LoginActivity.this, name);
        startActivity(intent);
        finish();
    }

    @Override
    public void requestException() {
        showToast("请求不到数据，请检测一下网络信号");
    }


    public static class LoginBean {
        private String username;
        private String password;

        public String getPassword() {
            return password;
        }

        public String getPhone() {
            return username;
        }

        public void setPhone(String phone) {
            this.username = phone;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        private String mode = "1";

        public LoginBean(String phone, String password) {
            this.username = phone;
            this.password = password;
        }
    }
}
