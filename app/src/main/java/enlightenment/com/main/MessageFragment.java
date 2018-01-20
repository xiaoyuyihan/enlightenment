package enlightenment.com.main;

import android.support.v4.app.Fragment;

/**
 * Created by lw on 2017/7/26.
 */
public class MessageFragment extends Fragment{
    private static MessageFragment messageFragment;
    public static Fragment getInstanceFragment() {
        if (messageFragment==null){
            messageFragment=new MessageFragment();
        }
        return messageFragment;
    }
}
