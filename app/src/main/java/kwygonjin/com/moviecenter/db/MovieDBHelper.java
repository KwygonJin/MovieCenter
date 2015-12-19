package kwygonjin.com.moviecenter.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by KwygonJin on 05.12.2015.
 */
public class MovieDBHelper extends SQLiteOpenHelper {
    private static MovieDBHelper instance = null;

    public static final String MOVIE_TITLE = "movie_title";
    public static final String MOVIE_ID_SQL = "id";
    public static final String MOVIE_YEAR = "movie_year";
    public static final String MOVIE_DESC = "movie_desc";
    public static final String MOVIE_ID = "movie_id";
    public static final String MOVIE_FAV = "movie_fav";
    public static final String MOVIE_IMG = "movie_img";
    public static final String MOVIE_RATE = "movie_rate";

    public static final String DATABASE_NAME = "movie_database";
    public static final int DATABASE_VERSION = 5;
    public static final String TABLE_NAME_MOVIE = "movies";

    private Context context;

    public static final String CREATE_TABLE_MOVIE = "CREATE TABLE " + TABLE_NAME_MOVIE +" ( " +
        MOVIE_ID_SQL + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "+
            MOVIE_TITLE + " TEXT, "+
            MOVIE_ID + " INTEGER NOT NULL, "+
            MOVIE_YEAR + " TEXT, "+
            MOVIE_DESC + " TEXT, "+
            MOVIE_FAV + " INTEGER, " +
            MOVIE_RATE + " TEXT, " +
            MOVIE_IMG + " TEXT) ";

    private MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static synchronized  MovieDBHelper getInstance(Context context) {
        if(instance == null)
            instance = new MovieDBHelper(context);

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE " + TABLE_NAME_MOVIE);
        onCreate(db);
    }
}
