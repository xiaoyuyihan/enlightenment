package enlightenment.com.base;

import android.support.v7.app.AppCompatActivity;

import enlightenment.com.mvp.BaseView;

/**
 * Created by lw on 2017/7/21.
 */

public interface baseView extends BaseView {
    void startNextActivity(Class name);
    void requestException();
}
