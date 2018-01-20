package enlightenment.com.base;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dd.CircularProgressButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import enlightenment.com.contents.Constants;
import enlightenment.com.main.MainActivity;
import enlightenment.com.tool.device.SystemState;

public class LoginActivity extends AppActivity implements View.OnClickListener, baseView {

    @InjectView(R.id.login_top_registered)
    public TextView mRegistered;
    @InjectView(R.id.login)
    public CircularProgressButton mLogin;
    @InjectView(R.id.login_forget_password)
    public TextView mForgetPassword;
    @InjectView(R.id.login_username)
    public EditText mUsername;
    @InjectView(R.id.login_password)
    public EditText mPassword;
    @InjectView(R.id.login_qq)
    public ImageView mLoginQQ;
    @InjectView(R.id.login_sina)
    public ImageView mLoginSina;
    @InjectView(R.id.login_wx)
    public ImageView mLoginWx;


    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private basePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter = basePresenter.getInstance();
        mPresenter.BindView(this);
        mPresenter.onStart();
        String phone = getSharedPerferences().getString(Constants.SHARED_PHONE, null);
        if (phone != null) {
            mUsername.setText(phone);
        }
    }

    private void init() {
        mRegistered.setOnClickListener(this);
        mLogin.setIndeterminateProgressMode(true);
        mLogin.setOnClickListener(this);
        mForgetPassword.setOnClickListener(this);
        mLoginQQ.setOnClickListener(this);
        mLoginSina.setOnClickListener(this);
        mLoginWx.setOnClickListener(this);
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
        if (mLogin.getProgress() == -1) {
            mLogin.setProgress(0);
            return;
        }
        if (SystemState.isNetworkState()) {
            mLogin.setProgress(50);
            mPresenter.executeLogin(new LoginBean(mUsername.getText().toString(),
                    mPassword.getText().toString()));
        } else {
            showToast("网络未打开");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unBindView(this);
    }

    @Override
    public AppCompatActivity getMainActivity() {
        return LoginActivity.this;
    }

    @Override
    public void showToast(String message) {
        mLogin.setErrorText(message);
        mLogin.setProgress(-1);
    }

    @Override
    public void startNextActivity(Class name) {
        Intent intent = new Intent(LoginActivity.this, name);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public static class LoginBean{
        private String phone;
        private String password;

        public String getPassword() {
            return password;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        private String mode="1";
        public LoginBean(String phone,String password){
            this.phone=phone;
            this.password=password;
        }
    }
}
