package enlightenment.com.main;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by lw on 2017/9/6.
 */

public class OnRecyclerScrollListener extends RecyclerView.OnScrollListener {

    private RecyclerView.LayoutManager mLayoutManager;
    private OnRefreshListener refresh;

    public void setRefresh(OnRefreshListener refresh) {
        this.refresh = refresh;
    }

    public OnRecyclerScrollListener(RecyclerView.LayoutManager l,OnRefreshListener r){
        this.mLayoutManager=l;
        this.refresh=r;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        isScrollBottom();
    }

    /**
     *  view is scroll bottom
     * @return
     */
    private boolean isScrollBottom() {
        if (mLayoutManager instanceof LinearLayoutManager){
            LinearLayoutManager l=(LinearLayoutManager)mLayoutManager;
            //获取最后一个可见view的位置
            int lastItemPosition = l.findLastVisibleItemPosition();
            if (lastItemPosition>l.getChildCount()-2){
                if (refresh!=null)
                    refresh.Refresh();
                return true;
            }
        }
        return false;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        switch (newState){
            //不处于滑动状态
            case RecyclerView.SCROLL_STATE_IDLE:
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
            case RecyclerView.SCROLL_STATE_SETTLING:
                break;
        }
    }
    public interface OnRefreshListener{
        void Refresh();
    }
}
