package kwygonjin.com.moviecenter.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;

import kwygonjin.com.moviecenter.R;
import kwygonjin.com.moviecenter.adapters.MyViewAdapter;

/**
 * Created by KwygonJin on 06.12.2015.
 */
public class MovieHTTPRequest {
    private static final String HTTP_SCHEME = "http";
    private static final String URL_AUTHORITY = "api.themoviedb.org";
    private static final String PARAM_PRIMARY_RELEASE_YEAR = "primary_release_year";
    private static final String PRIMARY_RELEASE_YEAR = "2015";
    private static final String PARAM_SORT_BY = "sort_by";
    private static final String SORT_BY = "popularity.desc";
    private static final String PARAM_API_KEY = "api_key";
    private static final String PARAM_PAGE = "page";
    private static final String API_KEY = "cdc3a5a6e72d6b9235fce3707259f255"; //REMOVED

    public static void doRequest(Context context, int pageNumber, MyViewAdapter myViewAdapter) {
        MovieFetcherAsync task = new MovieFetcherAsync(context, myViewAdapter);
        task.execute(new Uri.Builder()
                .scheme(HTTP_SCHEME)
                .authority(URL_AUTHORITY)
                .appendPath("3")
                .appendPath("discover")
                .appendPath("movie")
                .appendQueryParameter(PARAM_PRIMARY_RELEASE_YEAR, PRIMARY_RELEASE_YEAR)
                .appendQueryParameter(PARAM_SORT_BY, SORT_BY)
                .appendQueryParameter(PARAM_PAGE, Integer.toString(pageNumber))
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .build().toString());
    }

    public static boolean isInternetConnection(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));

        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
