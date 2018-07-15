package enlightenment.com.view;

import android.support.design.widget.AppBarLayout;
import android.util.Log;

/**
 * Created by admin on 2018/3/22.
 */

public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {
    /**
     * 打开
     */
    public static int EXPANDED = 1;
    /**
     * 关闭
     */
    public static int COLLAPSED = -1;
    /**
     * 中间状态
     */
    public static int IDLE = 0;

    private int mCurrentState = IDLE;

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            mCurrentState = EXPANDED;
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            mCurrentState = COLLAPSED;
        } else {
            mCurrentState = IDLE;
        }
        onStateChanged(appBarLayout, mCurrentState);
        Log.d(getClass().getName(), "AppBarChange:    " + mCurrentState);
    }

    public abstract void onStateChanged(AppBarLayout appBarLayout, int state);
}
