package enlightenment.com.main.mainAdapter;

import android.support.v7.widget.RecyclerView;

/**
 * Created by admin on 2018/7/27.
 */

public abstract class BaseAdapter extends RecyclerView.Adapter {
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    public static final int ITEM_TYPE_BOTTOM = 2;

    public static final int ITEM_BOTTOM_TYPE_FLAG_REFEWSH = 1001;
    public static final int ITEM_BOTTOM_TYPE_FLAG_ERROR = 1002;
    public static final int ITEM_BOTTOM_TYPE_FLAG_END = 1003;


    protected int mHeaderCount = 0;//头部View个数
    protected int mBottomCount = 1;//底部View个数

    protected String mHeaderMsg = "我们正在搜寻····";
    protected int mCurFlag = ITEM_BOTTOM_TYPE_FLAG_REFEWSH;

    public void setHeaderCount(String msg) {
        if (msg != null) {
            this.mHeaderMsg = msg;
            this.mHeaderCount = 1;
        } else
            this.mHeaderCount = 0;
    }
    public void updataBottomViewType(int flag) {
        this.mCurFlag = flag;
    }

}
