package com.edit.bean;

/**
 * Created by admin on 2018/6/14.
 */

public class WebContentBean {
    private String name="";        //名称
    private String content="";     //内容
    private int flag=0;           //类型
    private int viewFlag=0;       //视图类型
    private int time=0;           //时长

    public WebContentBean(String name, String content,
                          int flag, int viewFlag, int time) {
        this.name = name;
        this.content = content;
        this.flag = flag;
        this.viewFlag = viewFlag;
        this.time = time;
    }
    public WebContentBean(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getViewFlag() {
        return viewFlag;
    }

    public void setViewFlag(int viewFlag) {
        this.viewFlag = viewFlag;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
