package enlightenment.com.information;

import android.app.Dialog;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enlightenment.com.base.R;

/**
 * Created by lw on 2018/2/28.
 */

public class TypeCheckDialog extends DialogFragment {

    public static int CHECK_TYPE_LEARN=1;
    public static int CHECK_TYPE_DIY=2;

    private View mContentView;

    @BindView(R.id.top_left_image)
    ImageView mBackTopView;
    @BindView(R.id.top_center_text)
    TextView mContentTopView;
    @BindView(R.id.top_right_text)
    TextView mRightTextTopView;
    @BindView(R.id.fragment_dialog_type_check_learn)
    ImageView mLearnView;
    @BindView(R.id.fragment_dialog_type_check_diy)
    ImageView mDIYView;

    public OnClickTypeCheck onClickTypeCheck;

    public void setOnClickTypeCheck(OnClickTypeCheck onClickTypeCheck) {
        this.onClickTypeCheck = onClickTypeCheck;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView=inflater.inflate(R.layout.fragment_dialog_type_check,container,false);
        ButterKnife.bind(this,mContentView);
        init();
        return mContentView;
    }

    private void init() {
        mContentTopView.setText("类型");
        mRightTextTopView.setVisibility(View.GONE);
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

    @OnClick(R.id.top_left_image)
    public void onBack(View v){
        getDialog().dismiss();
    }

    @OnClick(R.id.fragment_dialog_type_check_learn)
    public void onClickLearn(View v){
        onClickTypeCheck.onClickType(CHECK_TYPE_LEARN);
    }

    @OnClick(R.id.fragment_dialog_type_check_diy)
    public void onClickDIY(View v){
        if (onClickTypeCheck!=null)
            onClickTypeCheck.onClickType(CHECK_TYPE_DIY);
    }

    interface OnClickTypeCheck{
        void onClickType(int type);
    }
}
