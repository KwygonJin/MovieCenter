package kwygonjin.com.moviecenter;

import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    public static String LOG_TAG = "my_log";
    public static final String APP_PREF = "mysettings";
    public static final String APP_PREF_KEY = "filmsId";
    public static SharedPreferences prefs;
    public static Set<String> favoriteFilmsId = new HashSet<String>();
    public List<Movie> movies = new ArrayList<Movie>();
    private static final String HTTP_SCHEME = "http";
    private static final String URL_AUTHORITY = "api.themoviedb.org";
    private static final String PARAM_PRIMARY_RELEASE_YEAR = "primary_release_year";
    private static final String PRIMARY_RELEASE_YEAR = "2015";
    private static final String PARAM_SORT_BY = "sort_by";
    private static final String SORT_BY = "popularity.desc";
    private static final String PARAM_API_KEY = "api_key";
    private static final String API_KEY = "cdc3a5a6e72d6b9235fce3707259f255"; //REMOVED

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        //gridLayoutManager.setSpanCount(2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(new MyViewAdapter(MainActivity.this, this.movies));
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void initData() {
        prefs = getSharedPreferences(MainActivity.APP_PREF, MODE_PRIVATE);
        Set<String> favoriteFilmsId = prefs.getStringSet(MainActivity.APP_PREF_KEY, new HashSet<String>());
        MovieFetcherAsync task = new MovieFetcherAsync(this.movies);
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

}
