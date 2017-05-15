package cniao5.com.admin.bean;

import java.io.Serializable;

/**
 * Created by Administration on 2017/5/5.
 */
public class OrderBean extends Ware{
    private int id;
    private String name;
    private String imgUrl;
    private String description;
    private Float price;
    private Double wareListPrice;

    private String detailHrefUrl;
    private int count;

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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getWareListPrice() {
        return wareListPrice;
    }

    public void setWareListPrice(Double wareListPrice) {
        this.wareListPrice = wareListPrice;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getDetailHrefUrl() {
        return detailHrefUrl;
    }

    public void setDetailHrefUrl(String detailHrefUrl) {
        this.detailHrefUrl = detailHrefUrl;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
