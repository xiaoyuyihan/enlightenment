package enlightenment.com.details;

import android.view.View;

import butterknife.OnClick;
import enlightenment.com.base.R;

/**
 * Created by admin on 2018/3/23.
 */

public interface onContentDetailsListener {

    //作者
    void onUsername();

    //栏目
    void onColumn();

    //模块
    void onModel();

    //关注
    void onFollow();

    //打赏
    void onReward();

    //更多
    void onLoadAllComment();
}
