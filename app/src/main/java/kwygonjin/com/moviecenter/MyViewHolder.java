package kwygonjin.com.moviecenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by KwygonJin on 26.11.2015.
 */
public class MyViewHolder extends RecyclerView.Adapter<MyViewHolder.MovieViewHolder> {
    public static String LOG_TAG = "my_log";

    private Context context;
    private static SharedPreferences prefs;
    private Set<String> favoriteFilmsId = new HashSet<String>();
    private List<Movie> movies = new ArrayList<Movie>();
    private static final String HTTP_SCHEME = "http";
    private static final String URL_AUTHORITY = "api.themoviedb.org";
    private static final String PARAM_PRIMARY_RELEASE_YEAR = "primary_release_year";
    private static final String PRIMARY_RELEASE_YEAR = "2015";
    private static final String PARAM_SORT_BY = "sort_by";
    private static final String SORT_BY = "popularity.desc";
    private static final String PARAM_API_KEY = "api_key";
    private static final String API_KEY = "cdc3a5a6e72d6b9235fce3707259f255"; //REMOVED

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView movieImg;
        CheckBox favorite;

        MovieViewHolder(View itemView) {
            super(itemView);
            movieImg = (ImageView)itemView.findViewById(R.id.movieImg);
            favorite = (CheckBox) itemView.findViewById(R.id.favorite);

        }
    }

    MyViewHolder(Context context){
        this.context = context;
        initData();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_list, viewGroup, false);
        MovieViewHolder mvh = new MovieViewHolder(v);
        final Movie movie;
        if (favoriteFilmsId != null) {
            movie = movies.get(i);
            mvh.favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                        @Override
                                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                            if (movie != null) {
                                                                if (isChecked)
                                                                    favoriteFilmsId.add(movie.getId());
                                                                else
                                                                    favoriteFilmsId.remove(movie.getId());
                                                                SharedPreferences.Editor e = prefs.edit();
                                                                e.putStringSet(MainActivity.APP_PREF_KEY, favoriteFilmsId);
                                                                e.apply();
                                                            }
                                                        }
                                                    }
            );
        }
        return mvh;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder movieViewHolder, int i) {
        Picasso.with(context).load(movies.get(i).getImgURL()).resize(200, 200).into(movieViewHolder.movieImg);
        movieViewHolder.favorite.setChecked(movies.get(i).isFavorite());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    private void initData() {
        prefs = context.getSharedPreferences(MainActivity.APP_PREF, context.MODE_PRIVATE);
        Set<String> favoriteFilmsId = prefs.getStringSet(MainActivity.APP_PREF_KEY, new HashSet<String>());
        MovieFetcherAsync task = new MovieFetcherAsync();
        task.execute(new Uri.Builder()
                .scheme(HTTP_SCHEME)
                .authority(URL_AUTHORITY)
                .appendPath("3")
                .appendPath("discover")
                .appendPath("movie")
                .appendQueryParameter(PARAM_PRIMARY_RELEASE_YEAR, PRIMARY_RELEASE_YEAR)
                .appendQueryParameter(PARAM_SORT_BY, SORT_BY)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .build().toString());
    }

    private class MovieFetcherAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String json = null;

            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null)
                    return null;
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null)
                    buffer.append(line + "\n");

                if (buffer.length() == 0)
                    return null;
                json = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) urlConnection.disconnect();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return json;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            if (result == null) {
                return;
            }

            try {
                JSONObject jsonObject= new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObjectArr = jsonArray.getJSONObject(i);
                    String id = jsonObjectArr.getString("id");
                    boolean isFav = false;
                    if (favoriteFilmsId.contains(id))
                        isFav = true;
                    movies.add(new Movie(id, jsonObjectArr.getString("title"),
                            jsonObjectArr.getString("release_date"),
                            jsonObjectArr.getString("overview"),
                            jsonObjectArr.getString("poster_path"), isFav));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
