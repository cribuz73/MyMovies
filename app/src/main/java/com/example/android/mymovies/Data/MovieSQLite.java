package com.example.android.mymovies.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.mymovies.Data.DataContract.MoviesEntry;

/**
 * Created by Cristi on 3/27/2018.
 */

public class MovieSQLite extends SQLiteOpenHelper {

    public static final String LOG_TAG = MovieSQLite.class.getSimpleName();

    private static final String DATABASE_NAME = "fav_movies.db";
    private static final int DATABASE_VERSION = 1;

    public MovieSQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MoviesEntry.TABLE_NAME + " ("
                + MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MoviesEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "
                + MoviesEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + MoviesEntry.COLUMN_POSTER + " TEXT NOT NULL, "
                + MoviesEntry.COLUMN_BACKDROP + " TEXT NOT NULL, "
                + MoviesEntry.COLUMN_OVERVIEW + " TEXT, "
                + MoviesEntry.COLUMN_RELEASE_DATE + " TEXT, "
                + MoviesEntry.COLUMN_RATING + " REAL);";


        Log.v(LOG_TAG, SQL_CREATE_MOVIES_TABLE);

        db.execSQL(SQL_CREATE_MOVIES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
