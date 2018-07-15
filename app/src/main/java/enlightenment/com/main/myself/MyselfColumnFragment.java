package enlightenment.com.main.myself;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import enlightenment.com.base.R;
import enlightenment.com.main.ItemViewHolder;
import enlightenment.com.tool.device.DisplayUtils;
import enlightenment.com.view.PopupWindow.CusPopupWindow;

/**
 * Created by admin on 2018/5/26.
 */

public class MyselfColumnFragment extends Fragment {
    public static MyselfColumnFragment getInstance() {
        return new MyselfColumnFragment();
    }

    private int[] mDrawables = {R.drawable.img1,
            R.drawable.img2, R.drawable.img3, R.drawable.img4,
            R.drawable.img5, R.drawable.img6,R.drawable.img1,
            R.drawable.img2, R.drawable.img3, R.drawable.img4,
            R.drawable.img5, R.drawable.img6};

    private String[] mTopNames ;
    private String[] mColumnTools;
    private CusPopupWindow mColumnPopupWindow = null;

    private MyselfColumnAdapter columnAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_recycler_only,
                container, false);
        mTopNames = getResources().getStringArray(R.array.myself_test);
        mColumnTools = getResources().getStringArray(R.array.main_myself_column_tool);
        columnAdapter = new MyselfColumnAdapter(getActivity(),mDrawables,mTopNames);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(columnAdapter);
        return recyclerView;
    }

    public class MyselfColumnAdapter extends RecyclerView.Adapter{

        public Context context;
        private int[] mDrawables;
        private String[] mTopNames;

        public MyselfColumnAdapter(Context context, int[] mDrawables, String[] mTopNames){
            this.context = context;
            this.mDrawables= mDrawables;
            this.mTopNames = mTopNames;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(context).inflate(R.layout.item_myself_column,parent,false);
            MyselfColumnHolderView holderView= new MyselfColumnHolderView(view);
            return holderView;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            MyselfColumnHolderView holderView = (MyselfColumnHolderView)holder;
            holderView.setImageView(mDrawables[position]);
            holderView.setTextView(mTopNames[position]);
            ((MyselfColumnHolderView) holder).itemView.
                    setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    showPopupWindow(holder.itemView,position);
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return mTopNames.length;
        }
    }

    private void showPopupWindow(View v, int position) {
        if (mColumnPopupWindow == null) {
            mColumnPopupWindow = CusPopupWindow.Builder.getInstance(getActivity())
                    .setView(R.layout.fragment_recycler_only)
                    .setBackground(new ColorDrawable(getResources().getColor(R.color.grey00)))
                    .setWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                    .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                    .setFocusable(false)
                    .setOnCusPopupLinener(new CusPopupWindow.OnCusPopupListener() {
                        @Override
                        public void onCusPopupListener(View view) {
                            RecyclerView mToolRecycler = (RecyclerView)view;
                            mToolRecycler.setPadding(2,2,2,2);
                            mToolRecycler.setBackgroundResource(R.color.gray);
                            LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
                            mToolRecycler.setLayoutManager(layoutManager);
                            mToolRecycler.setAdapter(new RecyclerView.Adapter() {
                                @Override
                                public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                                    ItemViewHolder.BaseViewHolder textViewHolder =
                                            new ItemViewHolder.BaseViewHolder(
                                                    ItemViewHolder.createTextView(getActivity(),
                                                            DisplayUtils.dp2px(getActivity(), 36),
                                                            DisplayUtils.dp2px(getActivity(), 84))
                                            );
                                    return textViewHolder;
                                }

                                @Override
                                public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
                                    ((TextView)holder.itemView).setText(mColumnTools[position]);
                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            onColumnToolClick(position,position);
                                        }
                                    });
                                }

                                @Override
                                public int getItemCount() {
                                    return mColumnTools.length;
                                }
                            });
                        }
                    })
                    .builder();
            mColumnPopupWindow.showAsDropDown(v,-v.getWidth()/2, -v.getHeight()/2,
                    Gravity.RIGHT|Gravity.TOP);
        } else {
            mColumnPopupWindow.dismiss();
            mColumnPopupWindow = null;
        }
    }

    private void onColumnToolClick(int columnID, int postion) {

    }

    public class MyselfColumnHolderView extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView textView;
        public MyselfColumnHolderView(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.item_myself_column_image);
            textView = (TextView)itemView.findViewById(R.id.item_myself_column_name);
        }

        public void setImageView(int id){
            imageView.setImageResource(id);
        }

        public void setTextView(String name) {
            textView.setText(name);
        }
    }
}
