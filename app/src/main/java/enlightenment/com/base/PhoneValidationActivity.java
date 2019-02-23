package enlightenment.com.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.provider.view.LoadingDialog;

import butterknife.ButterKnife;
import butterknife.BindView;
import enlightenment.com.contents.Constants;
import enlightenment.com.tool.device.CheckUtils;

/**
 * Created by lw on 2017/7/20.
 * 验证码获取
 */

public class PhoneValidationActivity extends AppActivity implements PhoneValidationView,
        View.OnClickListener, TextWatcher {
    private static final int HANDLER_TIME_RETURN = 1;

    public static final int TYPE_REGISTER = 2;
    public static final int TYPE_FOTGET = 3;
    public static final int TYPE_UUID = 4;
    public static final String TYPE_EXTES = "TYPE";
    public static final String UUID_PHONE_EXTE = "UUID_PHONE_EXTE";

    @BindView(R.id.registered_obtain)
    public TextView mObtain;
    @BindView(R.id.registered_phone)
    public EditText mPhone;
    @BindView(R.id.registered_number)
    public EditText mNumber;
    @BindView(R.id.top_right_text)
    public TextView topRightText;
    @BindView(R.id.top_center_text)
    public TextView topCenterText;
    @BindView(R.id.top_left_image)
    public ImageView topLeftImage;

    private basePresenter mPresenter;
    public int type;
    public String phone;

    public Handler mHandler;

    public LoadingDialog loadingDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_phone_vaildation;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);
        type = getIntent().getExtras().getInt(PhoneValidationActivity.TYPE_EXTES);
        if (type == TYPE_REGISTER) {
            topCenterText.setText("注册帐号");
            topRightText.setTextColor(getResources().getColor(R.color.mainTopColor));
        } else if (type == TYPE_FOTGET) {
            topCenterText.setText("找回密码");
            topRightText.setTextColor(getResources().getColor(R.color.mainTopColor));
        } else if (type == TYPE_UUID) {
            topCenterText.setText("更改设备号");
            phone = getIntent().getExtras().getString(PhoneValidationActivity.UUID_PHONE_EXTE);
            mPhone.setVisibility(View.GONE);
            topRightText.setText("更改");
            topRightText.setOnClickListener(this);
        }
        mNumber.addTextChangedListener(this);
        topLeftImage.setOnClickListener(this);
        mObtain.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mPresenter = basePresenter.getInstance();
        mPresenter.BindView(this);
        mPresenter.onStart();
        mHandler = new Handler(Looper.getMainLooper()) {
            private int time = 60;

            @Override
            public void handleMessage(Message msg) {

                switch (msg.what) {
                    case HANDLER_TIME_RETURN:
                        time--;
                        if (time <= 0) {
                            time = 60;
                            mObtain.setClickable(true);
                            mObtain.setText("获取验证码");
                        } else {
                            mObtain.setText(time + "秒后");
                            mHandler.sendEmptyMessageDelayed(HANDLER_TIME_RETURN, 1000);
                        }
                        break;
                }
            }
        };
        if (type == TYPE_UUID)
            mObtain.performClick();
    }

    @Override
    protected void clearData() {
        mHandler.removeMessages(HANDLER_TIME_RETURN);
        mHandler = null;
        mPresenter.onStop();
        mPresenter.unBindView(this);
        mPresenter = null;
    }

    @Override
    public void showToast(String message) {
        loadingDialog.dismiss();
        loadingDialog = null;
        Toast.makeText(PhoneValidationActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Object getObj() {
        return null;
    }

    @Override
    public void startNextActivity(Class name) {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
        Intent intent = new Intent(PhoneValidationActivity.this, name);
        startActivity(intent);
        finish();
    }

    @Override
    public void requestException() {
        showToast("请求不到数据，请检测一下网络信号");
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_left_image:
                startNextActivity(LoginActivity.class);
                break;
            case R.id.registered_obtain:
                if (type == TYPE_UUID)
                    Constants.phoneCode = phone;
                else
                    Constants.phoneCode = mPhone.getText().toString();
                mHandler.sendEmptyMessage(HANDLER_TIME_RETURN);
                if (CheckUtils.isPhone(Constants.phoneCode)) {
                    mPresenter.sendPhoneCode(Constants.phoneCode);
                    mObtain.setClickable(false);
                } else {
                    showToast("手机格式错误");
                }
                break;
            case R.id.top_right_text:
                tryNextLogin();
                break;
        }
    }

    @Override
    public int getViewType() {
        return type;
    }

    @Override
    public void UUIDCheck() {
        String UUID = java.util.UUID.randomUUID().toString().replace("-", "");
        setSetSharedPreferences(Constants.Set.SET_SYSTEM_UUID, UUID);
        mPresenter.replaceUUID(new UUIDBean(phone, UUID));
    }

    @Override
    public void tryNextLogin() {
        String password = getSetSharedPreferences(Constants.Set.SET_USER_PASSWORD, null);
        LoginActivity.LoginBean bean = new LoginActivity.LoginBean(phone, password,
                getSetSharedPreferences(Constants.Set.SET_SYSTEM_UUID, ""));
        mPresenter.executeLogin(bean);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //输入文字中的状态，count是一次性输入字符数
        if (charSequence.length() == 6) {
            mPresenter.equalsCode(mPhone.getText().toString(), charSequence.toString());
            if (type == TYPE_UUID) {
                loadingDialog = new LoadingDialog();
                Bundle bundle = new Bundle();
                bundle.putString(LoadingDialog.DIALOG_LOAD_NAME, "账户检测中···");
                loadingDialog.setArguments(bundle);
                loadingDialog.show(getSupportFragmentManager(), "LoadingDialog");
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public class UUIDBean {
        private String username;
        private String UUID;

        public UUIDBean(String phone, String UUID) {
            this.username = phone;
            this.UUID = UUID;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUUID() {
            return UUID;
        }

        public void setUUID(String UUID) {
            this.UUID = UUID;
        }
    }
}
