package com.edit.bean;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.utils.TypeConverUtil;
import com.webedit.HtmlTextBean;

import java.util.ArrayList;

/**
 * Created by lw on 2018/1/2.
 */

public class EditBean implements Parcelable {
    public static final int TYPE_TEXT = 0;
    public static final int TYPE_AUDIO = 1;
    public static final int TYPE_PHOTO = 2;
    public static final int TYPE_VIDEO = 3;

    private int mType;
    private int mViewType = 0;

    private String mHTML5 =null;
    private ArrayList<HtmlTextBean> htmlTextBeanArrayList = new ArrayList<>();
    private ArrayList<Integer> enumArrayList = new ArrayList<>();
    private int htmlTextSize;
    private String gravity;

    private String mProviderName;          //标题
    private String mTime = "0";               //时长

    private String mPath;   //存放文件路径
    private String mBackgroundPath;
    private String mHttpPath;   //网络文件路径

    public EditBean() {

    }

    public EditBean(int type){
        this.mType = type;
    }

    public static EditBean newInstance() {

        return new EditBean();
    }

    protected EditBean(Parcel in) {
        mType = in.readInt();
        mViewType = in.readInt();
        mHTML5 = in.readString();
        htmlTextBeanArrayList = in.createTypedArrayList(HtmlTextBean.CREATOR);
        enumArrayList = TypeConverUtil.intToList(in);
        htmlTextSize = in.readInt();
        gravity = in.readString();
        mProviderName = in.readString();
        mTime = in.readString();
        mPath = in.readString();
        mBackgroundPath = in.readString();
        mHttpPath = in.readString();
    }

    public static final Creator<EditBean> CREATOR = new Creator<EditBean>() {
        @Override
        public EditBean createFromParcel(Parcel in) {
            return new EditBean(in);
        }

        @Override
        public EditBean[] newArray(int size) {
            return new EditBean[size];
        }
    };

    public int getType() {
        return mType;
    }

    public EditBean setType(int mType) {
        this.mType = mType;
        return this;
    }

    public int getViewType() {
        return mViewType;
    }

    public void setViewType(int mViewType) {
        this.mViewType = mViewType;
    }

    public String getHTML5() {
        return mHTML5;
    }

    public EditBean setHTML5(String mHTML5) {
        this.mHTML5 = mHTML5;
        return this;
    }

    public ArrayList<HtmlTextBean> getHtmlTextBeanArrayList() {
        return htmlTextBeanArrayList;
    }

    public EditBean setHtmlTextBeanArrayList(ArrayList<HtmlTextBean> htmlTextBeanArrayList) {
        this.htmlTextBeanArrayList = htmlTextBeanArrayList;
        return this;
    }

    public ArrayList<Integer> getEnumArrayList() {
        return enumArrayList;
    }

    public EditBean setEnumArrayList(ArrayList<Integer> enumArrayList) {
        this.enumArrayList = enumArrayList;
        return this;
    }

    public int getHtmlTextSize() {
        return htmlTextSize;
    }

    public EditBean setHtmlTextSize(int htmlTextSize) {
        this.htmlTextSize = htmlTextSize;
        return this;
    }

    public String getGravity() {
        return gravity;
    }

    public EditBean setGravity(String gravity) {
        this.gravity = gravity;
        return this;
    }

    public String getProviderName() {
        return mProviderName;
    }

    public EditBean setProviderName(String mProviderName) {
        this.mProviderName = mProviderName;
        return this;
    }

    public String getTime() {
        return mTime;
    }

    public EditBean setTime(String mTime) {
        this.mTime = mTime;
        return this;
    }

    public String getPath() {
        return mPath;
    }

    public EditBean setPath(String mPath) {
        this.mPath = mPath;
        return this;
    }

    public void setBackgroundPath(String mBackgroundPath) {
        this.mBackgroundPath = mBackgroundPath;
    }

    public String getBackgroundPath() {
        return mBackgroundPath;
    }

    public String getHttpPath() {
        return mHttpPath;
    }

    public void setHttpPath(String mHttpPath) {
        this.mHttpPath = mHttpPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mType);
        dest.writeInt(mViewType);
        dest.writeString(mHTML5);
        dest.writeTypedList(htmlTextBeanArrayList);
        dest.writeInt(enumArrayList.size());
        dest.writeIntArray(TypeConverUtil.listToInt(enumArrayList));
        dest.writeInt(htmlTextSize);
        dest.writeString(gravity);
        dest.writeString(mProviderName);
        dest.writeString(mTime);
        dest.writeString(mPath);
        dest.writeString(mBackgroundPath);
        dest.writeString(mHttpPath);
    }

}
