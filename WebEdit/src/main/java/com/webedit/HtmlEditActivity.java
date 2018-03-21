package com.webedit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.edit.BaseActivity;
import com.edit.bean.EditBean;
import com.utils.MessageDialogFragment;
import com.webeditproject.R;
import com.webeditproject.R2;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lw on 2017/12/10.
 */

public class HtmlEditActivity extends BaseActivity implements HWebEditView.OnEnumClickListener {

    public static int HTML_EDIT_TYPE_FIRST = 1;       //初次
    public static int HTML_EDIT_TYPE_REWRITE = 3;     //修改

    public static String HTML_EDIT_TYPE_KEY = "HTML_EDIT_TYPE_KEY";           //类型
    public static String HTML_EDIT_DATA_KEY = "HTML_EDIT_DATA_KEY";           //数据
    public static String HTML_EDIT_POSITION_KEY="HTML_EDIT_POSITION_KEY";             //修改位置

    private int resultCode;
    private int position;
    private EditBean mEditBean;

    @BindView(R2.id.html_edit_HView)
    HWebEditView mHWebEditView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html_edit);
        ButterKnife.bind(this);
        resultCode = getIntent().getIntExtra(HTML_EDIT_TYPE_KEY, HTML_EDIT_TYPE_FIRST);
        if (resultCode != HTML_EDIT_TYPE_FIRST) {
            mEditBean = getIntent().getParcelableExtra(HTML_EDIT_DATA_KEY);
            mHWebEditView.upDataView(mEditBean.getHtmlTextBeanArrayList(), mEditBean.getEnumArrayList());
            position = getIntent().getIntExtra(HTML_EDIT_POSITION_KEY,-1);
        }
        mHWebEditView.setOnEnumClickListener(this);
    }

    @Override
    public void onBack() {
        if (mHWebEditView.getHWebHtmlList().size() == 0) {
            setResult(resultCode, null);
            finish();
        }else {
            final MessageDialogFragment messageDialogFragment=new MessageDialogFragment();
            messageDialogFragment.setOnMsgDialogClickListener(new MessageDialogFragment.OnMsgDialogClickListener() {
                @Override
                public void onSureBut() {
                    setResult(resultCode, null);
                    finish();
                }

                @Override
                public void onCancel() {
                    messageDialogFragment.dismiss();
                }
            });
            messageDialogFragment.setMessage("您有文章未保存，是否确定退出编辑页面！");
            messageDialogFragment.show(getSupportFragmentManager(),"MessageDialogFragment");
        }
    }

    @Override
    public void onSubject(ArrayList<HtmlTextBean> childHtmlBeans, ArrayList<Integer> HWebHtmlLabel,
                          String gravity, int Size, String html) {
        EditBean editBean = new EditBean();
        editBean.setType(EditBean.TYPE_TEXT)
                .setHtmlTextBeanArrayList(childHtmlBeans)
                .setEnumArrayList(HWebHtmlLabel)
                .setGravity(gravity)
                .setHtmlTextSize(Size)
                .setHTML5(html);
        Intent intent = new Intent();
        intent.putExtra(HTML_EDIT_DATA_KEY, editBean);
        intent.putExtra(HTML_EDIT_POSITION_KEY,position);
        setResult(resultCode, intent);
        finish();
    }
}
