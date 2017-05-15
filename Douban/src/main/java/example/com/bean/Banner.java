package example.com.bean;

/**
 * Created by Administration on 2017/4/14.
 */
public class Banner extends BaseBean {
    private  String name;
    private  String imgUrl;
    private  String imgDetailUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgDetailUrl() {
        return imgDetailUrl;
    }

    public void setImgDetailUrl(String imgDetailUrl) {
        this.imgDetailUrl = imgDetailUrl;
    }
}
