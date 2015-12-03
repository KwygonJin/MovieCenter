package kwygonjin.com.moviecenter;

import android.os.AsyncTask;
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
import java.util.List;

/**
 * Created by KwygonJin on 01.12.2015.
 */
public class MovieFetcherAsync extends AsyncTask<String, Integer, String> {
    private List<Movie> movies;

    MovieFetcherAsync (List<Movie> movies) {
        this.movies = movies;
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
            return;
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