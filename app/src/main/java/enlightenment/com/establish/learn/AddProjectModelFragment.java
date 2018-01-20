package enlightenment.com.establish.learn;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import enlightenment.com.base.EnlightenmentApplication;
import enlightenment.com.base.R;
import enlightenment.com.module.ModuleChildBean;
import enlightenment.com.module.ModuleFatherBean;
import enlightenment.com.view.FlowLayout;

/**
 * Created by lw on 2017/10/13.
 */

public class AddProjectModelFragment extends DialogFragment implements View.OnClickListener {

    private View contentView;
    private ImageView imageBack;
    private FlowLayout flowLayout;
    private TextView topContentView;

    private List<ModuleFatherBean> moduleFatherBeen = new ArrayList<>();
    private OnClickListener onClickListener;
    private boolean chilidFlag = false;

    public AddProjectModelFragment(OnClickListener clickListener){
        onClickListener=clickListener;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_dialog_project_model, container, false);
        imageBack = (ImageView) contentView.findViewById(R.id.top_left_image);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!chilidFlag) {
                    onClickListener.Back();
                }else {
                    chilidFlag=!chilidFlag;
                    setFatherView(moduleFatherBeen);
                }
            }
        });
        topContentView = (TextView) contentView.findViewById(R.id.top_center_text);
        topContentView.setText("选择模块");
        contentView.findViewById(R.id.top_right_text).setVisibility(View.GONE);
        flowLayout = (FlowLayout) contentView.findViewById(R.id.fragment_dialog_project_model_FlowLayout);
        moduleFatherBeen = EnlightenmentApplication.getInstance().getModuleFatherBeen();
        setFatherView(moduleFatherBeen);
        return contentView;
    }

    private void setFatherView(List<ModuleFatherBean> moduleChildBeen) {
        flowLayout.removeAllViews();
        for (ModuleFatherBean childBean : moduleChildBeen) {
            TextView textView = new TextView(getActivity());
            textView.setTag(childBean);
            textView.setText(childBean.getName());
            ViewGroup.MarginLayoutParams m = new ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            m.setMargins(18, 12, 18, 2);
            textView.setBackground(getResources().getDrawable(R.drawable.background_interest_module));
            textView.setLayoutParams(m);
            textView.setTextSize(18);
            textView.setOnClickListener(this);
            flowLayout.addView(textView);
        }
        flowLayout.requestLayout();
    }

    private void setChildView(List<ModuleChildBean> moduleChildBeen) {
        flowLayout.removeAllViews();
        for (ModuleChildBean childBean : moduleChildBeen) {
            TextView textView = new TextView(getActivity());
            textView.setTag(childBean);
            textView.setText(childBean.getName());
            ViewGroup.MarginLayoutParams m = new ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            m.setMargins(18, 12, 18, 2);
            textView.setBackground(getResources().getDrawable(R.drawable.background_interest_module));
            textView.setLayoutParams(m);
            textView.setTextSize(18);
            textView.setOnClickListener(this);
            flowLayout.addView(textView);
        }
        flowLayout.requestLayout();
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
    }

    @Override
    public void onClick(View view) {
        if (view.getTag() instanceof ModuleChildBean) {
            onClickListener.ItemChildModuleBean((ModuleChildBean) view.getTag());
        } else {
            chilidFlag = !chilidFlag;
            topContentView.setText(((ModuleFatherBean) view.getTag()).getName());
            setChildView(((ModuleFatherBean) view.getTag()).getChildBeen());
        }
    }

    interface OnClickListener {
        void Back();

        void ItemChildModuleBean(ModuleChildBean childBeanList);
    }
}
