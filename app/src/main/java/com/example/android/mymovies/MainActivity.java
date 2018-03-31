package com.example.android.mymovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.mymovies.Adapters.MovieAdapter;
import com.example.android.mymovies.AsyncUtils.MyAsyncTask;
import com.example.android.mymovies.NetworkUtils.Movie;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static String option = "popular";
    public MovieAdapter mAdapter;
    public List<Movie> moviesResult;


    private RecyclerView mImagesItems;
    private TextView mEmptyView;

    public static String getOpt() {
        return option;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImagesItems = findViewById(R.id.image_rv);
        mEmptyView = findViewById(R.id.empty_view);


        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns());

        mImagesItems.setLayoutManager(layoutManager);

        mImagesItems.setHasFixedSize(true);

        mAdapter = new MovieAdapter(this);

        mImagesItems.setAdapter(mAdapter);


        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


        if (networkInfo != null && networkInfo.isConnected()) {

            loadMovieData();

        } else {

            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            mEmptyView.setText(R.string.no_internet_connection);
        }
    }

    private void loadMovieData() {
        MyAsyncTask asyncTask = new MyAsyncTask(new MyAsyncTask.AsyncInterface() {
            @Override
            public void onTaskComplete(List movies) {
                View loadingIndicator = findViewById(R.id.loading_indicator);
                loadingIndicator.setVisibility(View.GONE);

                moviesResult = movies;

                if (movies != null) {
                    mAdapter.setMovieData(movies);
                }
            }
        });
        asyncTask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.most_popular:
                option = "popular";
                loadMovieData();
                return true;
            case R.id.top_rated:
                option = "top_rated";
                loadMovieData();
                return true;
            case R.id.favorites:
                Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int position) {

        Movie movie = moviesResult.get(position);
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("currentMovie", movie);
        startActivity(intent);
    }
}
