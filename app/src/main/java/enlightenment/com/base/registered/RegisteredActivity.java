package enlightenment.com.base.registered;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.BindView;
import enlightenment.com.base.AppActivity;
import enlightenment.com.base.EnlightenmentApplication;
import enlightenment.com.base.R;
import enlightenment.com.base.basePresenter;
import enlightenment.com.base.baseView;
import enlightenment.com.contents.Constants;
import enlightenment.com.contents.FileUrls;
import enlightenment.com.resources.AlbumActivity;
import enlightenment.com.resources.PictureDisposalActivity;
import enlightenment.com.tool.File.FileUtils;
import enlightenment.com.tool.Imagecompression.ImageCompression;
import enlightenment.com.tool.device.SystemState;
import enlightenment.com.view.CircularProgressButton.CircularProgressButton;
import enlightenment.com.view.PopupWindow.HeadPortraitWinPopupWindow;

/**
 * Created by lw on 2017/7/24.
 * 注册详细信息填写
 */

public class RegisteredActivity extends AppActivity implements baseView, View.OnClickListener {

    public static final int START_SELECT_PHOTO = 0;       //选择
    public static final int START_SHOOT_PHOTO = 1;// 拍照
    public static final int START_COMPLETE_PHOTO = 2;
    public static final String START_PHOTO_URL = "register_url";

    private static final String USER = "USER";
    private static final String PASSWORD = "PASSWORD";
    private static final String IMAGE_URL = "image_url";
    private static final String PASSWORD_NEXT = "PASSWORD_NEXT";

    @BindView(R.id.register_user_image)
    ImageView mUserImage;
    @BindView(R.id.registered_password)
    TextInputEditText mPassword;
    @BindView(R.id.registered_password_next)
    TextInputEditText mPasswordNext;
    @BindView(R.id.registered_username)
    TextInputEditText mUsername;
    @BindView(R.id.registered_password_layout)
    TextInputLayout mPasswordLayout;
    @BindView(R.id.registered)
    CircularProgressButton mRegistered;
    @BindView(R.id.registered_info)
    TextView mInfo;
    basePresenter mPresenter;
    private HeadPortraitWinPopupWindow mPopupWindow;

    private String photoURL = "";
    private String clipPhotoURL = "";
    private String UUID;
    private String source="1";
    private String status ="1";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_registered;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);
        mRegistered.setOnClickListener(this);
        mUserImage.setOnClickListener(this);
        UUID = java.util.UUID.randomUUID().toString().replace("-","");
        setSetSharedPreferences(Constants.Set.SET_SYSTEM_UUID,UUID);
        onRestoreState(savedInstanceState);
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
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        onRestoreState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        onRestoreState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void onRestoreState(Bundle inState) {
        if (inState == null)
            return;
        String ImageUrl = inState.getString(IMAGE_URL, null);
        if (ImageUrl != null) {
            Glide.with(this).load(new File(clipPhotoURL))
                    .into(mUserImage);
        }
        String username = inState.getString(USER, null);
        if (username != null) {
            mUsername.setText(username);
        }
        String password = inState.getString(PASSWORD, null);
        if (password != null)
            mPassword.setText(password);
        String passwordNext = inState.getString(PASSWORD_NEXT, null);
        if (passwordNext != null)
            mPasswordNext.setText(passwordNext);
    }

    @Override
    public void showToast(String message) {
        mRegistered.setErrorText(message);
        mRegistered.setProgress(-1);
    }

    @Override
    public Object getObj() {
        return null;
    }

    @Override
    public void startNextActivity(Class name) {
        Intent intent = new Intent(this, name);
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
            case R.id.registered:
                Registered();
                break;
            case R.id.register_user_image:
                showBottomView();
                break;
            case R.id.window_head_portrait_shooting:
                startShooting();
                break;
            case R.id.window_head_portrait_select:
                Intent intent = new Intent(this, AlbumActivity.class);
                startActivityForResult(intent, RegisteredActivity.START_SELECT_PHOTO);
                mPopupWindow.dismiss();
                break;
            case R.id.window_head_portrait_back:
                mPopupWindow.dismiss();
                break;
        }
    }

    @SuppressLint("WrongConstant")
    private void startShooting() {
        // 指定图片存储地址
        photoURL = FileUrls.PATH_PHOTO + System.currentTimeMillis() + ".jpg";
        if (!FileUtils.isFileNews(photoURL)) {
            Toast.makeText(this, "文件创建失败", Toast.LENGTH_SHORT).show();
            return;
        }
        // 启动相机
        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(photoURL)));
        startActivityForResult(intent1, RegisteredActivity.START_SHOOT_PHOTO);
        mPopupWindow.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == START_COMPLETE_PHOTO && data != null) {
            clipPhotoURL = data.getExtras()
                    .getString(RegisteredActivity.START_PHOTO_URL);
            Glide.with(this).load(new File(clipPhotoURL))
                    .into(mUserImage);
        } else if (requestCode == START_SELECT_PHOTO && data != null) {
            String url = data.getExtras().getString(RegisteredActivity.START_PHOTO_URL);
            if (url != null && com.utils.FileUtils.isFile(url))
                startClip(url);
        } else if (requestCode == START_SHOOT_PHOTO) {
            startClip(photoURL);
        }
    }

    private void startClip(String url) {
        Intent intent = new Intent(this, PictureDisposalActivity.class);
        intent.putExtra(RegisteredActivity.START_PHOTO_URL, url);
        startActivityForResult(intent, RegisteredActivity.START_COMPLETE_PHOTO);
    }

    private void showBottomView() {
        mPopupWindow = new HeadPortraitWinPopupWindow(this, this);
        //设置layout在PopupWindow中显示的位置
        mPopupWindow.showAtLocation(findViewById(R.id.registered_layout),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 注册
     */
    private void Registered() {
        if (mRegistered.getProgress() == -1) {
            mRegistered.setProgress(0);
            return;
        }
        String password = mPassword.getText().toString();
        if (password.equals(mPasswordNext.getText().toString())) {
            if (SystemState.isNetworkState()) {
                mRegistered.setProgress(50);
                RegisteredBean registeredBean = new RegisteredBean(Constants.phoneCode, password,
                        mUsername.getText().toString(), source, status,
                        EnlightenmentApplication.getInstance().getModules(),UUID);
                mPresenter.submitInfo(clipPhotoURL, registeredBean);
            } else
                showToast("网络未打开");
        } else
            showToast("两次密码输入不一致");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(IMAGE_URL, clipPhotoURL);
        outState.putString(USER, mUsername.getText().toString());
        outState.putString(PASSWORD, mPassword.getText().toString());
        outState.putString(PASSWORD_NEXT, mPasswordNext.getText().toString());
        super.onSaveInstanceState(outState);
    }

    public class RegisteredBean {
        private String phone;
        private String password;
        private String username;
        private String source;
        private String status;
        private String interest;
        private String avatar;
        private String uuid;
        private String address="";

        public RegisteredBean(String phone, String password, String username, String source,
                              String status, String interest,String uuid) {
            this.phone = phone;
            this.password = password;
            this.username = username;
            this.source = source;
            this.status = status;
            this.interest = interest;
            this.uuid = uuid;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getInterest() {
            return interest;
        }

        public void setInterest(String interest) {
            this.interest = interest;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
