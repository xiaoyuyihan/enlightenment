package enlightenment.com.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import enlightenment.com.base.R;

/**
 * Created by lw on 2017/7/26.
 */
public class MyselfFragment extends Fragment{
    private static MyselfFragment messageFragment;
    public static Fragment getInstanceFragment() {
        if (messageFragment==null){
            messageFragment=new MyselfFragment();
        }
        return messageFragment;
    }

    private RecyclerView myself_field;
    private MainItemAdapter homeAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myself,container,false);
        myself_field=(RecyclerView)view.findViewById(R.id.myself_field);
        myself_field.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.HORIZONTAL));
        homeAdapter = new MainItemAdapter(getActivity(),new ArrayList(),R.layout.item_myself_field);
        myself_field.setAdapter(homeAdapter);
        return view;
    }
}
