package kwygonjin.com.moviecenter;

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
    private static final String IMAGE_PATH = "http://image.tmdb.org/t/p/original";

    public Movie(String id, String name, String year, String desc, String imgURL, boolean favorite) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.desc = desc;
        this.imgURL = IMAGE_PATH + imgURL;
        this.favorite = favorite;
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

}
