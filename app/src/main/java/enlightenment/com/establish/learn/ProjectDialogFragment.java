package enlightenment.com.establish.learn;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import enlightenment.com.base.R;
import enlightenment.com.resources.GeneralAdapter;

/**
 * Created by lw on 2017/10/11.
 */

public class ProjectDialogFragment extends DialogFragment implements View.OnClickListener{

    private View contentView;
    private RecyclerView recyclerView;
    private ImageView addView;
    private ImageView backView;
    private TextView centerTextView;
    private GeneralAdapter mAdapter;
    private LinearLayout loadingLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<ProjectBean> projectData = new ArrayList();
    private OnDialogClick itemHolderClick;
    private static String NEW_TYPE_KEY="new_type_key";

    public void setItemHolderClick(OnDialogClick itemHolderClick) {
        this.itemHolderClick = itemHolderClick;
    }

    public static ProjectDialogFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt(NEW_TYPE_KEY,type);
        ProjectDialogFragment fragment = new ProjectDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_dialog_project, container, false);
        backView=(ImageView) contentView.findViewById(R.id.top_left_image);
        backView.setOnClickListener(this);
        centerTextView=(TextView) contentView.findViewById(R.id.top_center_text);
        centerTextView.setText("选择所属栏目");
        addView = (ImageView) contentView.findViewById(R.id.top_right_image);
        addView.setVisibility(View.VISIBLE);
        addView.setOnClickListener(this);
        contentView.findViewById(R.id.top_right_text).setVisibility(View.GONE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onProjectRefresh();
            }
        });
        loadingLayout=(LinearLayout)contentView.findViewById(R.id.fragment_dialog_project_loading);
        recyclerView = (RecyclerView) contentView.findViewById(R.id.fragment_recycler);
        mAdapter = new GeneralAdapter(ProjectHolder.class, getActivity(), R.layout.item_dialog_project_text,
                projectData, new GeneralAdapter.GeneralAdapterHelp() {
            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ProjectHolder projectHolder=(ProjectHolder)holder;
                projectHolder.setNameViewText(projectData.get(position).getName());
                projectHolder.setTimeView(projectData.get(position).getTime());
                if (itemHolderClick!=null){
                    itemHolderClick.onItemHolderClick(projectData.get(position));
                }
            }
        });
        //线性布局管理器
        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        //设置布局管理器
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerView.setAdapter(mAdapter);
        return contentView;
    }

    /**
     * 跟新数据
     */
    private void onProjectRefresh() {

    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.8),(int) (dm.heightPixels * 0.6));
            //点击外部不消失的方法
            dialog.setCancelable(false);
        }
        onProjectRefresh();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.top_left_image:
                if (itemHolderClick!=null){
                    itemHolderClick.onBack();
                }
                break;
            case R.id.top_right_image:
                if (itemHolderClick!=null){
                    itemHolderClick.onAddHolderClick();
                }
                break;
        }
    }

    public interface OnDialogClick{
        void onItemHolderClick(ProjectBean projectBean);
        void onAddHolderClick();
        void onBack();
    }

    class ProjectHolder extends RecyclerView.ViewHolder {
        private TextView nameView;
        private TextView timeView;

        public ProjectHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.item_dialog_project_name);
            timeView = (TextView) itemView.findViewById(R.id.item_dialog_project_time);
        }

        public void setNameViewText(String text) {
            nameView.setText(text);
        }

        public void setTimeView(String text) {
            timeView.setText(text);
        }
    }

    public static class ProjectBean implements Parcelable{
        private String Name;
        private String Introduce;
        private String Time;
        private String Id;

        protected ProjectBean(Parcel in) {
            Name = in.readString();
            Introduce = in.readString();
            Time = in.readString();
            Id = in.readString();
        }

        public static final Creator<ProjectBean> CREATOR = new Creator<ProjectBean>() {
            @Override
            public ProjectBean createFromParcel(Parcel in) {
                return new ProjectBean(in);
            }

            @Override
            public ProjectBean[] newArray(int size) {
                return new ProjectBean[size];
            }
        };

        public void setName(String name) {
            Name = name;
        }

        public void setIntroduce(String introduce) {
            Introduce = introduce;
        }

        public void setTime(String time) {
            Time = time;
        }

        public String getName() {
            return Name;
        }

        public String getIntroduce() {
            return Introduce;
        }

        public String getTime() {
            return Time;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(Name);
            parcel.writeString(Introduce);
            parcel.writeString(Time);
            parcel.writeString(Id);
        }
    }
}
