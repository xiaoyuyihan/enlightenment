package enlightenment.com.base;

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

import butterknife.ButterKnife;
import butterknife.BindView;
import enlightenment.com.contents.Constants;
import enlightenment.com.tool.device.CheckUtils;

/**
 * Created by lw on 2017/7/20.
 * 验证码获取
 */

public class PhoneValidationActivity extends AppActivity implements  baseView,View.OnClickListener{
    private static final int TIME_RETURN=1;
    public static final int TYPE_REGISTER=2;
    public static final int TYPE_FOTGET=3;
    public static final String TYPE_EXTES="TYPE";

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
    public int activityType;

    public Handler mHandler= new Handler(Looper.getMainLooper()){
        private int time=60;
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case TIME_RETURN:
                    time--;
                    if(time<=0){
                        time=60;
                        mObtain.setClickable(true);
                        mObtain.setText("获取验证码");
                    }else {
                        mObtain.setText(time+"秒后");
                        mHandler.sendEmptyMessageDelayed(TIME_RETURN,1000);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter = basePresenter.getInstance();
        mPresenter.onStart();
        mPresenter.BindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_phone_vaildation;
    }

    @Override
    protected void init(){
        ButterKnife.bind(this);
        activityType=getIntent().getExtras().getInt(PhoneValidationActivity.TYPE_EXTES);
        topRightText.setTextColor(getResources().getColor(R.color.mainTopColor));
        if (activityType==TYPE_REGISTER)
            topCenterText.setText("注册帐号");
        else
            topCenterText.setText("找回密码");
        topLeftImage.setOnClickListener(this);
        mNumber.addTextChangedListener(new TextWatcher() {
            //输入文本之前的状态
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //输入文字中的状态，count是一次性输入字符数
                if (charSequence.length()==6){
                    mPresenter.equalsCode(mPhone.getText().toString(),charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mObtain.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unBindView(this);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(PhoneValidationActivity.this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public Object getObj() {
        return null;
    }

    @Override
    public void startNextActivity(Class name) {
        Intent intent = new Intent(PhoneValidationActivity.this, name);
        startActivity(intent);
        finish();
    }

    @Override
    public void requestException() {
        showToast("请求不到数据，请检测一下网络信号");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.top_left_image:
                startNextActivity(LoginActivity.class);
                break;
            case R.id.registered_obtain:
                mHandler.sendEmptyMessage(TIME_RETURN);
                Constants.phoneCode=mPhone.getText().toString();
                if (CheckUtils.isPhone(Constants.phoneCode)){
                    mPresenter.sendPhoneCode(Constants.phoneCode);
                    mObtain.setClickable(false);
                }else {
                    showToast("手机格式错误");
                }
                break;
        }
    }
}
