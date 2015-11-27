package kwygonjin.com.moviecenter;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static String LOG_TAG = "my_log";
    protected List<Movie> movies = new ArrayList<Movie>();
    private static final String HTTP_SCHEME = "http";
    private static final String URL_AUTHORITY = "api.themoviedb.org";
    private static final String PARAM_PRIMARY_RELEASE_YEAR = "primary_release_year";
    private static final String PRIMARY_RELEASE_YEAR = "2015";
    private static final String PARAM_SORT_BY = "sort_by";
    private static final String SORT_BY = "popularity.desc";
    private static final String PARAM_API_KEY = "api_key";
    private static final String API_KEY = "cdc3a5a6e72d6b9235fce3707259f255"; //REMOVED
    private static final String IMAGE_PATH = "http://image.tmdb.org/t/p/original";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();

        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new MovieAdapter(this, movies));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intent = new Intent(MainActivity.this, MovieAdv_Activity.class);
                intent.putExtra("movie_object", movies.get(position));
                startActivity(intent);
            }
        });
    }

    private void initData() {
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
                    movies.add(new Movie(jsonObjectArr.getString("title"), jsonObjectArr.getString("release_date"), jsonObjectArr.getString("overview"), IMAGE_PATH + jsonObjectArr.getString("poster_path")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG);
        }
    }
}
