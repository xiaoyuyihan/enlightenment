package enlightenment.com.details;

import android.support.v4.app.Fragment;

import java.util.List;

import enlightenment.com.operationBean.CommentBean;

/**
 * Created by admin on 2018/3/30.
 */

public abstract class ContentDetailsFragment extends Fragment {

    abstract void updateComments(List<CommentBean> commentBeans);
}
