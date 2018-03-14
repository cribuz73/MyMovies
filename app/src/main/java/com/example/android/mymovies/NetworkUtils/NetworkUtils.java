package com.example.android.mymovies.NetworkUtils;

import android.net.Uri;

import com.example.android.mymovies.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Cristi on 3/9/2018.
 */

public class NetworkUtils {

    private static final String API_KEY = "968e8a5b5e65431470ac65b085ecfcd3";
    private static final String JSON_URL_BASE = "https://api.themoviedb.org/3/movie/?";



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
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
