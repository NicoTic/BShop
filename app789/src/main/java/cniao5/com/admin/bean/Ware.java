package cniao5.com.admin.bean;

/**
 * Created by Administration on 2017/4/7.
 */
public class Ware {
    private int count;
    private boolean isChecked;

    private String address;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    private int id;
    private String name;
    private String imgUrl;
    private String description;
    private Float price;
    private Double wareListPrice;

    private String detailHrefUrl;

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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Double getWareListPrice() {
        return wareListPrice;
    }

    public void setWareListPrice(Double wareListPrice) {
        this.wareListPrice = wareListPrice;
    }

    public String getDetailHrefUrl() {
        return detailHrefUrl;
    }

    public void setDetailHrefUrl(String detailHrefUrl) {
        this.detailHrefUrl = detailHrefUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
