package enlightenment.com.view;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by lw on 2017/7/27.
 */

public class AgainNestedScrollView extends NestedScrollView {
    private float firstX;
    public AgainNestedScrollView(Context context) {
        super(context);
    }

    public AgainNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AgainNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        return super.dispatchGenericMotionEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }
}
