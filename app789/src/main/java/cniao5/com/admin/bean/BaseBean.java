package cniao5.com.admin.bean;

import java.io.Serializable;

/**
 * Created by Administration on 15/10/2.
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
