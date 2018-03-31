package com.example.android.mymovies;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.example.android.mymovies.Adapters.FavMoviesAdapter;
import com.example.android.mymovies.Data.MovieSQLite;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Cristi on 3/28/2018.
 */

public class FavoritesActivity extends Activity implements FavMoviesAdapter.FavMovieAdapterOnClickHandler {

    public FavMoviesAdapter movie_db_adapter;
    @BindView(R.id.image_rv)
    RecyclerView mDBImagesItems;
    @BindView(R.id.empty_view)
    TextView mEmptyView;
    private ArrayList<Bitmap> moviesDBPoster;
    private MovieSQLite mDbMovieHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mDBImagesItems = findViewById(R.id.image_rv);
        mEmptyView = findViewById(R.id.empty_view);

        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns());
        mDBImagesItems.setLayoutManager(layoutManager);

        movie_db_adapter = new FavMoviesAdapter(this);
        mDBImagesItems.setAdapter(movie_db_adapter);

        if (moviesDBPoster != null) {
            movie_db_adapter.setMovieDBData(moviesDBPoster);
        }

    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }


    @Override
    public void onClick(int position) {

    }
}