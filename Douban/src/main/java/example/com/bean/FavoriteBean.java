package example.com.bean;

import java.io.Serializable;

/**
 * Created by Administration on 2017/5/6.
 */
public class FavoriteBean implements Serializable{
    private int id;
    private String webUrl;
    private String favoriteImgUrl;
    private String favoriteTitle;
    private String favoriteContent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getFavoriteImgUrl() {
        return favoriteImgUrl;
    }

    public void setFavoriteImgUrl(String favoriteImgUrl) {
        this.favoriteImgUrl = favoriteImgUrl;
    }

    public String getFavoriteTitle() {
        return favoriteTitle;
    }

    public void setFavoriteTitle(String favoriteTitle) {
        this.favoriteTitle = favoriteTitle;
    }

    public String getFavoriteContent() {
        return favoriteContent;
    }

    public void setFavoriteContent(String favoriteContent) {
        this.favoriteContent = favoriteContent;
    }

    @Override
    public String toString() {
        return favoriteImgUrl+favoriteTitle+favoriteContent;
    }
}
