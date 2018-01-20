package enlightenment.com.module;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lw on 2017/8/30.
 */

public class ModuleChildBean implements Parcelable,Serializable{
    private static final long serialVersionUID = 2L;
    private String name;
    private int identity;
    private String introduction;
    private String picture;

    public String getName() {
        return name;
    }

    public int getIdentity() {
        return identity;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getPicture() {
        return picture;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(identity);
        parcel.writeString(introduction);
        parcel.writeString(picture);
    }
    public static final Creator<ModuleChildBean> CREATOR = new Creator<ModuleChildBean>() {
        @Override
        public ModuleChildBean createFromParcel(Parcel in) {
            return new ModuleChildBean(in);
        }

        @Override
        public ModuleChildBean[] newArray(int size) {
            return new ModuleChildBean[size];
        }
    };

    public ModuleChildBean(Parcel in){
        name=in.readString();
        identity=in.readInt();
        introduction=in.readString();
        picture=in.readString();
    }
}
