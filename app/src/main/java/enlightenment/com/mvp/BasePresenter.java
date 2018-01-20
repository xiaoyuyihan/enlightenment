package enlightenment.com.mvp;

import android.os.Bundle;
import android.view.View;

/**
 * Created by lw on 2017/7/21.
 */

public abstract class BasePresenter<T extends BaseView> {

    /**
     * 绑定View
     * @param view
     */
    public void BindView(T view){};

    /**
     * 解除引用
     */
    public void unBindView(BaseView baseView){};
    public void onCreate(Bundle savedInstanceState){}
    public void onStart(){}
    public void onRestart(){}
    public void onResume(){}
    public void onPause(){}
    public void onStop(){}
    public void onDestroy(){}
}
