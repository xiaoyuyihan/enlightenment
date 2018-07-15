package enlightenment.com.operationBean;

import enlightenment.com.base.EnlightenmentApplication;
import enlightenment.com.contents.Constants;

/**
 * Created by admin on 2018/3/30.
 */

public class CommentGetBean {
    private String id;
    private String type;
    private String token= EnlightenmentApplication.getInstance().getString(Constants.Set.SET_USER_TOKEN);

    public CommentGetBean(String id,String type){
        this.id=id;
        this.type=type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
