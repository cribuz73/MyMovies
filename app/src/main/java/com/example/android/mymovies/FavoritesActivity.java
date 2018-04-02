package com.example.android.mymovies;

import android.annotation.SuppressLint;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Cristi on 3/28/2018.
 */

public class FavoritesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = FavoritesActivity.class.getSimpleName();
    private static final int TASK_LOADER_ID = 0;
    @BindView(R.id.image_rv)
    RecyclerView mDBImagesItems;
    @BindView(R.id.empty_view)
    TextView mEmptyView;
    public List<String> favMoviesPath;
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

        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);

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

    //   public ArrayList<String> getPosterBitmapFromDB() {

    //   mDbMovieHelper = new MovieSQLite(context);


    //     String selectQuery = "SELECT * FROM " + MoviesEntry.TABLE_NAME;
    //     String[] projection = {
    //             MoviesEntry.COLUMN_POSTER
    //     };
    //     Cursor cursor = database.query(MoviesEntry.TABLE_NAME, projection, selectQuery, null, null, null, null);

//        postersBitmap = new ArrayList<>();
    //       if (cursor != null) {
    //           while (cursor.moveToNext()) {
    //              String posterPath = cursor.getString(cursor.getColumnIndex(MoviesEntry.COLUMN_POSTER));
    //              try {
    //                 File f = new File(posterPath);
    //                 Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
    //                 postersBitmap.add(b);
    //             } catch (FileNotFoundException e) {
    //                e.printStackTrace();
    //           }
    //       }
    //   }
    //   return postersBitmap;
    // }

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
        movie_db_adapter.setMovieData(favMoviesPath);
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        movie_db_adapter.setMovieData(null);

    }

    public List<String> readCursor(Cursor favData) {
        if (favData.getCount() <= 0) {
            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyView.setText(R.string.no_fav_movies);

        } else {
            favMoviesPath = new ArrayList<>();
            favData.moveToFirst();
            do {
                String moviePath = favData.getString(favData.getColumnIndex(
                        MoviesEntry.COLUMN_POSTER));

                favMoviesPath.add(moviePath);


            } while (favData.moveToNext());

        }
        return favMoviesPath;

    }


}