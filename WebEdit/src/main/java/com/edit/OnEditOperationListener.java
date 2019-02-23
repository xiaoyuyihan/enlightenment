package com.edit;

import com.edit.bean.EditBean;

import java.util.ArrayList;

/**
 * Created by admin on 2018/8/3.
 */

public interface OnEditOperationListener{
    void onDelete(EditBean bean);
    void onDeleteAll(ArrayList<EditBean> beans);
}

