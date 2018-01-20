package enlightenment.com.module;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lw on 2017/8/22.
 */

public class ModuleFatherBean implements Parcelable,Serializable{
    private static final long serialVersionUID = 1L;
    private String name;
    private int identity;
    private String introduction;
    private String picture;
    private ArrayList<ModuleChildBean> child;

    protected ModuleFatherBean(Parcel in) {
        name = in.readString();
        identity = in.readInt();
        introduction = in.readString();
        picture = in.readString();
        child = in.createTypedArrayList(ModuleChildBean.CREATOR);
    }

    public static final Creator<ModuleFatherBean> CREATOR = new Creator<ModuleFatherBean>() {
        @Override
        public ModuleFatherBean createFromParcel(Parcel in) {
            return new ModuleFatherBean(in);
        }

        @Override
        public ModuleFatherBean[] newArray(int size) {
            return new ModuleFatherBean[size];
        }
    };

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

    public ArrayList<ModuleChildBean> getChildBeen() {
        return child;
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

    public void setChild(ArrayList<ModuleChildBean> child) {
        this.child = child;
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
        parcel.writeTypedList(child);
    }
}
