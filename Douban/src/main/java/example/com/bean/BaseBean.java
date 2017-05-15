package example.com.bean;

import java.io.Serializable;

/**
 * Created by Administration on 2017/4/14.
 */
public class BaseBean implements Serializable {
    protected   long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
