package kwygonjin.com.moviecenter.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import kwygonjin.com.moviecenter.interfaces.IDataManager;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import kwygonjin.com.moviecenter.items.Movie;
import kwygonjin.com.moviecenter.items.MovieListSingleton;
import kwygonjin.com.moviecenter.network.MovieHTTPRequest;

/**
 * Created by KwygonJin on 05.12.2015.
 */
public class MovieDBManager implements IDataManager<Movie> {
    private MovieDBHelper movieDBHelper;
    private static MovieDBManager mInstance = null;
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock readLock = rwl.readLock();
    private final Lock writeLock = rwl.writeLock();

    private MovieDBManager(Context context) {
        movieDBHelper = MovieDBHelper.getmInstance(context);
        movieDBHelper.setWriteAheadLoggingEnabled(true);
    }

    public static MovieDBManager getInstance (Context context){
        if(mInstance == null)
        {
            mInstance = new MovieDBManager(context);
        }
        return mInstance;
    }

    @Override
    public long save(Movie movie) throws IOException {
        if (contains(movie)) {
            update(movie);
            return movie.getIdSQLite();
        }

        SQLiteDatabase db = movieDBHelper.getWritableDatabase();
        writeLock.lock();
        db.beginTransactionNonExclusive();
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

        writeLock.unlock();
        db.close();

        return _id;
    }

    @Override
    public boolean delete(Movie movie) throws IOException {
        SQLiteDatabase db = movieDBHelper.getWritableDatabase();
        writeLock.lock();
        db.beginTransactionNonExclusive();
        long res = db.delete(MovieDBHelper.TABLE_NAME_MOVIE, MovieDBHelper.MOVIE_ID + " = ? ", new String[]{movie.getId()});
        if (res != 0) {
            db.setTransactionSuccessful();
        }
        else throw new IOException();
        db.close();
        writeLock.unlock();
        return res != 0;
    }

    @Override
    public Movie get(String id) {
        Movie movie = null;
        SQLiteDatabase db = movieDBHelper.getReadableDatabase();
        readLock.lock();
        Cursor cursor = db.query(MovieDBHelper.TABLE_NAME_MOVIE,null ,MovieDBHelper.MOVIE_ID + " = ? ", new String[] {id}, null, null, null);
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
        db.close();
        readLock.unlock();
        return movie;
    }

    @Override
    public List<Movie> getAll() {

        SQLiteDatabase db = movieDBHelper.getReadableDatabase();
        readLock.lock();
        Cursor cursor = db.query(MovieDBHelper.TABLE_NAME_MOVIE, null, null, null, null, null, MovieDBHelper.MOVIE_RATE + " DESC ");
        MovieListSingleton movieListSingleton = MovieListSingleton.getInstance();
        List<Movie> movies = movieListSingleton.getMovieList();

        while (cursor.moveToNext()) {
            boolean fav = false;
            if (cursor.getInt(cursor.getColumnIndex(MovieDBHelper.MOVIE_FAV)) == 1)
                fav = true;
            movies.add(new Movie(cursor.getString(cursor.getColumnIndex(MovieDBHelper.MOVIE_ID)),
                    cursor.getString(cursor.getColumnIndex(MovieDBHelper.MOVIE_TITLE)),
                    cursor.getString(cursor.getColumnIndex(MovieDBHelper.MOVIE_YEAR)),
                    cursor.getString(cursor.getColumnIndex(MovieDBHelper.MOVIE_DESC)),
                    cursor.getString(cursor.getColumnIndex(MovieDBHelper.MOVIE_IMG)),
                    fav,
                    cursor.getInt(cursor.getColumnIndex(MovieDBHelper.MOVIE_ID_SQL)),
                    cursor.getString(cursor.getColumnIndex(MovieDBHelper.MOVIE_IMG))));

        }
        db.close();
        readLock.unlock();
        return movies;
    }

    @Override
    public boolean contains(Movie movie) {
        SQLiteDatabase db = movieDBHelper.getReadableDatabase();
        readLock.lock();
        Cursor cursor = db.query(MovieDBHelper.TABLE_NAME_MOVIE,null ,MovieDBHelper.MOVIE_ID + " = ? ", new String[] {movie.getId()}, null, null, null);
        while (cursor.moveToNext()) {
            db.close();
            readLock.unlock();
            return true;
        }
        db.close();
        readLock.unlock();
        return false;
    }

    @Override
    public boolean update(Movie movie) {
        writeLock.lock();
        SQLiteDatabase db = movieDBHelper.getWritableDatabase();
        db.beginTransactionNonExclusive();
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
                MovieDBHelper.MOVIE_ID + " = ? ",
                new String[]{movie.getId()});

        if (_id != 0) {
            db.setTransactionSuccessful();
            db.close();
            writeLock.unlock();
            return true;
        } else {
            db.close();
            writeLock.unlock();
            return false;
        }

    }
}
