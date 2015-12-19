package kwygonjin.com.moviecenter.network;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kwygonjin.com.moviecenter.MainActivity;
import kwygonjin.com.moviecenter.db.MovieDBManager;
import kwygonjin.com.moviecenter.items.Movie;
import kwygonjin.com.moviecenter.items.MovieListSingleton;

/**
 * Created by KwygonJin on 06.12.2015.
 */
public class MovieHTTPParse {

    public static void parseJSON(String result, Context context) {
        MovieListSingleton movieListSingleton = MovieListSingleton.getInstance();

        if (result == null) {
            return;
        }

        try {
            MovieDBManager movieDBManager = MovieDBManager.getInstance(context);
            JSONObject jsonObject= new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectArr = jsonArray.getJSONObject(i);
                String id = jsonObjectArr.getString("id");
                boolean isFav = false;
                if (MainActivity.favoriteFilmsId.contains(id))
                    isFav = true;
                if (MainActivity.showOnlyFavorite && !isFav)
                    continue;
                Movie movie = new Movie(id, jsonObjectArr.getString("title"),
                        jsonObjectArr.getString("release_date"),
                        jsonObjectArr.getString("overview"),
                        jsonObjectArr.getString("poster_path"), isFav, -1,
                        jsonObjectArr.getString("poster_path"));
                if ((!MainActivity.showOnlyFavorite) || (MainActivity.showOnlyFavorite && isFav))
                    movieListSingleton.getMovieList().add(movie);
                movieDBManager.save(movie);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

    }
}
