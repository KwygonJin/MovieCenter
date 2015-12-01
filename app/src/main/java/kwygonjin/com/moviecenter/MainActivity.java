package kwygonjin.com.moviecenter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    public static final String APP_PREF = "mysettings";
    public static final String APP_PREF_KEY = "filmsId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        //gridLayoutManager.setSpanCount(2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(new MyViewAdapter(MainActivity.this));

    }

}
