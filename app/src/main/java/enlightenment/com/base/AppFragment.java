package enlightenment.com.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
/**
 * Created by admin on 2018/7/18.
 */

public abstract class AppFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    public void onDestroy() {
        clearData();
        super.onDestroy();
    }

    protected abstract void clearData();

    protected abstract void initData();
}
