package kwygonjin.com.moviecenter.network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kwygonjin.com.moviecenter.MainActivity;
import kwygonjin.com.moviecenter.items.Movie;

/**
 * Created by KwygonJin on 06.12.2015.
 */
public class MovieHTTPParse {
    public static List<Movie> movies = new ArrayList<Movie>();

    public static List<Movie> parseJSON(String result) {
        //MovieHTTPParse.movies = new ArrayList<Movie>();

        if (result == null) {
            return MovieHTTPParse.movies;
        }

        try {
            JSONObject jsonObject= new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectArr = jsonArray.getJSONObject(i);
                String id = jsonObjectArr.getString("id");
                boolean isFav = false;
                if (MainActivity.favoriteFilmsId.contains(id))
                    isFav = true;
                MovieHTTPParse.movies.add(new Movie(id, jsonObjectArr.getString("title"),
                        jsonObjectArr.getString("release_date"),
                        jsonObjectArr.getString("overview"),
                        jsonObjectArr.getString("poster_path"), isFav));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return MovieHTTPParse.movies;

    }
}
