package enlightenment.com.operationBean;

import enlightenment.com.base.EnlightenmentApplication;
import enlightenment.com.contents.Constants;

/**
 * Created by admin on 2018/3/27.
 * @:param type (提问 1 or 内容 0)
 * @:param flag -1 取消，1 喜欢
 */

public class LiveBean {
    private String id;
    private String flag;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token=
            EnlightenmentApplication.getInstance().getString(Constants.Set.SET_USER_TOKEN);

    private LiveBean(){

    }

    private LiveBean(String id , String flag,String type){
        this.id=id;
        this.flag=flag;
        this.type=type;
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

    private String type;

    public static LiveBean createLiveBean(String id,String type){
        return new LiveBean(id,"1",type);
    }

    public static LiveBean createUnLiveBean(String id,String type){
        return new LiveBean(id,"-1",type);
    }
}
