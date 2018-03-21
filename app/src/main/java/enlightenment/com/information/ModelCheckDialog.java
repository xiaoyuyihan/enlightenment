package enlightenment.com.information;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import enlightenment.com.base.R;
import enlightenment.com.module.ModuleBean;
import enlightenment.com.module.ModuleChildBean;

/**
 * Created by lw on 2018/3/1.
 */

public class ModelCheckDialog extends DialogFragment {

    public static String DIALOG_MODEL_DATA="DIALOG_MODEL_DATA";

    private View mContentView;

    public void setOnClickModelCheck(OnClickModelCheck onClickModelCheck) {
        this.onClickModelCheck = onClickModelCheck;
    }

    private OnClickModelCheck onClickModelCheck;

    @BindView(R.id.fragment_dialog_model_father)
    RecyclerView mFatherRecycler;
    @BindView(R.id.fragment_dialog_model_child)
    RecyclerView mChildRecycler;

    private ModelAdapter mFatherAdapter;
    private ModelAdapter mChildAdapter;

    private LinearLayoutManager mFatherManager;
    private LinearLayoutManager mChildManager;

    private ArrayList<ModuleBean> mModuleBeanList;
    private ArrayList<ModuleChildBean> mModuleChildList;
    private int position=0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_dialog_model_check,container,false);
        ButterKnife.bind(this,mContentView);
        mModuleBeanList=getArguments().getParcelableArrayList(DIALOG_MODEL_DATA);
        mModuleChildList=mModuleBeanList.get(position).getChildBeen();
        init();
        return mContentView;
    }

    private void init() {
        if (mModuleBeanList ==null&&mModuleBeanList.size()>0)
            return;
        mFatherAdapter=new ModelAdapter();
        mFatherAdapter.setItemCount(mModuleBeanList.size());
        mFatherAdapter.setOnBindView(new OnBindView() {
            @Override
            public void onBindView(RecyclerView.ViewHolder holder, final int position) {
                TextView mContentText=((TextView)holder.itemView);
                mContentText.setText(mModuleBeanList.get(position).getName());
                mContentText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mModuleChildList=mModuleBeanList.get(position).getChildBeen();
                        mChildAdapter.setItemCount(mModuleChildList.size());
                        ModelCheckDialog.this.position=position;
                        mChildAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        mChildAdapter=new ModelAdapter();
        mChildAdapter.setItemCount(mModuleChildList.size());
        mChildAdapter.setOnBindView(new OnBindView() {
            @Override
            public void onBindView(RecyclerView.ViewHolder holder, final int position) {
                TextView mContentText=((TextView)holder.itemView);
                mContentText.setText(mModuleChildList.get(position).getName());
                mContentText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onClickModelCheck!=null)
                            onClickModelCheck.onClickCheck(ModelCheckDialog.this.position,position);
                    }
                });
            }
        });

        mFatherManager=new LinearLayoutManager(getActivity());
        mChildManager=new LinearLayoutManager(getActivity());
        mFatherRecycler.setAdapter(mFatherAdapter);
        mFatherRecycler.setLayoutManager(mFatherManager);
        mChildRecycler.setAdapter(mChildAdapter);
        mChildRecycler.setLayoutManager(mChildManager);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.9),(int) (dm.heightPixels * 0.6));
            //点击外部不消失的方法
            //dialog.setCancelable(false);
        }
    }

    class ModelAdapter extends RecyclerView.Adapter{
        private OnBindView onBindView;
        private int height=120;
        private int itemCount=0;

        public ModelAdapter setHeight(int height) {
            this.height = height;
            return this;
        }

        public ModelAdapter setItemCount(int itemCount) {
            this.itemCount = itemCount;
            return this;
        }

        public ModelAdapter setOnBindView(OnBindView onBindView) {
            this.onBindView = onBindView;
            return this;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView=new TextView(getActivity());
            textView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 100));
            textView.setGravity(Gravity.CENTER);
            ModelHolder modelHolder=new ModelHolder(textView);
            return modelHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (onBindView !=null)
                onBindView.onBindView(holder,position);
        }

        @Override
        public int getItemCount() {
            return itemCount;
        }
    }

    class ModelHolder extends RecyclerView.ViewHolder{

        public ModelHolder(View itemView) {
            super(itemView);
        }
    }

    interface OnBindView{
        void onBindView(RecyclerView.ViewHolder holder, int position);
    }

    interface OnClickModelCheck{
        void onClickCheck(int fatherPosition,int childPosition);
    }
}
