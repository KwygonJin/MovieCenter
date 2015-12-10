package kwygonjin.com.moviecenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kwygonjin.com.moviecenter.adapters.MyViewAdapter;
import kwygonjin.com.moviecenter.db.MovieDBHelper;
import kwygonjin.com.moviecenter.items.Movie;
import kwygonjin.com.moviecenter.items.MovieListSingleton;
import kwygonjin.com.moviecenter.network.MovieFetcherAsync;
import kwygonjin.com.moviecenter.network.MovieHTTPParse;
import kwygonjin.com.moviecenter.network.MovieHTTPRequest;

public class MainActivity extends AppCompatActivity {
    public static String LOG_TAG = "my_log";
    public static final String APP_PREF = "mysettings";
    public static final String APP_PREF_KEY = "filmsId";
    public static final String LIST_STATE_KEY = "myState";
    public static SharedPreferences prefs;
    public static Set<String> favoriteFilmsId = new HashSet<String>();
    private GridLayoutManager gridLayoutManager;
    private Parcelable mListState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MovieDBHelper movieDBHelper = new MovieDBHelper(this);

        prefs = getSharedPreferences(MainActivity.APP_PREF, MODE_PRIVATE);
        favoriteFilmsId = prefs.getStringSet(APP_PREF_KEY, favoriteFilmsId);

        MyViewAdapter myViewAdapter = new MyViewAdapter(MainActivity.this);
        MovieListSingleton movieListSingleton = MovieListSingleton.getInstance();
        if (movieListSingleton.getMovieList().isEmpty())
            MovieHTTPRequest.doRequest(MainActivity.this, 1,myViewAdapter);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(myViewAdapter);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mListState = gridLayoutManager.onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, mListState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mListState != null) {
            gridLayoutManager.onRestoreInstanceState(mListState);
        }
    }
}
