package kwygonjin.com.moviecenter.adapters;

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

import java.util.ArrayList;
import java.util.List;

import kwygonjin.com.moviecenter.Constants;
import kwygonjin.com.moviecenter.MainActivity;
import kwygonjin.com.moviecenter.db.MovieDBManager;
import kwygonjin.com.moviecenter.items.Movie;
import kwygonjin.com.moviecenter.MovieAdvActivity;
import kwygonjin.com.moviecenter.R;
import kwygonjin.com.moviecenter.items.MovieListSingleton;
import kwygonjin.com.moviecenter.network.MovieHTTPRequest;

/**
 * Created by KwygonJin on 26.11.2015.
 */
public class MyViewAdapter extends RecyclerView.Adapter<MyViewAdapter.MovieViewHolder> {
    private Context context;
    private List<Movie> movies = new ArrayList<Movie>();
    private static MyViewAdapter myViewAdapter = null;

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView movieImg;
        public CheckBox favorite;
        public int position;

        MovieViewHolder(View itemView) {
            super(itemView);
            movieImg = (ImageView)itemView.findViewById(R.id.movieImg);
            favorite = (CheckBox) itemView.findViewById(R.id.favorite);

            movieImg.setOnClickListener(this);
            favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                                    @Override
                                                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                                        if (MovieListSingleton.getInstance().getMovieList().get(position) != null) {
                                                                            MovieListSingleton.getInstance().getMovieList().get(position).setFavorite(isChecked);
                                                                            if (isChecked)
                                                                                MainActivity.favoriteFilmsId.add(MovieListSingleton.getInstance().getMovieList().get(position).getId());
                                                                            else
                                                                                MainActivity.favoriteFilmsId.remove(MovieListSingleton.getInstance().getMovieList().get(position).getId());

                                                                            SharedPreferences.Editor e = MainActivity.prefs.edit();
                                                                            e.putStringSet(MainActivity.APP_PREF_KEY_FILM_ID, MainActivity.favoriteFilmsId);
                                                                            e.apply();

                                                                            MovieDBManager movieDBManager = MovieDBManager.getInstance(context);
                                                                            movieDBManager.update(MovieListSingleton.getInstance().getMovieList().get(position));
                                                                        }
                                                                    }
                                                                }
            );
        }

        public void  setPosition(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, MovieAdvActivity.class);
            intent.putExtra("movie_object", MovieListSingleton.getInstance().getMovieList().get(position));
            context.startActivity(intent);
        }
    }

    private MyViewAdapter(Context context){
        this.context = context;
    }

    public static synchronized MyViewAdapter getInstance(Context context){
        if(myViewAdapter == null)
            myViewAdapter = new MyViewAdapter(context);

        return myViewAdapter;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_list, null);
        MovieViewHolder mvh = new MovieViewHolder(v);
        //mvh.setPosition(i);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder movieViewHolder, int i) {
        movieViewHolder.setPosition(i);
        Picasso.with(context).load(MovieListSingleton.getInstance().getMovieList().get(i).getImgURL(Movie.WIDTH_500)).placeholder(R.drawable.place_holder).into(movieViewHolder.movieImg);
        movieViewHolder.favorite.setChecked(MovieListSingleton.getInstance().getMovieList().get(i).isFavorite());
        if (i == getItemCount()-1 && !MainActivity.showOnlyFavorite)
            MovieHTTPRequest.doRequest(context, (getItemCount()/20) + 1, this);
   }

    @Override
    public int getItemCount() {
        return MovieListSingleton.getInstance().getMovieList().size();
    }


}
