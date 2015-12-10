package kwygonjin.com.moviecenter.items;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by KwygonJin on 26.11.2015.
 */
public class Movie implements Parcelable {
    private String id;
    private String name;
    private String year;
    private String desc;
    private String imgURL;
    private boolean favorite;
    private long idSQLite;
    private String rate;
    private static final String URL_IMAGE_PATH = "http://image.tmdb.org/t/p/";
    public static final String WIDTH_154 = "w154";
    public static final String WIDTH_342 = "w342";
    public static final String WIDTH_500 = "w500";
    public static final String WIDTH_780 = "w780";

    public Movie(String id, String name, String year, String desc, String imgURL, boolean favorite, long idSQLite, String rate) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.desc = desc;
        this.imgURL = imgURL;
        this.favorite = favorite;
        this.idSQLite = idSQLite;
        this.rate = rate;
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

    public String getImgURL(String preferedWidth) {
        return getFullPosterPath(this.imgURL, preferedWidth);
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Movie(Parcel in) {
        readFromParcel(in);
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public long getIdSQLite() {
        return idSQLite;
    }

    public void setIdSQLite(long idSQLite) {
        this.idSQLite = idSQLite;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in ) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(year);
        dest.writeString(desc);
        dest.writeString(imgURL);
        dest.writeString(id);
    }

    private void readFromParcel(Parcel in ) {
        this.name = in.readString();
        this.year = in.readString();
        this.desc = in.readString();
        this.imgURL = in.readString();
        this.id = in.readString();
    }

    private String getFullPosterPath(String thumbPath, String preferedWidth) {
        StringBuilder sb = new StringBuilder();
        sb.append(URL_IMAGE_PATH);
        sb.append(preferedWidth);
        sb.append(thumbPath);

        return sb.toString();
    }

}
