package com.example.android.mymovies.NetworkUtils;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristi on 3/9/2018.
 */

public class NetworkJson {

    private static final String LOG_TAG = NetworkJson.class.getSimpleName();

    private NetworkJson() {
    }

    public static List<Movie> extractMovieFromJson(String json) {

        if (TextUtils.isEmpty(json)) {
            return null;
        }

        ArrayList<Movie> movies = new ArrayList<>();
        try {
            JSONObject baseJsonObject = new JSONObject(json);

            JSONArray moviesArray = baseJsonObject.optJSONArray("results");

            for (int i = 0; i < moviesArray.length(); i++) {

                JSONObject movieObject = moviesArray.optJSONObject(i);

                String movieName = movieObject.optString("title");
                String releaseDate = movieObject.optString("release_date");
                double voteAverage = movieObject.optDouble("vote_average");
                String overview = movieObject.optString("overview");
                String poster = movieObject.optString("poster_path");
                String backdrop = movieObject.optString("backdrop_path");
                int id = movieObject.optInt("id");

                Movie movie = new Movie(movieName, releaseDate, voteAverage, overview, poster, backdrop, id);
                movies.add(movie);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error in Json", e);
        }

        return movies;
    }


    public static ArrayList<String> extractTrailerFromJson(String json) {

        if (TextUtils.isEmpty(json)) {
            return null;
        }

        ArrayList<String> trailers = new ArrayList<>();
        try {
            JSONObject baseJsonObject = new JSONObject(json);

            JSONArray moviesArray = baseJsonObject.optJSONArray("results");

            for (int i = 0; i < moviesArray.length(); i++) {

                JSONObject movieObject = moviesArray.optJSONObject(i);

                String trailerKey = movieObject.optString("key");


                trailers.add(trailerKey);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error in Json", e);
        }

        return trailers;
    }

    public static List<Review> extractReviewFromJson(String json) {

        if (TextUtils.isEmpty(json)) {
            return null;
        }

        ArrayList<Review> reviews = new ArrayList<>();
        try {
            JSONObject baseJsonObject = new JSONObject(json);

            JSONArray moviesArray = baseJsonObject.optJSONArray("results");

            for (int i = 0; i < moviesArray.length(); i++) {

                JSONObject movieObject = moviesArray.optJSONObject(i);

                String revAuthor = movieObject.optString("author");
                String revContent = movieObject.optString("content");


                Review review = new Review(revAuthor, revContent);
                reviews.add(review);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error in Json", e);
        }

        return reviews;
    }
}
