package kwygonjin.com.moviecenter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashSet;
import java.util.Set;

import kwygonjin.com.moviecenter.adapters.MyViewAdapter;
import kwygonjin.com.moviecenter.db.MovieDBManager;
import kwygonjin.com.moviecenter.items.MovieListSingleton;
import kwygonjin.com.moviecenter.network.MovieHTTPRequest;
import kwygonjin.com.moviecenter.services.UpdateMoviesService;

public class MainActivity extends AppCompatActivity {
    public static String LOG_TAG = "my_log";
    public final static String FILE_NAME = "filename";
    public static final String APP_PREF = "mysettings";
    public static final String APP_PREF_KEY_FILM_ID = "filmsId";
    public static final String APP_PREF_KEY_SHOW_FAV = "showOnlyFavorite";
    public static SharedPreferences prefs;
    public static Set<String> favoriteFilmsId = new HashSet<String>();
    public static boolean showOnlyFavorite;
    private MyViewAdapter myViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(MainActivity.APP_PREF, MODE_PRIVATE);
        favoriteFilmsId = prefs.getStringSet(APP_PREF_KEY_FILM_ID, favoriteFilmsId);
        showOnlyFavorite = prefs.getBoolean(APP_PREF_KEY_SHOW_FAV, showOnlyFavorite);
        myViewAdapter = MyViewAdapter.getInstance(MainActivity.this);
        //MovieListSingleton movieListSingleton = MovieListSingleton.getInstance();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(myViewAdapter);

        startService(new Intent(MainActivity.this, UpdateMoviesService.class));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (MovieListSingleton.getInstance().getMovieList().isEmpty()){
            if (MovieHTTPRequest.isInternetConnection(MainActivity.this) && !showOnlyFavorite)
                MovieHTTPRequest.doRequest(MainActivity.this, 1,myViewAdapter);
            else {
                MovieDBManager movieDBManager = MovieDBManager.getInstance(MainActivity.this);
                MovieListSingleton.getInstance().getMovieList().addAll(movieDBManager.getAll());
                myViewAdapter.notifyDataSetChanged();
            }
        }
    }
}
