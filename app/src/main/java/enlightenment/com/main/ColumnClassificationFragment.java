package enlightenment.com.main;

import android.support.v4.app.Fragment;

/**
 * Created by lw on 2017/7/31.
 */

public class ColumnClassificationFragment extends Fragment {
    private static ColumnClassificationFragment columnClassificationFragment;

    public static ColumnClassificationFragment getInstance() {
        if(columnClassificationFragment==null){
            columnClassificationFragment=new ColumnClassificationFragment();
        }
        return columnClassificationFragment;
    }
}
