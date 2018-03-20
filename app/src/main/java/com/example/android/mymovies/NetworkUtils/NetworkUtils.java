package com.example.android.mymovies.NetworkUtils;

import android.net.Uri;

import com.example.android.mymovies.BuildConfig;
import com.example.android.mymovies.DetailActivity;
import com.example.android.mymovies.MainActivity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Cristi on 3/9/2018.
 */

public class NetworkUtils {

    public static final String API_KEY = BuildConfig.API_KEY;
    public static final String JSON_URL_BASE = "https://api.themoviedb.org/3/movie?";
    public static final String TRAILERS = "videos";
    private static final String REVIEWS = "reviews";
    public static int movieID;




    public static URL buildUrl() {

        String opt_1 = MainActivity.getOpt();

        Uri baseUri = Uri.parse(JSON_URL_BASE);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendEncodedPath(opt_1);
        uriBuilder.appendQueryParameter("api_key", API_KEY);
        uriBuilder.build();

        URL url = null;
        try {
            url = new URL(uriBuilder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static URL trailerUrl() {

        movieID = DetailActivity.getID();
        String mID = String.valueOf(movieID);

        Uri baseUri = Uri.parse(JSON_URL_BASE);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendEncodedPath(mID);
        uriBuilder.appendEncodedPath(TRAILERS);
        uriBuilder.appendQueryParameter("api_key", API_KEY);
        uriBuilder.build();

        URL urlTrailer = null;
        try {
            urlTrailer = new URL(uriBuilder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return urlTrailer;
    }

    public static URL reviewUrl() {

        movieID = DetailActivity.getID();
        String mID = String.valueOf(movieID);

        Uri baseUri = Uri.parse(JSON_URL_BASE);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendEncodedPath(mID);
        uriBuilder.appendEncodedPath(REVIEWS);
        uriBuilder.appendQueryParameter("api_key", API_KEY);
        uriBuilder.build();

        URL url = null;
        try {
            url = new URL(uriBuilder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

}
