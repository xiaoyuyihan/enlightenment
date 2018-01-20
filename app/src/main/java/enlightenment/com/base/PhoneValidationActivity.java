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
import butterknife.InjectView;
import enlightenment.com.contents.Constants;

/**
 * Created by lw on 2017/7/20.
 */

public class PhoneValidationActivity extends AppActivity implements  baseView,View.OnClickListener{
    private static final int TIME_RETURN=1;
    public static final int TYPE_REGISTER=2;
    public static final int TYPE_FOTGET=3;
    public static final String TYPE_EXTES="TYPE";

    @InjectView(R.id.registered_obtain)
    public TextView mObtain;
    @InjectView(R.id.registered_phone)
    public EditText mPhone;
    @InjectView(R.id.registered_number)
    public EditText mNumber;
    @InjectView(R.id.top_right_text)
    public TextView topRightText;
    @InjectView(R.id.top_center_text)
    public TextView topCenterText;
    @InjectView(R.id.top_left_image)
    public ImageView topLeftImage;

    private basePresenter mPresenter;
    public int activiyType;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_vaildation);
        ButterKnife.inject(this);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter = basePresenter.getInstance();
        mPresenter.BindView(this);
    }
    private void init(){
        activiyType=getIntent().getExtras().getInt(PhoneValidationActivity.TYPE_EXTES);
        topRightText.setTextColor(getResources().getColor(R.color.mainTopColor));
        if (activiyType==TYPE_REGISTER)
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
    public AppCompatActivity getMainActivity() {
        return PhoneValidationActivity.this;
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
    public void startNextActivity(Class name) {
        Intent intent = new Intent(PhoneValidationActivity.this, name);
        startActivity(intent);
        finish();
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
                mPresenter.sendPhoneCode(Constants.phoneCode);
                mObtain.setClickable(false);
                break;
        }
    }
}