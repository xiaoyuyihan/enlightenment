package enlightenment.com.resources;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by lw on 2017/9/14.
 */

public class GeneralAdapter <T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {

    private T mViewHolder;
    private List mData;
    private int mViewID;
    private LayoutInflater mInflater;
    private Class<T> mHolderClass;
    private GeneralAdapterHelp mAdapterHelp;

    /**
     *
     * @param mHolderClass
     * @param mContext
     * @param mViewID
     * @param mData
     * @param mAdapterHelp
     */
    public GeneralAdapter(Class<T> mHolderClass, Context mContext,int mViewID,List mData,
                          GeneralAdapterHelp mAdapterHelp){
        this.mData=mData;
        this.mViewID=mViewID;
        this.mInflater=LayoutInflater.from(mContext);
        this.mHolderClass=mHolderClass;
        this.mAdapterHelp=mAdapterHelp;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflater.inflate(mViewID,parent,false);

        try {
            Constructor c=mHolderClass.getConstructor(View.class);//获取有参构造
            mViewHolder=(T) c.newInstance(view);    //通过有参构造创建对象
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e){
            e.printStackTrace();
        }
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mAdapterHelp.onBindViewHolder(holder,position);
    }

    @Override
    public int getItemCount() {
        return mData!=null?mData.size():0;
    }

    public interface GeneralAdapterHelp{
        void onBindViewHolder(RecyclerView.ViewHolder holder,int position);
    }
}
