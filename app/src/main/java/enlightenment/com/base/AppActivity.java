package enlightenment.com.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import enlightenment.com.contents.Constants;
import enlightenment.com.recevicer.UserTokenReceiver;

/**
 * Created by lw on 2017/8/17.
 */

public abstract class AppActivity extends AppCompatActivity {

    private UserTokenReceiver mUserTokenReceiver;

    private static boolean isMiUi = false;

    protected void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    private SensorEventListener sensorEventListener;
    private PowerManager.WakeLock wakeLock;
    private SensorManager sensorManager;
    private AudioManager audioManager;

    protected abstract int getLayoutId();

    protected abstract void init(@Nullable Bundle savedInstanceState);

    protected abstract void initData();

    protected abstract void clearData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        setContentView(getLayoutId());
        init(savedInstanceState);
        initData();
    }

    public SharedPreferences getSharedPreferences() {
        return getSharedPreferences(Constants.Set.SET, Context.MODE_PRIVATE);
    }

    public boolean getSetSharedPreferences(String key, boolean flag) {
        return getSharedPreferences().getBoolean(key, flag);
    }

    public boolean setSetSharedPreferences(String key, boolean flag) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(key, flag);
        editor.commit();
        return true;
    }

    public void showCustomToast(String message) {
        showCustomToast(message, R.drawable.shape_bg_toast);
    }

    public void showCustomToast(String message, int color) {
        //加载Toast布局
        View toastRoot = LayoutInflater.from(this).inflate(R.layout.view_toast, null);
        //初始化布局控件
        TextView mTextView = (TextView) toastRoot.findViewById(R.id.toast_message);
        ImageView mImageView = (ImageView) toastRoot.findViewById(R.id.toast_image);
        //为控件设置属性
        mTextView.setText(message);
        mTextView.setBackground(getDrawable(color));
        //Toast的初始化
        Toast toastStart = new Toast(this);
        //获取屏幕高度
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        //Toast的Y坐标是屏幕高度的1/3，不会出现不适配的问题
        toastStart.setGravity(Gravity.TOP, 0, 90);
        toastStart.setDuration(Toast.LENGTH_LONG);
        toastStart.setView(toastRoot);
        toastStart.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerUserReceiver();
        //initSensor();

    }

    private void registerUserReceiver() {
        mUserTokenReceiver = new UserTokenReceiver() {
            @Override
            public void onLoginErrorCallback(String msg) {
                onLoginError(msg);
            }
        };
        IntentFilter filter = new IntentFilter(UserTokenReceiver.APP_RECEIVER_LOGIN_ERROR);
        registerReceiver(mUserTokenReceiver, filter);
    }

    /**
     * 监听光感，自动切换耳机模式
     */
    protected void initSensor() {
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        wakeLock = powerManager.newWakeLock(
                PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, this.getClass().getName());
        wakeLock.setReferenceCounted(false);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float[] dis = sensorEvent.values;
                if (0.0f == dis[0]) { // 靠近身体
                    wakeLock.release(); // 熄灭屏幕
                    //switchToEarpiece(); // 切换到听筒
                    switchToSpeaker();
                } else {
                    wakeLock.acquire(); // 点亮屏幕
                    switchToSpeaker(); // 切换到扬声器
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        sensorManager.registerListener(sensorEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void switchToEarpiece() {
        audioManager.setSpeakerphoneOn(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        } else {
            audioManager.setMode(AudioManager.MODE_IN_CALL);
        }
    }

    protected void switchToSpeaker() {
        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.setSpeakerphoneOn(true);
    }

    @Override
    protected void onStop() {
        if (mUserTokenReceiver != null)
            unregisterReceiver(mUserTokenReceiver);
        mUserTokenReceiver = null;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        clearData();
        super.onDestroy();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN)
            Glide.get(this).clearMemory();
        Glide.get(this).trimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }

    /**
     * 设置小米黑色状态栏字体
     */
    @SuppressLint("PrivateApi")
    private void setMIUIStatusBarDarkMode() {
        if (isMiUi) {
            Class<? extends Window> clazz = getWindow().getClass();
            try {
                int darkModeFlag;
                Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                extraFlagField.invoke(getWindow(), darkModeFlag, darkModeFlag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 静态域，获取系统版本是否基于MIUI
     */

    static {
        try {
            @SuppressLint("PrivateApi") Class<?> sysClass = Class.forName("android.os.SystemProperties");
            Method getStringMethod = sysClass.getDeclaredMethod("get", String.class);
            String version = (String) getStringMethod.invoke(sysClass, "ro.miui.ui.version.name");
            isMiUi = version.compareTo("V6") >= 0 && Build.VERSION.SDK_INT < 24;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置魅族手机状态栏图标颜色风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean setMeiZuDarkMode(Window window, boolean dark) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 24) {
            return false;
        }
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @SuppressLint("InlinedApi")
    private int getStatusBarLightMode() {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isMiUi) {
                result = 1;
            } else if (setMeiZuDarkMode(getWindow(), true)) {
                result = 2;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                result = 3;
            }
        }
        return result;
    }


    @SuppressLint("InlinedApi")
    protected void setStatusBarDarkMode() {
        int type = getStatusBarLightMode();
        if (type == 1) {
            setMIUIStatusBarDarkMode();
        } else if (type == 2) {
            setMeiZuDarkMode(getWindow(), true);
        } else if (type == 3) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    public void onLoginError(String msg) {
        showCustomToast(msg);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(AppActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 4000);
    }
}
