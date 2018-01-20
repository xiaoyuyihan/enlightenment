package enlightenment.com.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import enlightenment.com.base.R;

/**
 * Created by lw on 2017/7/28.
 */

public class MainItemAdapter extends RecyclerView.Adapter {

    private static final int TYPE_LOAD_MORE=100;

    private Context context;
    private int viewID;
    private ArrayList mData=new ArrayList();

    public MainItemAdapter(Context context,ArrayList mData, int viewID){
        this.context=context;
        this.viewID=viewID;
        this.mData=mData;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder mViewHolder;
        View view;
        switch (viewType){
            case TYPE_LOAD_MORE:
                view= LayoutInflater.from(context).inflate(R.layout.item_learn_text,parent,false);
                mViewHolder=new ItemViewHolder.LoadViewHolder(view);
                break;
            default:
                view= LayoutInflater.from(context).inflate(viewID,parent,false);
                mViewHolder=new ItemViewHolder.ImageViewHolder(view);
        }
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mData.size()<10?mData.size():mData.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
       if (position==mData.size()){
           return TYPE_LOAD_MORE;
       }else{
           return 0;
       }
    }
}
