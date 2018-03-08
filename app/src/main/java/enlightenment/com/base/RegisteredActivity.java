package enlightenment.com.base;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dd.CircularProgressButton;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.BindView;
import enlightenment.com.contents.Constants;
import enlightenment.com.contents.FileUrls;
import enlightenment.com.main.MainActivity;
import enlightenment.com.resources.AlbumActivity;
import enlightenment.com.resources.PictureDisposalActivity;
import enlightenment.com.tool.File.FileUtils;
import enlightenment.com.tool.device.KeyboardChangeListener;
import enlightenment.com.tool.device.SystemState;
import enlightenment.com.view.HeadPortraitWinPopupWindow;

/**
 * Created by lw on 2017/7/24.
 * 注册详细信息填写
 */

public class RegisteredActivity extends AppActivity implements baseView, View.OnClickListener {

    public static final int START_SELECT_PHOTO = 0;       //选择
    public static final int START_SHOOT_PHOTO = 1;// 拍照
    public static final int START_COMPLETE_PHOTO = 2;
    public static final String START_PHOTO_URL = "register_url";

    private static final String USER="USER";
    private static final String PASSWORD="PASSWORD";
    private static final String IMAGE_URL="image_url";
    private static final String PASSWORD_NEXT="PASSWORD_NEXT";

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
    private String clipPhotoURL="";

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);
        ButterKnife.bind(this);
        mRegistered.setOnClickListener(this);
        mUserImage.setOnClickListener(this);

        /*KeyboardChangeListener m = new KeyboardChangeListener(this);
        m.setKeyBoardListener(new KeyboardChangeListener.KeyBoardListener() {
            @Override
            public void onKeyboardChange(boolean isShow, int keyboardHeight) {
                if (isShow) {
                    mUserImage.setMaxWidth(64);
                    mUserImage.setMaxHeight(64);
                } else {
                    mUserImage.setMaxWidth(80);
                    mUserImage.setMaxHeight(80);
                }
            }
        });*/
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        if (savedInstanceState != null) {
            clipPhotoURL=savedInstanceState.getString(IMAGE_URL);
            if (clipPhotoURL!=null&&!clipPhotoURL.equals("")){
                Glide.with(this).load(new File(clipPhotoURL))
                        .into(mUserImage);
            }
            mUsername.setText(savedInstanceState.getString(USER));
            mPassword.setText(savedInstanceState.getString(PASSWORD));
            mPassword.setText(savedInstanceState.getString(PASSWORD_NEXT));
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //状态 判断

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter = basePresenter.getInstance();
        mPresenter.BindView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unBindView(this);
    }

    @Override
    public void showToast(String message) {
        mRegistered.setErrorText(message);
        mRegistered.setProgress(-1);
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
        } else if (requestCode == START_SELECT_PHOTO) {
            startClip(data.getExtras()
                    .getString(RegisteredActivity.START_PHOTO_URL));
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
                        mUsername.getText().toString(), "1", "1",
                        EnlightenmentApplication.getInstance().getModules());
                mPresenter.submitInfo(clipPhotoURL, registeredBean);
            } else
                showToast("网络未打开");
        } else
            showToast("两次密码输入不一致");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(IMAGE_URL,clipPhotoURL);
        outState.putString(USER,mUsername.getText().toString());
        outState.putString(PASSWORD,mPassword.getText().toString());
        outState.putString(PASSWORD_NEXT,mPasswordNext.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    public class RegisteredBean {
        private String phone;
        private String password;
        private String username;
        private String sourceint;
        private String status;
        private String interest;

        public RegisteredBean(String phone, String password, String username, String sourceint,
                              String status, String interest) {
            this.phone = phone;
            this.password = password;
            this.username = username;
            this.sourceint = sourceint;
            this.status = status;
            this.interest = interest;
        }
    }
}
