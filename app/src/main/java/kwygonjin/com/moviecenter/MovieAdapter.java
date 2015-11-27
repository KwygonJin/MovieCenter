package kwygonjin.com.moviecenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by KwygonJin on 26.11.2015.
 */
public class MovieAdapter extends BaseAdapter {
    private Context context;
    private List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.movie_list, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.movieImage = (ImageView) convertView.findViewById(R.id.movieImg);
            int height = parent.getHeight();
            if (height > 0) {
                ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
                layoutParams.height = (int) (height / 2);
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Movie item = movies.get(position);
        Picasso.with(context).load(item.getImgURL()).fit().into(viewHolder.movieImage);

        return convertView;
    }

    private static class ViewHolder {
        ImageView movieImage;
        TextView movieName;
    }
}
