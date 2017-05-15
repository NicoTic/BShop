package cniao5.com.admin.bean;

import java.io.Serializable;

/**
 * Created by Administration on 2017/4/20.
 */
public class ShoppingCartWare extends Ware implements Serializable{
    /**
     * 购买的数量
     */
    private int count;
    /**
     * 是否勾选
     */
    private boolean isChecked = true;

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

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "count=" + count +
                ", isCheck=" + isChecked +
                '}';
    }
}
