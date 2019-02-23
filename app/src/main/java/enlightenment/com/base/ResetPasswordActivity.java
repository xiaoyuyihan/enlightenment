package enlightenment.com.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.BindView;
import enlightenment.com.contents.Constants;
import enlightenment.com.contents.HttpUrls;
import enlightenment.com.tool.device.CheckUtils;
import enlightenment.com.tool.device.SystemState;
import enlightenment.com.tool.okhttp.OkHttpUtils;
import enlightenment.com.view.CircularProgressButton.CircularProgressButton;

/**
 * Created by lw on 2017/8/22.
 * 修改密码
 */

public class ResetPasswordActivity extends AppActivity implements baseView, View.OnClickListener {

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
    @BindView(R.id.reset_password_hint)
    public TextView inputPasswordHint;
    @BindView(R.id.reset_password_next)
    public TextInputEditText InputPasswordNext;

    private basePresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reset_password;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);
        topLeft.setOnClickListener(this);
        topCenter.setText("重置密码");
        topRight.setTextColor(getResources().getColor(R.color.mainTopColor));
        reset.setOnClickListener(this);
        inputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String info = CheckUtils.checkPassword(charSequence.toString());
                if (info.equals("弱")) {
                    inputPasswordHint.setTextColor(getResources().getColor(R.color.cpb_red));
                } else if (info.equals("中")) {
                    inputPasswordHint.setTextColor(getResources().getColor(R.color.colorAccent));
                } else {
                    inputPasswordHint.setTextColor(getResources().getColor(R.color.colorLogin));
                }
                inputPasswordHint.setText("密码强度：" + info);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    protected void initData() {
        mPresenter = basePresenter.getInstance();
        mPresenter.BindView(this);
        mPresenter.onStart();
    }

    @Override
    protected void clearData() {
        mPresenter.onStop();
        mPresenter.unBindView(this);
        mPresenter = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reset:
                if (reset.getProgress() == -1) {
                    reset.setProgress(0);
                    return;
                }
                if (!SystemState.isNetworkState()) {
                    Toast.makeText(this, "无网络连接", Toast.LENGTH_SHORT).show();
                }
                reset.setProgress(50);
                String password = inputPassword.getText().toString();
                if (password.length() < 6) {
                    showToast("最小密码长度六位");
                } else if (password.equals(InputPasswordNext.getText().toString())) {
                    mPresenter.resetPassword(new LoginActivity.LoginBean(Constants.phoneCode,
                            password,getSetSharedPreferences(Constants.Set.SET_SYSTEM_UUID,"")));
                } else
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
    public Object getObj() {
        return null;
    }

    @Override
    public void startNextActivity(Class name) {
        Intent intent = new Intent(this, name);
        intent.putExtra(PhoneValidationActivity.TYPE_EXTES, PhoneValidationActivity.TYPE_FOTGET);
        startActivity(intent);
        finish();
    }

    @Override
    public void requestException() {
        showToast("提交失败，请再尝试一下");
    }

    @Override
    public Context getContext() {
        return this;
    }

}
