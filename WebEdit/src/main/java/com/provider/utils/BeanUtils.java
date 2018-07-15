package com.provider.utils;

import android.os.Parcelable;

import com.edit.bean.EditBean;

import java.util.ArrayList;

/**
 * Created by admin on 2018/6/23.
 */

public class BeanUtils {
    public static ArrayList<Parcelable> getEditBeanList(ArrayList<Parcelable> mCheckPathList) {
        ArrayList<Parcelable> beanList=new ArrayList<>();
        for (Parcelable p:mCheckPathList){
            if (p instanceof ImageBean){
                beanList.add(imageBeanToEditBean(p));
            }else if (p instanceof AudioBean){
                beanList.add(audioBeanToEditBean(p));
            }else if (p instanceof VideoBean){
                beanList.add(videoBeanToEditBean(p));
            }
        }
        return beanList;
    }

    private static EditBean imageBeanToEditBean(Parcelable p){
        EditBean e=new EditBean();
        ImageBean i=(ImageBean)p;
        e.setType(EditBean.TYPE_PHOTO).setPath(i.getPath());
        return e;
    }
    private static EditBean audioBeanToEditBean(Parcelable p){
        EditBean e=new EditBean();
        AudioBean i=(AudioBean)p;
        e.setType(EditBean.TYPE_AUDIO).setPath(i.getPath())
                .setProviderName(i.getName()).setTime(i.getDuration());
        return e;
    }
    private static EditBean videoBeanToEditBean(Parcelable p){
        EditBean e=new EditBean();
        VideoBean i=(VideoBean)p;
        e.setType(EditBean.TYPE_VIDEO).setPath(i.getPath());
        return e;
    }
}
