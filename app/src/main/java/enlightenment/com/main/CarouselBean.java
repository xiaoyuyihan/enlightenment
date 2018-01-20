package enlightenment.com.main;

/**
 * Created by lw on 2017/9/24.
 */

public class CarouselBean {
    private String name;
    private String photo;

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public CarouselBean setPhoto(String photo){
        this.photo=photo;
        return this;
    }
}
