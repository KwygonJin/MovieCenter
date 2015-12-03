package kwygonjin.com.moviecenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieAdvActivity extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_adv_);

        ImageView iv = (ImageView) findViewById(R.id.movieImgAdv);
        TextView movieName = (TextView) findViewById(R.id.movieNameAdv);
        TextView movieYear = (TextView) findViewById(R.id.movieYearAdv);
        TextView movieDesc = (TextView) findViewById(R.id.movieDescAdv);

        Movie movie = getIntent().getExtras().getParcelable("movie_object");
        this.setTitle("Movie: " + movie.getName());
        if (!movie.getImgURL().isEmpty())
            Picasso.with(this).load(movie.getImgURL()).resize(70, 70).into(iv);
        if (!movie.getName().isEmpty())
            movieName.setText(movie.getName());
        if (!movie.getYear().isEmpty())
            movieYear.setText(movie.getYear());
        if (!movie.getDesc().isEmpty())
            movieDesc.setText(movie.getDesc());
    }

}
