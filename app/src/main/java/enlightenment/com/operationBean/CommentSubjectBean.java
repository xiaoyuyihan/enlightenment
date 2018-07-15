package enlightenment.com.operationBean;

import enlightenment.com.base.EnlightenmentApplication;
import enlightenment.com.contents.Constants;

/**
 * Created by admin on 2018/3/27.
 */

public class CommentSubjectBean {
    private String identity;
    private String content;
    private String type;
    private String content_id;

    public String getContent_id() {
        return content_id;
    }

    public void setContent_id(String content_id) {
        this.content_id = content_id;
    }

    private String token=
            EnlightenmentApplication.getInstance().getString(Constants.Set.SET_USER_TOKEN);

    /**
     *
     * @param identity  评论ID
     * @param content   内容
     * @param type      类型
     * @param content_id    所在内容ID
     */
    public CommentSubjectBean(String identity, String content, String type, String content_id){
        this.identity=identity;
        this.content=content;
        this.type=type;
        this.content_id=content_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static CommentSubjectBean createAnswerCommentBean(String identity, String content){
        return new CommentSubjectBean(identity,content,"0",identity);
    }

    public static CommentSubjectBean createCommentCommentBean(String identity, String content, String content_id){
        return new CommentSubjectBean(identity,content,"2",content_id);
    }

    public static CommentSubjectBean createContentCommentBean(String identity, String content){
        return new CommentSubjectBean(identity,content,"1",identity);
    }
}
