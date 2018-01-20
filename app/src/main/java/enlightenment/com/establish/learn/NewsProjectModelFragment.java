package enlightenment.com.establish.learn;

import android.app.Dialog;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import enlightenment.com.base.R;

/**
 * Created by lw on 2017/10/15.
 */

public class NewsProjectModelFragment extends DialogFragment implements View.OnClickListener{
    private View contentView;
    private ImageView topLeftView;
    private TextView topCenterView;
    private TextView topRightView;
    private ImageView projectImageView;
    private TextInputEditText mProjectName;
    private TextInputEditText mProjectIntroduction;
    private AddProjectModelFragment.OnClickListener onClickListener;

    public NewsProjectModelFragment(AddProjectModelFragment.OnClickListener onClickListener){
        this.onClickListener=onClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView=inflater.inflate(R.layout.fragment_new_project,container,false);
        initView();
        return contentView;
    }

    private void initView() {
        topLeftView=(ImageView)contentView.findViewById(R.id.top_left_image);
        topLeftView.setOnClickListener(this);
        topCenterView=(TextView) contentView.findViewById(R.id.top_center_text);
        topCenterView.setText("创建新栏目");
        topRightView=(TextView) contentView.findViewById(R.id.top_right_text);
        topRightView.setOnClickListener(this);
        topRightView.setText("创建");
        projectImageView=(ImageView) contentView.findViewById(R.id.fragment_new_project_image);
        projectImageView.setOnClickListener(this);
        mProjectName=(TextInputEditText) contentView.findViewById(R.id.fragment_new_project_name);
        mProjectIntroduction=(TextInputEditText) contentView.findViewById(R.id.fragment_new_project_Introduction);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.top_left_image:
                onClickListener.Back();
                break;
            case R.id.top_right_text:
                break;
        }
    }
}
