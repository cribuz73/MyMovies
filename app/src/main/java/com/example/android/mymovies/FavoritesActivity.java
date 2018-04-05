package com.example.android.mymovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.mymovies.Adapters.DbMoviesAdapter;
import com.example.android.mymovies.Data.DataContract.MoviesEntry;
import com.example.android.mymovies.NetworkUtils.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Cristi on 3/28/2018.
 */

public class FavoritesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, DbMoviesAdapter.DbMovieAdapterOnClickHandler {

    private static final String TAG = FavoritesActivity.class.getSimpleName();
    private static final int MOVIE_LOADER_ID = 0;
    @BindView(R.id.image_rv)
    RecyclerView mDBImagesItems;
    @BindView(R.id.empty_view)
    TextView mEmptyView;
    public List<Movie> favMovies;
    private DbMoviesAdapter movie_db_adapter;
    private Cursor mMoviesPath;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mDBImagesItems = findViewById(R.id.image_rv);
        mEmptyView = findViewById(R.id.empty_view);

        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns());
        mDBImagesItems.setLayoutManager(layoutManager);

        movie_db_adapter = new DbMoviesAdapter(this);

        mDBImagesItems.setAdapter(movie_db_adapter);

        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }



    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle args) {

        return new AsyncTaskLoader<Cursor>(this) {


            @Override
            protected void onStartLoading() {
                if (mMoviesPath != null) {
                    deliverResult(mMoviesPath);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {

                    return getContentResolver().query(MoviesEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                mMoviesPath = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        readCursor(data);
        movie_db_adapter.setMovieData(favMovies);
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        movie_db_adapter.setMovieData(null);

    }

    public List<Movie> readCursor(Cursor favData) {
        if (favData.getCount() <= 0) {
            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyView.setText(R.string.no_fav_movies);

        } else {
            favMovies = new ArrayList<>();
            favData.moveToFirst();
            do {
                String movieName = favData.getString(favData.getColumnIndex(
                        MoviesEntry.COLUMN_TITLE));
                String releaseDate = favData.getString(favData.getColumnIndex(MoviesEntry.COLUMN_RELEASE_DATE));
                Double voteAverage = favData.getDouble(favData.getColumnIndex(MoviesEntry.COLUMN_RATING));
                String overview = favData.getString(favData.getColumnIndex(MoviesEntry.COLUMN_OVERVIEW));
                String poster_path = favData.getString(favData.getColumnIndex(MoviesEntry.COLUMN_POSTER));
                String backdrop_path = favData.getString(favData.getColumnIndex(MoviesEntry.COLUMN_BACKDROP));
                int favMovieID = favData.getInt(favData.getColumnIndex(MoviesEntry.COLUMN_MOVIE_ID));

                favMovies.add(new Movie(movieName, releaseDate, voteAverage, overview, poster_path, backdrop_path, favMovieID));

            } while (favData.moveToNext());
        }
        return favMovies;
    }


    @Override
    public void onClick(int position) {
        Movie movie = favMovies.get(position);
        Intent intent = new Intent(FavoritesActivity.this, FavDetailsActivity.class);
        intent.putExtra("currentFavMovie", movie);
        startActivity(intent);
    }
}