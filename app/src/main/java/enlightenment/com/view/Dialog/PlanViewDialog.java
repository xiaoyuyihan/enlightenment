package enlightenment.com.view.Dialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enlightenment.com.base.R;
import enlightenment.com.main.message.NewsPlanActivity;
import enlightenment.com.operationBean.PlanBean;
import enlightenment.com.tool.recycelr.SpacesItemDecoration;
import enlightenment.com.view.CircularTextView;

/**
 * Created by admin on 2018/7/26.
 */

public class PlanViewDialog extends DialogFragment implements View.OnLongClickListener {

    private static final String DIALOG_PLAN_KEY_YEAR = "DIALOG_PLAN_KEY_YEAR";
    private static final String DIALOG_PLAN_KEY_MONTH = "DIALOG_PLAN_KEY_MONTH";
    private static final String DIALOG_PLAN_KEY_DAY = "DIALOG_PLAN_KEY_DAY";
    private static final String DIALOG_PLAN_KEY_DATA = "DIALOG_PLAN_KEY_DATA";

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private int mYear;
    private int mMonth;
    private int mDay;
    private ArrayList<PlanBean> mPlanList = new ArrayList<>();
    private boolean mFlag = true;

    @BindView(R.id.top_center_text)
    TextView mCenterTopText;
    @BindView(R.id.fragment_recycler_only)
    RecyclerView mContentRecycler;
    @BindView(R.id.top_right_text)
    TextView mRightTopText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new PlanItemViewHolder(LayoutInflater.from(getActivity())
                        .inflate(R.layout.view_item_plan, parent, false));
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((PlanItemViewHolder)holder).updateDelete(mFlag);
            }

            @Override
            public int getItemCount() {
                return 10;
            }
        };
        /*mYear = getArguments().getInt(DIALOG_PLAN_KEY_YEAR);
        mMonth = getArguments().getInt(DIALOG_PLAN_KEY_MONTH);
        mDay = getArguments().getInt(DIALOG_PLAN_KEY_DAY);
        mPlanList = getArguments().getParcelableArrayList(DIALOG_PLAN_KEY_DATA);
    */}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_dialog_plan,
                container, false);
        ButterKnife.bind(this, contentView);
        init();
        return contentView;
    }

    private void init() {
        mRightTopText.setText("新建");
        mCenterTopText.setText("2018年7月27日计划");
        mCenterTopText.setTextSize(12);
        mContentRecycler.setLayoutManager(layoutManager);
        mContentRecycler.setAdapter(mAdapter);
        mContentRecycler.addItemDecoration(new SpacesItemDecoration(8));
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.9),(int) (dm.heightPixels * 0.7));
            //点击外部不消失的方法
            dialog.setCancelable(false);
        }
    }

    @OnClick(R.id.top_left_image)
    public void onBack(View view){
        dismiss();
    }

    @OnClick(R.id.top_right_text)
    public void onNews(View v){
        getActivity().startActivity(new Intent(getActivity(), NewsPlanActivity.class));
    }

    @Override
    public boolean onLongClick(View view) {
        mFlag = !mFlag;
        mAdapter.notifyDataSetChanged();
        return false;
    }

    public static class PlanItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_plan_circular)
        CircularTextView circularTextView;
        @BindView(R.id.item_plan_message)
        TextView messageView;
        @BindView(R.id.item_plan_time)
        TextView timeView;
        @BindView(R.id.item_plan_delete)
        ImageView deleteView;


        public PlanItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void updateDelete(boolean flag){
            if (flag){
                deleteView.setVisibility(View.GONE);
            }else
                deleteView.setVisibility(View.VISIBLE);
        }

    }

    public interface OnPlanItemClickListener {
        void onPlanClick(PlanBean bean);
        void onDelete(PlanBean bean);
    }

}
