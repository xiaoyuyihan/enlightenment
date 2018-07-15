package enlightenment.com.main;

import enlightenment.com.operationBean.ContentBean;

/**
 * Created by lw on 2018/3/14.
 */

public interface OnContentItemListener {
    void onItemClick(ContentBean contentBean);
    void onAvatarClick(String username);
    void onModelClick(int mode, int type);
}
