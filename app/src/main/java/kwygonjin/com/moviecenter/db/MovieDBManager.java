package kwygonjin.com.moviecenter.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import kwygonjin.com.moviecenter.MainActivity;
import kwygonjin.com.moviecenter.interfaces.IDataManager;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import kwygonjin.com.moviecenter.items.Movie;
import kwygonjin.com.moviecenter.items.MovieListSingleton;
import kwygonjin.com.moviecenter.network.MovieHTTPRequest;

/**
 * Created by KwygonJin on 05.12.2015.
 */
public class MovieDBManager implements IDataManager<Movie> {
    private AtomicInteger mOpenCounter = new AtomicInteger();

    private static MovieDBHelper movieDBHelper;
    private static MovieDBManager instance;
    private SQLiteDatabase db;

    public static synchronized MovieDBManager getInstance(Context context) {
        if (instance == null) {
            instance = new MovieDBManager(context);
        }

        return instance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        if(mOpenCounter.incrementAndGet() == 1) {
            db = movieDBHelper.getWritableDatabase();
        }
        return db;
    }

    public synchronized void closeDatabase() {
        if(mOpenCounter.decrementAndGet() == 0) {
            db.close();
        }
    }

    @Override
    public long save(Movie movie) throws IOException {
        db = openDatabase();
        if (contains(movie)) {
            update(movie);
            return movie.getIdSQLite();
        }

        db.beginTransaction();
        ContentValues cv = new ContentValues();
        cv.put(MovieDBHelper.MOVIE_TITLE, movie.getName());
        cv.put(MovieDBHelper.MOVIE_YEAR, movie.getYear());
        cv.put(MovieDBHelper.MOVIE_DESC, movie.getDesc());
        cv.put(MovieDBHelper.MOVIE_ID, movie.getId());
        cv.put(MovieDBHelper.MOVIE_IMG, movie.getImgURL(Movie.WIDTH_500));
        cv.put(MovieDBHelper.MOVIE_RATE, movie.getRate());
        int favorite = 1;
        if (!movie.isFavorite())
            favorite = 0;
        cv.put(MovieDBHelper.MOVIE_FAV, favorite);

        long _id = db.insert(MovieDBHelper.TABLE_NAME_MOVIE, null, cv);
        if (_id != -1) {
            db.setTransactionSuccessful();
            movie.setIdSQLite(_id);
        }
        else throw new IOException();
        db.endTransaction();
        closeDatabase();

        return _id;
    }

    @Override
    public boolean delete(Movie movie) throws IOException {
        db = openDatabase();
        db.beginTransaction();
        long res = db.delete(MovieDBHelper.TABLE_NAME_MOVIE, MovieDBHelper.MOVIE_ID + " = ?", new String[]{movie.getId()});

        if (res != 0) {
            db.setTransactionSuccessful();
        }
        else throw new IOException();

        db.endTransaction();
        closeDatabase();

        return res != 0;
    }

    @Override
    public Movie get(String id) {
        db = openDatabase();

        Movie movie = null;
        Cursor cursor = db.query(MovieDBHelper.TABLE_NAME_MOVIE, null, MovieDBHelper.MOVIE_ID + " = ? ", new String[]{id}, null, null, null);

        while (cursor.moveToNext()) {
            boolean fav = false;
            if (cursor.getInt(cursor.getColumnIndex(MovieDBHelper.MOVIE_FAV)) == 1)
                fav = true;
            movie = new Movie(cursor.getString(cursor.getColumnIndex(MovieDBHelper.MOVIE_ID)),
                    cursor.getString(cursor.getColumnIndex(MovieDBHelper.MOVIE_TITLE)),
                    cursor.getString(cursor.getColumnIndex(MovieDBHelper.MOVIE_YEAR)),
                    cursor.getString(cursor.getColumnIndex(MovieDBHelper.MOVIE_DESC)),
                    cursor.getString(cursor.getColumnIndex(MovieDBHelper.MOVIE_IMG)),
                    fav,
                    cursor.getInt(cursor.getColumnIndex(MovieDBHelper.MOVIE_ID_SQL)),
                    cursor.getString(cursor.getColumnIndex(MovieDBHelper.MOVIE_RATE)));

        }
        cursor.close();
        closeDatabase();

        return movie;
    }

    @Override
    public List<Movie> getAll() {
        db = openDatabase();

        Cursor cursor = db.query(MovieDBHelper.TABLE_NAME_MOVIE, null, null, null, null, null, MovieDBHelper.MOVIE_RATE + " DESC ");
        MovieListSingleton movieListSingleton = MovieListSingleton.getInstance();
        List<Movie> movies = movieListSingleton.getMovieList();

        while (cursor.moveToNext()) {
            boolean isFav = false;
            if (MainActivity.favoriteFilmsId.contains(cursor.getString(cursor.getColumnIndex(MovieDBHelper.MOVIE_ID))))
                isFav = true;
            if (MainActivity.showOnlyFavorite && !isFav)
                continue;
            movies.add(new Movie(cursor.getString(cursor.getColumnIndex(MovieDBHelper.MOVIE_ID)),
                    cursor.getString(cursor.getColumnIndex(MovieDBHelper.MOVIE_TITLE)),
                    cursor.getString(cursor.getColumnIndex(MovieDBHelper.MOVIE_YEAR)),
                    cursor.getString(cursor.getColumnIndex(MovieDBHelper.MOVIE_DESC)),
                    cursor.getString(cursor.getColumnIndex(MovieDBHelper.MOVIE_IMG)),
                    isFav,
                    cursor.getInt(cursor.getColumnIndex(MovieDBHelper.MOVIE_ID_SQL)),
                    cursor.getString(cursor.getColumnIndex(MovieDBHelper.MOVIE_IMG))));

        }
        cursor.close();
        closeDatabase();

        return movies;
    }

    @Override
    public boolean contains(Movie movie) {
        db = openDatabase();

        Cursor cursor = db.query(MovieDBHelper.TABLE_NAME_MOVIE, null, MovieDBHelper.MOVIE_ID + " = ?", new String[]{movie.getId()}, null, null, null);
        while (cursor.moveToNext()) {
            cursor.close();
            closeDatabase();

            return true;
        }
        closeDatabase();

        return false;
    }

    @Override
    public boolean update(Movie movie) {
        db = openDatabase();
        db.beginTransaction();

        ContentValues cv = new ContentValues();
        cv.put(MovieDBHelper.MOVIE_TITLE, movie.getName());
        cv.put(MovieDBHelper.MOVIE_YEAR, movie.getYear());
        cv.put(MovieDBHelper.MOVIE_DESC, movie.getDesc());
        cv.put(MovieDBHelper.MOVIE_ID, movie.getId());
        cv.put(MovieDBHelper.MOVIE_IMG, movie.getImgURL(Movie.WIDTH_500));
        cv.put(MovieDBHelper.MOVIE_RATE, movie.getRate());
        int favorite = 1;
        if (!movie.isFavorite())
            favorite = 0;
        cv.put(MovieDBHelper.MOVIE_FAV, favorite);

        long _id =  db.update(MovieDBHelper.TABLE_NAME_MOVIE,
                cv,
                MovieDBHelper.MOVIE_ID + " = ?",
                new String[]{movie.getId()});

        if (_id != 0) {
            db.setTransactionSuccessful();
            db.endTransaction();
            closeDatabase();

            return true;
        } else {
            db.endTransaction();
            closeDatabase();

            return false;
        }

    }

    private MovieDBManager(Context context) {
        movieDBHelper = MovieDBHelper.getInstance(context);
    }

}
