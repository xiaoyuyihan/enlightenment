package enlightenment.com.main;

import android.support.v4.widget.SwipeRefreshLayout;

import enlightenment.com.mvp.BaseView;

/**
 * Created by lw on 2017/9/4.
 */

public interface MainView extends BaseView{
    void swipeRefresh(SwipeRefreshLayout swipeRefreshLayout);
}
