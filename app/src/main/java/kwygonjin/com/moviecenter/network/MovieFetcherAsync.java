package kwygonjin.com.moviecenter.network;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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
import java.util.List;

import kwygonjin.com.moviecenter.MainActivity;
import kwygonjin.com.moviecenter.R;
import kwygonjin.com.moviecenter.db.MovieDBManager;
import kwygonjin.com.moviecenter.items.Movie;
import kwygonjin.com.moviecenter.adapters.MyViewAdapter;
import kwygonjin.com.moviecenter.items.MovieListSingleton;

/**
 * Created by KwygonJin on 01.12.2015.
 */
public class MovieFetcherAsync extends AsyncTask<String, Integer, String> {

    private Context context;
    private MyViewAdapter myViewAdapter;

    public MovieFetcherAsync(Context context, MyViewAdapter myViewAdapter) {
        this.context = context;
        this.myViewAdapter = myViewAdapter;
    }

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
            MovieDBManager movieDBManager = MovieDBManager.getInstance(context);
            MovieListSingleton.getInstance().getMovieList().addAll(movieDBManager.getAll());
            Log.e(MainActivity.LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(MainActivity.LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return json;
    }

    @Override
    protected void onPostExecute(String result) {

        super.onPostExecute(result);

        if (result == null) {
            myViewAdapter.notifyDataSetChanged();
            return;
        }

        if (!result.isEmpty()) {
            MovieHTTPParse.parseJSON(result, context);
            myViewAdapter.notifyDataSetChanged();
            return;
        }

    }

}