package enlightenment.com.information;

import java.util.ArrayList;

/**
 * Created by lw on 2018/2/28.
 */

public class ColumnBean implements Comparable{
    private int id;
    private String name;
    private String introduction;
    private String time;
    private String phone;

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

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArrayList<ColumnChildBean> getChild() {
        return child;
    }

    public void setChild(ArrayList<ColumnChildBean> child) {
        this.child = child;
    }

    private String picture;
    private int type;
    private ArrayList<ColumnChildBean> child;

    @Override
    public int compareTo(Object o) {
        return (this.getType() < ((ColumnBean)o).getType() ? -1 : 1);
    }

    public class ColumnChildBean {
        private int id;
        private String name;
        private String introduction;
        private String time;
        private int location;
        private String picture;
        private int type;
        private int fatherID;

        public int getFatherID() {
            return fatherID;
        }

        public void setFatherID(int fatherID) {
            this.fatherID = fatherID;
        }

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

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getLocation() {
            return location;
        }

        public void setLocation(int location) {
            this.location = location;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
