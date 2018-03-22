package com.example.android.mymovies.AsyncUtils;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.mymovies.NetworkUtils.NetworkJson;
import com.example.android.mymovies.NetworkUtils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by Cristi on 3/18/2018.
 */

public class TrailersAsynkTask extends AsyncTask<String, Void, List<String>> {

    private static final String TAG = "Fetch dat TrailerAsyncT";
    public TrailerAsyncIntf delegate = null;

    public TrailersAsynkTask(TrailerAsyncIntf delegate) {
        this.delegate = delegate;
    }

    @Override
    protected List<String> doInBackground(String... strings) {
        URL jsonURL = NetworkUtils.trailerUrl();
        String trailerJson;

        try {
            trailerJson = NetworkUtils.getResponseFromHttpUrl(jsonURL);

            List<String> trailers;
            trailers = NetworkJson.extractTrailerFromJson(trailerJson);
            return trailers;

        } catch (IOException e) {
            Log.e(TAG, "Problem making the HTTP request.", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<String> trailers) {
        delegate.onTaskComplete(trailers);
    }

    public interface TrailerAsyncIntf<T> {
        void onTaskComplete(List<String> trailers);
    }
}
