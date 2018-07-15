package enlightenment.com.operationBean;

import enlightenment.com.base.EnlightenmentApplication;
import enlightenment.com.contents.Constants;

/**
 * Created by lw on 2018/3/2.
 */

public class InformationBean {
    private String name;
    private String content;
    private String visition;
    private String viewType="0";
    private String type;
    private String fatherID;
    private String columnID;
    private String token= EnlightenmentApplication.getInstance().getString(Constants.Set.SET_USER_TOKEN);

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

    public String getVisition() {
        return visition;
    }

    public void setVisition(String visition) {
        this.visition = visition;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getColumnID() {
        return columnID;
    }

    public void setColumnID(String columnID) {
        this.columnID = columnID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
