package example.com.bean;

/**
 * Created by Administration on 2017/4/22.
 */
public class RecentMovieItem  {

    private String movieTitle;//电影名称
    private String movieDirector;//电影导演
    private String movieActors;//电影演员
    private String movieType;//电影类型
    private String movieCountry;//制作国家
    private String movieImageUrl;//电影剧照链接

    private String webViewUrl;//电影介绍链接

    private String howMuchSaw;//看过的人数

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieDirector() {
        return movieDirector;
    }

    public void setMovieDirector(String movieDirector) {
        this.movieDirector = movieDirector;
    }

    public String getMovieActors() {
        return movieActors;
    }

    public void setMovieActors(String movieActors) {
        this.movieActors = movieActors;
    }

    public String getMovieType() {
        return movieType;
    }

    public void setMovieType(String movieType) {
        this.movieType = movieType;
    }

    public String getMovieCountry() {
        return movieCountry;
    }

    public void setMovieCountry(String movieCountry) {
        this.movieCountry = movieCountry;
    }

    public String getMovieImageUrl() {
        return movieImageUrl;
    }

    public void setMovieImageUrl(String movieImageUrl) {
        this.movieImageUrl = movieImageUrl;
    }

    public String getWebViewUrl() {
        return webViewUrl;
    }

    public void setWebViewUrl(String webViewUrl) {
        this.webViewUrl = webViewUrl;
    }

    public String getHowMuchSaw() {
        return howMuchSaw;
    }

    public void setHowMuchSaw(String howMuchSaw) {
        this.howMuchSaw = howMuchSaw;
    }
}
