package enlightenment.com.main;

/**
 * Created by lw on 2017/9/5.
 */

public class ContentBean {
    private int id;
    private String name;                //标题
    private String subtitle;            //副标题
    private String content_details;     //内容
    private String time;
    private String accounts;            //创建人
    private String photo;
    private String video;
    private String audio;
    private String url;                 //
    private String visition;            //是否隐藏
    private String type;                //类型
    private String richtext;            //富文本
    private String father_id;           //父ID
    private String live;
    private String commentaries;


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setContent_details(String content_details) {
        this.content_details = content_details;
    }

    public void setAccounts(String accounts) {
        this.accounts = accounts;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setVisition(String visition) {
        this.visition = visition;
    }

    public void setRichtext(String richtext) {
        this.richtext = richtext;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFather_id(String father_id) {
        this.father_id = father_id;
    }

    public void setLive(String live) {
        this.live = live;
    }

    public void setCommentaries(String commentaries) {
        this.commentaries = commentaries;
    }

    public String getCommentaries() {
        return commentaries;
    }

    public String getLive() {
        return live;
    }

    public String getFather_id() {
        return father_id;
    }

    public String getRichtext() {
        return richtext;
    }

    public String getType() {
        return type;
    }

    public String getVisition() {
        return visition;
    }

    public String getUrl() {
        return url;
    }

    public String getAudio() {
        return audio;
    }

    public String getVideo() {
        return video;
    }

    public String getPhoto() {
        return photo;
    }

    public String getAccounts() {
        return accounts;
    }

    public String getTime() {
        return time;
    }

    public String getContent_details() {
        return content_details;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
