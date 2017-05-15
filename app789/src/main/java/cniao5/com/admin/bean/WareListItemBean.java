package cniao5.com.admin.bean;

/**
 * Created by Administration on 2017/4/25.
 */
public class WareListItemBean {
    private String imgUrl;
    private String wareTitle;
    private String warePrice;

    private String webHrefUrl;//网页链接

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getWareTitle() {
        return wareTitle;
    }

    public void setWareTitle(String wareTitle) {
        this.wareTitle = wareTitle;
    }

    public String getWarePrice() {
        return warePrice;
    }

    public void setWarePrice(String warePrice) {
        this.warePrice = warePrice;
    }

    public String getWebHrefUrl() {
        return webHrefUrl;
    }

    public void setWebHrefUrl(String webHrefUrl) {
        this.webHrefUrl = webHrefUrl;
    }
}
