package enlightenment.com.information;

import android.app.Activity;

import java.util.ArrayList;

import enlightenment.com.mvp.BaseView;

/**
 * Created by lw on 2018/2/27.
 */

public interface InformationView extends BaseView {

    @Override
    void showToast(String message);

    void showColumnCheck();
    void showColumnNews();
    void showModelDialog();
}
