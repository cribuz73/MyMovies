package com.example.android.mymovies;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.example.android.mymovies.NetworkUtils.Movie;
import com.example.android.mymovies.NetworkUtils.NetworkJson;
import com.example.android.mymovies.NetworkUtils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private MovieAdapter mAdapter;
    private RecyclerView mImagesItems;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImagesItems = findViewById(R.id.image_rv);


        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mImagesItems.setLayoutManager(layoutManager);

        mImagesItems.setHasFixedSize(true);

        mAdapter = new MovieAdapter(this);

        mImagesItems.setAdapter(mAdapter);

        loadMovieData();
    }

    private void loadMovieData() {
        // showWeatherDataView();

        //  String location = SunshinePreferences.getPreferredWeatherLocation(this);
        new FetchMovieTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(String... lists) {


            URL jsonURL = NetworkUtils.buildUrl();
            String movieJson = null;

            try {
                movieJson = NetworkUtils.getResponseFromHttpUrl(jsonURL);

                List<Movie> movies = NetworkJson.extractMovieFromJson(movieJson);
                return movies;

            } catch (IOException e) {
                Log.e(TAG, "Problem making the HTTP request.", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            if (movies != null) {
                mAdapter.setMovieData(movies);
            }
        }
    }
    @Override
    public void onClick(String oneMovie) {
        Context context = this;
        Toast.makeText(context, oneMovie, Toast.LENGTH_SHORT)
                .show();
    }
}
