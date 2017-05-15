package example.com.bean;

import java.io.Serializable;

/**
 * Created by Administration on 2017/4/14.
 */
public class BookItem implements Serializable {
    private String bookImgUrl;
    private String bookTitle;
    private String author;

    public String getBookImgUrl() {
        return bookImgUrl;
    }

    public void setBookImgUrl(String bookImgUrl) {
        this.bookImgUrl = bookImgUrl;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
