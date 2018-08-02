package enlightenment.com.main.mainAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import enlightenment.com.base.R;
import enlightenment.com.main.found.FoundDynamicFragment;

/**
 * Created by admin on 2018/7/27.
 */

public class FoundItemAdapter extends BaseAdapter {
    public Context context;

    public FoundItemAdapter(Context context){
        this.context=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return  new FoundDynamicFragment.FoundViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_learn_text,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
