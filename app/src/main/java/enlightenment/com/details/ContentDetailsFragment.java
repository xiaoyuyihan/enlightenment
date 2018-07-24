package enlightenment.com.details;

import java.util.List;

import enlightenment.com.base.AppFragment;
import enlightenment.com.operationBean.CommentBean;

/**
 * Created by admin on 2018/3/30.
 */

public abstract class ContentDetailsFragment extends AppFragment {

    abstract void updateComments(List<CommentBean> commentBeans);
}
