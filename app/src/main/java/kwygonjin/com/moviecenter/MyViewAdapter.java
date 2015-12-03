package kwygonjin.com.moviecenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by KwygonJin on 26.11.2015.
 */
public class MyViewAdapter extends RecyclerView.Adapter<MyViewAdapter.MovieViewHolder> {
    private Context context;
    private List<Movie> movies;

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        protected ImageView movieImg;
        protected CheckBox favorite;

        MovieViewHolder(View itemView) {
            super(itemView);
            movieImg = (ImageView)itemView.findViewById(R.id.movieImg);
            favorite = (CheckBox) itemView.findViewById(R.id.favorite);

        }
    }

    MyViewAdapter(Context context, List<Movie> movies){
        this.context = context;
        this.movies = movies;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_list, null);
        MovieViewHolder mvh = new MovieViewHolder(v);

        return mvh;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder movieViewHolder, int i) {
        Picasso.with(context).load(movies.get(i).getImgURL()).resize(200, 200).into(movieViewHolder.movieImg);
        movieViewHolder.favorite.setChecked(movies.get(i).isFavorite());
        final Movie movie = movies.get(i);
        if (MainActivity.favoriteFilmsId != null) {
            movieViewHolder.favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                        @Override
                                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                            if (movie != null) {
                                                                if (isChecked)
                                                                    MainActivity.favoriteFilmsId.add(movie.getId());
                                                                else
                                                                    MainActivity.favoriteFilmsId.remove(movie.getId());
                                                                SharedPreferences.Editor e = MainActivity.prefs.edit();
                                                                e.putStringSet(MainActivity.APP_PREF_KEY, MainActivity.favoriteFilmsId);
                                                                e.apply();
                                                            }
                                                        }
                                                    }
            );
        }

        movieViewHolder.movieImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MovieAdvActivity.class);
                intent.putExtra("movie_object", movie);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }



}
