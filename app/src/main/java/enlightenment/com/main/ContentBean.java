package enlightenment.com.main;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lw on 2017/9/5.
 */

public class ContentBean implements Parcelable{
    private int id;
    private String name;                //标题
    private String subtitle;            //副标题
    private String content;             //内容
    private String time;
    private String phone;               //创建人
    private String photo;
    private String video;
    private String audio;
    private String url;                 //
    private int viewType;
    private int fatherID;
    private int columnID;
    private int columnFatherID;
    private String visition;            //是否隐藏
    private String type;                //类型
    private String live;
    private String columnName;
    private String username;
    private String avatar;
    private String number;

    protected ContentBean(Parcel in) {
        id = in.readInt();
        name = in.readString();
        subtitle = in.readString();
        content = in.readString();
        time = in.readString();
        phone = in.readString();
        photo = in.readString();
        video = in.readString();
        audio = in.readString();
        url = in.readString();
        viewType = in.readInt();
        fatherID = in.readInt();
        columnID = in.readInt();
        columnFatherID = in.readInt();
        visition = in.readString();
        type = in.readString();
        live = in.readString();
        columnName = in.readString();
        username = in.readString();
        avatar = in.readString();
        number = in.readString();
    }

    public static final Creator<ContentBean> CREATOR = new Creator<ContentBean>() {
        @Override
        public ContentBean createFromParcel(Parcel in) {
            return new ContentBean(in);
        }

        @Override
        public ContentBean[] newArray(int size) {
            return new ContentBean[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public int getFatherID() {
        return fatherID;
    }

    public void setFatherID(int fatherID) {
        this.fatherID = fatherID;
    }

    public int getColumnID() {
        return columnID;
    }

    public void setColumnID(int columnID) {
        this.columnID = columnID;
    }

    public String getVisition() {
        return visition;
    }

    public void setVisition(String visition) {
        this.visition = visition;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLive() {
        return live;
    }

    public void setLive(String live) {
        this.live = live;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getColumnFatherID() {
        return columnFatherID;
    }

    public void setColumnFatherID(int columnFatherID) {
        this.columnFatherID = columnFatherID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(subtitle);
        dest.writeString(content);
        dest.writeString(time);
        dest.writeString(phone);
        dest.writeString(photo);
        dest.writeString(video);
        dest.writeString(audio);
        dest.writeString(url);
        dest.writeInt(viewType);
        dest.writeInt(fatherID);
        dest.writeInt(columnID);
        dest.writeInt(columnFatherID);
        dest.writeString(visition);
        dest.writeString(type);
        dest.writeString(live);
        dest.writeString(columnName);
        dest.writeString(username);
        dest.writeString(avatar);
        dest.writeString(number);
    }
}
