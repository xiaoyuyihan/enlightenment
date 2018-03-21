package com.edit;

import android.content.Intent;

/**
 * Created by lw on 2018/1/17.
 */

public interface OnFragmentResultListener {
    void onFragmentResult(int requestCode, int resultCode, Intent data);

    /**
     * 提交给Fragment处理
     * @param intent    添加各自的数据
     * @return  true 数据不为空， false 为空，不允许提交
     */
    boolean onSubject(Intent intent);
}
