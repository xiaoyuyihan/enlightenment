package com.provider.utils;

import android.content.Intent;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by lw on 2017/12/19.
 */

public class IntentBean<T extends Parcelable> {
    private static IntentBean bean;

    public static IntentBean getInstance() {
        if (bean==null)
            bean=new IntentBean();
        return bean;
    }

    private ArrayList<T> mData=new ArrayList<>();
    private ArrayList<T> mChecks=new ArrayList<>();
    private int position;

    public ArrayList<T> getData() {
        return mData;
    }

    public IntentBean setData(ArrayList<T> mData) {
        this.mData = mData;
        return bean;
    }

    public ArrayList<T> getChecks() {
        return mChecks;
    }

    public IntentBean setChecks(ArrayList<T> mChecks) {
        this.mChecks = mChecks;
        return bean;
    }

    public IntentBean setCheck(T mCheck){
        this.mChecks.add(mCheck);
        return bean;
    }

    public int getPosition() {
        return position;
    }

    public IntentBean setPosition(int position) {
        this.position = position;
        return bean;
    }

    public IntentBean addCheck(T check){
        mChecks.add(check);
        return bean;
    }

    public void clear(){
        mData.clear();
        mChecks.clear();
    }
}
