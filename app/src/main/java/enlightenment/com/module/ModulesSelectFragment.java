package enlightenment.com.module;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.model.ModelLoader;

import java.util.ArrayList;
import java.util.List;

import enlightenment.com.base.R;
import enlightenment.com.main.found.FoundFragment;
import enlightenment.com.main.MainActivity;

/**
 * Created by lw on 2017/8/22.
 * 栏目选择
 */

public class ModulesSelectFragment extends Fragment {
    
    public final static int MOEDELES_FLAG_LEARN = 1;        //学习
    public final static int MOEDELES_FLAG_CREATE = 2;       //创造
    public final static int MOEDELES_FLAG_QUESETION = 3;    //提问

    private ExpandableListView moduleView;
    private ImageView topLeft;
    private TextView topCenter;
    private TextView topRight;
    private View view;
    private static ModulesSelectFragment mModulesSelectFragment;
    private int currentFlag = MOEDELES_FLAG_LEARN;

    public static ModulesSelectFragment getInstance() {
        if (mModulesSelectFragment==null){
            mModulesSelectFragment=new ModulesSelectFragment();
        }
        return mModulesSelectFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_modules_select,container,false);

        initView();

        List<List<ModuleBean>> childModules = new ArrayList<>();
        List<ModuleBean> fatherModules = new ArrayList<>();
        ModulesSelectAdapter modulesSelectAdapter = new ModulesSelectAdapter(getActivity(), childModules, fatherModules);
        moduleView.setAdapter(modulesSelectAdapter);
        return view;
    }

    private void initView() {
        topLeft=(ImageView) view.findViewById(R.id.top_left_image);
        topLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity=getActivity();
                if (activity instanceof MainActivity){
                    ((MainActivity)activity).setMainFragment(FoundFragment.getInstanceFragment());
                }
            }
        });
        topCenter=(TextView) view.findViewById(R.id.top_center_text);
        topCenter.setText("学习模块");
        topRight=(TextView) view.findViewById(R.id.top_right_text);
        topRight.setTextColor(getResources().getColor(R.color.mainTopColor));
        moduleView=(ExpandableListView)view.findViewById(R.id.select_modules_list);
    }

    class ModulesSelectAdapter extends BaseExpandableListAdapter {
        Context context;
        List<List<ModuleBean>> childModules = new ArrayList<>();
        List<ModuleBean> fatherModules = new ArrayList<>();

        public ModulesSelectAdapter(Context context, List<List<ModuleBean>> childModules, List<ModuleBean> fatherModules) {
            this.context = context;
            this.childModules = childModules;
            this.fatherModules = fatherModules;
        }


        @Override
        public int getGroupCount() {
            return 10;
        }

        @Override
        public int getChildrenCount(int i) {
            return 1;
        }

        @Override
        public Object getGroup(int i) {
            return fatherModules.get(i);
        }

        @Override
        public Object getChild(int i, int i1) {
            return childModules.get(i).get(i1);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @SuppressLint("ResourceType")
        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

            RelativeLayout layoutView = new RelativeLayout(context);
            TextView fatherText = new TextView(context);
            ImageView moduleImage = new ImageView(context);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 160);
            layoutView.setLayoutParams(params);
            layoutView.setPadding(120, 0, 0, 0);
            layoutView.setGravity(Gravity.CENTER);
            RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                    80, 80);
            moduleImage.setLayoutParams(imageParams);
            imageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            imageParams.setMargins(0, 40, 36, 40);
            moduleImage.setPadding(8,8,8,8);
            moduleImage.setId(1);
            moduleImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(),ModulesActivity.class));
                }
            });
            moduleImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_module_move));
            RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            textParams.addRule(RelativeLayout.LEFT_OF, moduleImage.getId());
            fatherText.setTextSize(16);
            fatherText.setText("生命科学院");
            fatherText.setGravity(Gravity.CENTER_VERTICAL);
            fatherText.setLayoutParams(textParams);
            layoutView.addView(fatherText);
            layoutView.addView(moduleImage);
            return layoutView;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            GridView gridView = new GridView(context);
            GridView.LayoutParams params = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 400);
            gridView.setGravity(Gravity.CENTER);
            gridView.setVerticalScrollBarEnabled(false);
            gridView.setNumColumns(3);
            gridView.setColumnWidth(48);
            gridView.setHorizontalSpacing(2);
            gridView.setVerticalSpacing(8);
            gridView.setLayoutParams(params);
            childModulesAdapter modulesAdapter = new childModulesAdapter(null, context);
            gridView.setAdapter(modulesAdapter);
            return gridView;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return false;
        }
    }

    class childModulesAdapter extends BaseAdapter {

        private List<ModuleBean> moduleBeans;
        private Context context;

        public childModulesAdapter(List<ModuleBean> moduleBeans, Context context) {
            this.moduleBeans = moduleBeans;
            this.context = context;
        }

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int i) {
            return moduleBeans.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            TextView textView = new TextView(context);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,4,0,4);
            textView.setPadding(16,16,16,16);
            textView.setLayoutParams(params);
            textView.setBackground(getResources().getDrawable(R.drawable.background_corners_4));
            textView.setGravity(Gravity.CENTER);
            textView.setText("世界美妙");
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context,ModulesActivity.class);
                    startActivity(intent);
                }
            });
            textView.setTextSize(14f);
            return textView;
        }
    }
}
