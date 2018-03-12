package com.provider.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.webeditproject.R;

/**
 * Created by lw on 2017/11/30.
 */

public class LoadingDialog extends DialogFragment {
    public static final String DIALOG_LOAD_NAME = "DIALOG_LOAD_NAME";

    private TextView mTextName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View DialogView = inflater.inflate(R.layout.fragment_dialog_loading, container, false);
        mTextName = DialogView.findViewById(R.id.dialog_load_name);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String msg = getArguments().getString(LoadingDialog.DIALOG_LOAD_NAME, "资源加载中····");
            mTextName.setText(msg);
        }
        return DialogView;
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout((int) (dm.widthPixels * 0.8), (int) (dm.heightPixels * 0.5));
    }
}
