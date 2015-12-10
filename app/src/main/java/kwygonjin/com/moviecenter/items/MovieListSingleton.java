package kwygonjin.com.moviecenter.items;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KwygonJin on 10.12.2015.
 */
public class MovieListSingleton {
    private static MovieListSingleton mInstance = null;
    private List<Movie> movies;

    private MovieListSingleton(){
        this.movies = new ArrayList<Movie>();
    }

    public static MovieListSingleton getInstance(){
        if(mInstance == null)
        {
            mInstance = new MovieListSingleton();
        }
        return mInstance;
    }

    public List<Movie> getMovieList(){
        return this.movies;
    }
}
