package enlightenment.com.operationBean;

import enlightenment.com.base.EnlightenmentApplication;
import enlightenment.com.contents.Constants;

/**
 * Created by admin on 2018/3/27.
 */

public class FollowBean {
    private String id;
    private String flag;
    private String type;
    private String source;
    private String token= EnlightenmentApplication.getInstance().getString(Constants.Set.SET_USER_TOKEN);

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private FollowBean(String id, String flag, String type, String source){
        this.id=id;
        this.flag=flag;
        this.type=type;
        this.source =source;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public static FollowBean createFollowBean(String id,String type,String source){
        return new FollowBean(id,"1",type,source);
    }

    public static FollowBean createUnFollowBean(String id,String type,String source){
        return new FollowBean(id,"-1",type,source);
    }
}
