package kwygonjin.com.moviecenter;

/**
 * Created by KwygonJin on 26.11.2015.
 */
public class Movie {
    private String name;
    private String year;
    private String desc;
    private String imgURL;

    public Movie(String name, String year, String desc, String imgURL) {
        this.name = name;
        this.year = year;
        this.desc = desc;
        this.imgURL = imgURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
