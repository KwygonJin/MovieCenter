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
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.movie_list, parent, false);

            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.movieImage = (ImageView) convertView.findViewById(R.id.movieImg);
            //viewHolder.movieName = (TextView) convertView.findViewById(R.id.movieName);
            int height = parent.getHeight();
            if (height > 0) {
                ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
                layoutParams.height = (int) (height / 2);
            } // for the 1st item parent.getHeight() is not calculated yet
            //viewHolder.movieImage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MainActivity.);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // update the item view
        Movie item = movies.get(position);
        Picasso.with(context).load(item.getImgURL()).into(viewHolder.movieImage);
        //viewHolder.movieName.setText(item.getName());

        return convertView;
    }

    private static class ViewHolder {
        ImageView movieImage;
        TextView movieName;
    }
}
