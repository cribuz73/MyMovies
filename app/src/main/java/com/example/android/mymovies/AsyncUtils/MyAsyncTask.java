package com.example.android.mymovies.AsyncUtils;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.mymovies.NetworkUtils.Movie;
import com.example.android.mymovies.NetworkUtils.NetworkJson;
import com.example.android.mymovies.NetworkUtils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by Cristi on 3/15/2018.
 */

public class MyAsyncTask extends AsyncTask<String, Void, List<Movie>> {

    private static final String TAG = "Fetch data MyAsynkTask";
    public AsyncInterface delegate = null;

    public MyAsyncTask(AsyncInterface delegate) {
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Movie> doInBackground(String... strings) {

        URL jsonURL = NetworkUtils.buildUrl();
        String movieJson;

        try {
            movieJson = NetworkUtils.getResponseFromHttpUrl(jsonURL);

            List<Movie> movies;
            movies = NetworkJson.extractMovieFromJson(movieJson);
            return movies;

        } catch (IOException e) {
            Log.e(TAG, "Problem making the HTTP request.", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        delegate.onTaskComplete(movies);
    }


    public interface AsyncInterface<T> {
        void onTaskComplete(List<Movie> movies);
    }
}
