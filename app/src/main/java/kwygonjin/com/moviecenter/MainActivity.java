package kwygonjin.com.moviecenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import kwygonjin.com.moviecenter.items.Movie;
import kwygonjin.com.moviecenter.network.MovieFetcherAsync;
import kwygonjin.com.moviecenter.network.MovieHTTPParse;
import kwygonjin.com.moviecenter.network.MovieHTTPRequest;

public class MainActivity extends AppCompatActivity {
    public static String LOG_TAG = "my_log";
    public static final String APP_PREF = "mysettings";
    public static final String APP_PREF_KEY = "filmsId";
    public static SharedPreferences prefs;
    public static Set<String> favoriteFilmsId = new HashSet<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(MainActivity.APP_PREF, MODE_PRIVATE);
        favoriteFilmsId = prefs.getStringSet(APP_PREF_KEY, favoriteFilmsId);
        if (MovieHTTPParse.movies.isEmpty())
            MovieHTTPRequest.doRequest(MainActivity.this, 1);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        boolean b = false;
        while (MovieHTTPParse.movies.isEmpty()){
            b = true;
        }
        recyclerView.setAdapter(new MyViewAdapter(MainActivity.this));
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
}
